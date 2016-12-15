package com.livestar.rxjavademo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "rxjava";
    private static final int CODE_FOR_OPEN_CAMERA = 100;
    private ImageView iv;
    private TextView tv,flatTV;
    StringBuilder stringBuilder = new StringBuilder("children:"),sb = new StringBuilder("children:");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        iv = (ImageView) findViewById(R.id.iv);
        tv = (TextView) findViewById(R.id.tv);
        flatTV = (TextView) findViewById(R.id.tv_flat_map);

        ArrayList<People> peoples = new ArrayList<>();


        People people = new People("lily", 27, false, new String[]{"haha", "vivi", "baby", "rabby"});


        Observable.just(people,people,people).flatMap(new Func1<People, Observable<String[]>>() {
            @Override
            public Observable<String[]> call(People people) {

                return Observable.just(people.getChildren());
            }
        }).subscribe(new Action1<String[]>() {
            @Override
            public void call(String[] strings) {
                Observable.from(strings).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        sb.append(s);

                        flatTV.setText(sb);
                    }
                });
            }
        });



        rxjavaMap(people);


        Log.e(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPerssion();

        } else {
            rxjaveMap();
        }


    }

    private void rxjavaMap(People people) {
        Observable.just(people).map(new Func1<People, String[]>() {
            @Override
            public String[] call(People people) {

                return people.getChildren();
            }
        }).subscribe(new Action1<String[]>() {
            @Override
            public void call(String[] strings) {

                for (String s : strings) {
                    stringBuilder.append(" " + s);
                }

                tv.setText(stringBuilder);
                Log.e(TAG, "Action1----" + "call" + "---->" + stringBuilder + Thread.currentThread().getName());
            }
        });
    }


    /**
     * 简单变换
     */
    private void rxjaveMap() {
        Observable.just("/mnt/sdcard/a.jpg").map(new Func1<String, Bitmap>() {
            @Override
            public Bitmap call(String filePath) {
                Log.e(TAG, "map----" + "call" + "---->" + Thread.currentThread().getName());

                return createBitmap(filePath);
            }
        }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPerssion() {
        int hasPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) | checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    CODE_FOR_OPEN_CAMERA);
        } else {
            rxjaveMap();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_FOR_OPEN_CAMERA) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                rxjaveMap();
            } else {
                Toast.makeText(getApplication(), "没有权限", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Bitmap createBitmap(String filePath) {
        Log.e(TAG, "map----" + "createBitmap" + "---->" + Thread.currentThread().getName());
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

    private void rxjavaDemo1() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "observer----" + "onCompleted" + "---->" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "observer----" + "onError" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "observer----" + "onNext------->" + s + "---->" + Thread.currentThread().getName());
            }
        };


        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("haha");
                subscriber.onNext("hello");
                subscriber.onNext("hehe");
                subscriber.onCompleted();
                Log.e(TAG, "OnSubscribe---call---->" + Thread.currentThread().getName());
            }
        });


        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }
}
