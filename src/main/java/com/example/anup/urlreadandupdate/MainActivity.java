package com.example.anup.urlreadandupdate;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements IView{

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tenthCharTv , everyTenthCharTv , totalCountTv;
    private MainPresenter mPresenter;
    private View contentLayout , startButton , progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter=new MainPresenter(this,new ModelDataSource(AppConstants.URL));
        initViews();
    }

    private void initViews() {
        tenthCharTv = findViewById(R.id.tenthCharValueTv);
        everyTenthCharTv = findViewById(R.id.everyTenthCharValueTv);
        totalCountTv = findViewById(R.id.totalWordsValueTv);
        contentLayout = findViewById(R.id.content_layout);
        startButton = findViewById(R.id.start_button);
        progressBar = findViewById(R.id.progressBar);
        startButton.setOnClickListener(v -> mPresenter.loadContent());
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        contentLayout.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(getString(R.string.alert))
                .setMessage(error)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void updateTenthCharTv(String text) {
        tenthCharTv.setText(text);
    }

    @Override
    public void updateEveryTenthCharTv(String text) {
        everyTenthCharTv.setText(text);
    }

    @Override
    public void updateTotalWordCountTv(String text) {
        totalCountTv.setText(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onFinish();
        mPresenter = null;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        else
            return false;


    }
}
