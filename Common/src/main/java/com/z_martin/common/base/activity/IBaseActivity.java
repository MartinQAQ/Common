package com.z_martin.common.base.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.z_martin.common.base.app.IBaseView;

public interface IBaseActivity extends IBaseView {
    
    void startNewActivity(@NonNull Class<?> clz);

    void startNewActivity(@NonNull Class<?> clz, Intent intent);

    void startNewActivity(@NonNull Class<?> clz, Bundle bundle);

    void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode);
}
