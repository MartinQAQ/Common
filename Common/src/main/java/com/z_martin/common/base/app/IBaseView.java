package com.z_martin.common.base.app;

public interface IBaseView {

    void showToast(String msg);

    void showToast(int msg);

    void showDialog(String waitMsg);
    
    void showDialog(int waitMsg);

    void showDialog();

    void hideDialog();

    void hideKeyboard();

    void back();

    void showNetworkError();

    void showNetworkError(String msg);
    
    void showNetworkError(int msg);
}
