package com.example.anup.urlreadandupdate;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements IPresenter {

    private final IView view;
    private final IModel model;
    long totalWordCount;
    int ten=10;
    StringBuilder tenthCharStringBuilder = new StringBuilder();
    private boolean tenthCharFound;
    int offsetCharLength=10;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(IView view, IModel model) {
        this.view=view;
        this.model=model;
    }

    @Override
    public void loadContent() {
        if(view.isNetworkAvailable()){
            view.showProgress();
            Observable<String> observable = model.getObservable();

            //add observer for 10th char
            compositeDisposable.add(
                    observable
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(tenthTextObserver)
            );

            //add observer for every 10th char
            compositeDisposable.add(
                    observable
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(everyTenthCharObserver)
            );


            //add observer for word count char
            compositeDisposable.add(
                    observable
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(wordCountObserver)
            );

        }else{
            view.showError("No Network Available !!!");
        }
    }

    DisposableObserver<String> tenthTextObserver = new DisposableObserver<String>() {

        int countOffset=0;

        @Override
        public void onComplete() {
            //Do Nothing
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(String s) {

            if(!tenthCharFound  ){
                if( s.length() >= ten-countOffset){
                    view.hideProgress();
                    tenthCharFound = true;
                    view.updateTenthCharTv(String.valueOf(s.charAt(ten-countOffset -1)));
                    tenthTextObserver.dispose();
                }else{
                    countOffset = s.length();
                }


            }
        }

    };

    DisposableObserver<String> everyTenthCharObserver = new DisposableObserver<String>() {
        int countOffset=0;

        @Override
        public void onComplete() {
            //Do Nothing
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(String s) {
            Log.d("onNext",s );

            // read every 10th char of the string from the last 10th char printed
            if(s.length() > ten-countOffset){
                for(int i = ten-countOffset; i<=s.length(); i=i+ten){
                    tenthCharStringBuilder.append(s.charAt(i-1));
                    view.updateEveryTenthCharTv(tenthCharStringBuilder.toString());
                }
            }
            countOffset = (s.length() + countOffset) % ten ;
            Log.d("offset ",""+countOffset);
        }

    };


    DisposableObserver<String> wordCountObserver = new DisposableObserver<String>() {
        @Override
        public void onComplete() {
            view.updateTotalWordCountTv(String.valueOf(totalWordCount));
        }

        @Override
        public void onError(Throwable e) {
            view.showError(e.getMessage());
        }

        @Override
        public void onNext(String s) {
            totalWordCount += findWordCount(s);
            view.updateTotalWordCountTv(String.valueOf(totalWordCount));
        }

    };

    public long findWordCount(String input) {
        final int end = 0;
        final int start = 1;
        int state = end;
        long wc = 0;  // word count
        int i = 0;

        // Scan all characters one by one
        while (i < input.length())
        {
            //(i.e. space, tab, line break,)
            if (input.charAt(i) == ' ' || input.charAt(i) == '\n'
                    || input.charAt(i) == '\t')
                state = end;

            else if (state == end)
            {
                state = start;
                ++wc;
            }

            // Move to next character
            ++i;
        }
        return wc;
    }

    @Override
    public void onFinish() {
        compositeDisposable.clear();
    }
}
