package com.example.anup.urlreadandupdate;

public interface IView {

    void showProgress();
    void hideProgress();
    void showError(String error);
    void updateTenthCharTv(String text);
    void updateEveryTenthCharTv(String text);
    void updateTotalWordCountTv(String text);
    boolean isNetworkAvailable();
}
