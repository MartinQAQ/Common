package com.z_martin.common.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.CallSuper;
import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.aries.ui.view.title.TitleBarView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.z_martin.common.R;
import com.z_martin.common.base.app.AppConfig;
import com.z_martin.common.base.app.AppManager;
import com.z_martin.common.base.app.AppStatus;
import com.z_martin.common.base.app.AppStatusManager;
import com.z_martin.common.base.app.BaseApp;
import com.z_martin.common.base.app.InjectManager;
import com.z_martin.common.entity.NetworkChangeEvent;
import com.z_martin.common.utils.AppUtils;
import com.z_martin.common.utils.LoadingDialog;
import com.z_martin.common.utils.ScreenUtil;
import com.z_martin.common.utils.SpUtils;
import com.z_martin.common.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity implements LifecycleProvider<ActivityEvent>, IBaseActivity {
    protected BaseApp mApplication;
    private LoadingDialog dialog;
    private LoadingDialog.Builder builder;
    protected Context mContext;
    protected boolean isTransAnim;
    protected Unbinder unbinder;

    protected boolean mCheckNetWork = true;
    View mTipView;
    WindowManager mWindowManager;
    WindowManager.LayoutParams mLayoutParams;
    protected ImmersionBar mImmersionBar;


    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        onBeforeBind();
        init(savedInstanceState);
    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    @CallSuper
    protected void onResume() {
        lifecycleSubject.onNext(ActivityEvent.RESUME);
        super.onResume();
//        hasNetWork(NetManagerUtils.isOpenNetwork(mContext));
//        MobclickAgent.onResume(this);
    }

    @Override
    @CallSuper
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
        super.onPause();
        hideDialog();
//        MobclickAgent.onPause(this);
    }

    @Override
    @CallSuper
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    protected void onBeforeBind() {}

    private boolean isSetScreen = true;
    public void setSetScreen(){
        isSetScreen = false;
    }
    
    private boolean initImmersionBar = true;
    public void setImmersionBar(){
        initImmersionBar = false;
    }

    private void init(Bundle savedInstanceState) {

//        if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYCLE){
//            Intent intent = new Intent(this, AppConfig.getSplashActivity());
//            startActivity(intent);
//            AppManager.getAppManager().finishAllActivity();
////            finish();
//            return;
//        }
        if (isSetScreen) {
            ScreenUtil.resetDensity(this);
        }
        InjectManager.inject(this);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this);
        if (initImmersionBar) {
            initImmersionBar();
        }
        builder = new LoadingDialog.Builder(this);
        initData();
        initTipView();
        initView(savedInstanceState);
        setCheckNetWork(false);
        AppManager.getAppManager().addActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(final NetworkChangeEvent event) {
        hasNetWork(event.isConnected);
    }
    //
    private void hasNetWork(boolean has) {
        if (isCheckNetWork()) {
            if (has) {
                if (mTipView != null && mTipView.getParent() != null) {
                    mWindowManager.removeView(mTipView);
                }
            } else {
                if (mTipView.getParent() == null) {
                    mWindowManager.addView(mTipView, mLayoutParams);
                }
            }
        }
    }

    public void setCheckNetWork(boolean checkNetWork) {
        mCheckNetWork = checkNetWork;
    }

    public boolean isCheckNetWork() {
        return mCheckNetWork;
    }

    private void initTipView() {
        LayoutInflater inflater = getLayoutInflater();
        mTipView = inflater.inflate(R.layout.layout_network_tip, null); 
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
        mTipView.findViewById(R.id.error_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                if (isTransAnim)
                    overridePendingTransition(R.anim.activity_start_trans_in, R.anim
                            .activity_start_trans_out);
            }
        });
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.hideBar(BarHide.FLAG_SHOW_BAR).init();
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
//    }


    protected abstract void initView(Bundle savedInstanceState);

    protected void initData() {
        mContext = this;
        mApplication = (BaseApp) getApplication();
        isTransAnim = true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                AppUtils.hideKeyboard(ev, view, this);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void startNewActivity(Class<?> clz) {
        startActivity(new Intent(this, clz));
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_trans_in, R.anim
                    .activity_start_trans_out);
    }

    @Override
    public void startNewActivity(Class<?> clz, Intent intent) {
        intent.setClass(this, clz);
        startActivity(intent);
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_trans_in, R.anim
                    .activity_start_trans_out);
    }

    @Override
    public void startNewActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_trans_in, R.anim
                    .activity_start_trans_out);
    }

    public void startNewActivityForResult(Class<?> clz, Bundle bundle,
                                          int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_start_trans_in, R.anim
                    .activity_start_trans_out);
    }

    @Override
    public void showDialog(String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = builder.create(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    
    @Override
    public void showDialog() {
        showDialog(null);
    }
    
    @Override
    public void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void showToast(int id) {
        ToastUtils.showToast(mContext, id);
    }

    @Override
    public void hideKeyboard() {
        View view = getCurrentFocus();
        assert view != null;
        AppUtils.hideSoftInput(view);
    }

    @Override
    public void back() {
        super.onBackPressedSupport();
    }

    @Override
    public void showNetworkError() {

    }
    @Override
    public void showNetworkError(String msg) {
        if (msg.contains("500")) {
        } else if (msg.contains("401")) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Bundle bundle1 = new Bundle();
            setIsTransAnim(false);
            intent.setClass(this, AppConfig.getTokenErrorActivity());
            intent.putExtras(bundle1);
            startActivity(intent);
            AppConfig.setAuthorization("");
            SpUtils.remove(AppUtils.getContext(), "user_info");
//            AppUtils.getContext().startActivity(new Intent(AppUtils.getContext(), AppConfig.getTokenErrorActivity()));
            AppManager.getAppManager().finishAllActivity();
            finish();
//            if(!AppManager.getAppManager().isOpenActivity(AppConfig.getTokenErrorActivity())) {
//                AppConfig.setAuthorization("");
//                SpUtils.remove(AppUtils.getContext(), "user_info");
//                AppUtils.getContext().startActivity(new Intent(AppUtils.getContext(), AppConfig.getTokenErrorActivity()));
//                AppManager.getAppManager().finishAllActivity();
//                finish();
//            }
        }
    }
    
    @Override
    public void showNetworkError(int msg) {

    }

    @Override
    public void finish() {
        super.finish();
        if (mTipView != null && mTipView.getParent() != null) {
            mWindowManager.removeView(mTipView);
        }
        if (isTransAnim)
            overridePendingTransition(R.anim.activity_finish_trans_in, R.anim
                    .activity_finish_trans_out);
    }

    protected boolean hiddenKeyboard() {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), 0);
    }

    protected void initTitleBarView(TitleBarView toolbar, int title) {
        toolbar.setTitleMainText(title).setLeftTextDrawable(R.mipmap.back_white).setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }

    protected void initTitleBarView(TitleBarView toolbar, String title) {
        toolbar.setLeftTextPadding(40,0, 40,0).setTitleMainText(title).setLeftTextDrawable(R.mipmap.back_white).setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedSupport();
            }
        });
    }


    protected boolean isTransAnim() {
        return isTransAnim;
    }

    public void setIsTransAnim(boolean b) {
        isTransAnim = b;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenUtil.resetDensity(this);
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
        if (unbinder != null)  unbinder.unbind();
        EventBus.getDefault().unregister(this);
        AppManager.getAppManager().finishActivity(this);
        System.gc();
        System.runFinalization();
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration !=null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }
}
