package com.livestar.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "rxjava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"observer----" + "onCompleted" + "---->" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"observer----" + "onError" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,"observer----" + "onNext------->" + s + "---->"+ Thread.currentThread().getName());
            }
        };


        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("haha");
                subscriber.onNext("hello");
                subscriber.onNext("hehe");
                subscriber.onCompleted();
                Log.e(TAG,"OnSubscribe---call---->" + Thread.currentThread().getName());
            }
        });


        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

    }
}
