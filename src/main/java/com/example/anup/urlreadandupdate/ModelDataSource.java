package com.example.anup.urlreadandupdate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;

public class ModelDataSource implements IModel{

    String url;

    public ModelDataSource(String url) {
        this.url = url;
    }


    @Override
    public Observable<String> getObservable() {
        return Observable.defer(() -> new FileObservableSource(url));
    }

    class FileObservableSource implements ObservableSource<String> {

        private final String filename;

        FileObservableSource(String filename) {
            this.filename = filename;
        }

        @Override
        public void subscribe(Observer<? super String> observer) {
            try {
                URL website = new URL(filename);
                URLConnection connection = website.openConnection();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    observer.onNext(line);
                }
                reader.close();
            }catch (Exception e) {
                observer.onError(e);
            }
            observer.onComplete();

        }
    }
}
