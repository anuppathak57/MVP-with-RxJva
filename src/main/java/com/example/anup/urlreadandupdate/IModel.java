package com.example.anup.urlreadandupdate;


import io.reactivex.Observable;

public interface IModel {

    Observable<String> getObservable();
}
