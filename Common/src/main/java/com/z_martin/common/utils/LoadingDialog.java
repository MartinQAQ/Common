package com.z_martin.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.z_martin.common.R;

public class LoadingDialog extends Dialog {
    private Context mContext;

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        Window window = this.getWindow();
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            window.setAttributes(layoutParams);
        }
    }

    public static class Builder {

        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public LoadingDialog create(String msg) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.layout_loading_dialog, null);  
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view); 
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);   
            TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView); 
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.loading_animation);
            spaceshipImage.startAnimation(animation);
            if(StringUtils.isEmpty(msg)) {
                tipTextView.setVisibility(View.GONE);
            } else {
                tipTextView.setVisibility(View.VISIBLE);
                tipTextView.setText(msg);
            }
            LoadingDialog loadingDialog = new LoadingDialog(mContext, R.style.loading_dialog);  
            loadingDialog.setCancelable(false);   
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));  
            Window dialogWindow = loadingDialog.getWindow();
            assert dialogWindow != null;
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics d = AppUtils.getContext().getResources().getDisplayMetrics(); 
            lp.width = d.widthPixels;
            lp.height = d.heightPixels;
            dialogWindow.getAttributes().gravity = Gravity.CENTER;
            return loadingDialog;
        }

        public LoadingDialog create() {
            return create("");
        }

        public LoadingDialog create(int msg) {
            return create((String) mContext.getText(msg));
        }
    }
}
