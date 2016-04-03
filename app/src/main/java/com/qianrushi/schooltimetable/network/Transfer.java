package com.qianrushi.schooltimetable.network;

import android.util.Log;

import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.model.MyCourseinfo;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lwx on 2016/4/1.
 */
public class Transfer {
    private static Transfer transfer;
    String baseUrl;
    ApiService service;
    boolean init;
    public void downloadCourseInfo(){
        init();
        Observable<CourseList> observable = service.get();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CourseList>() {
                    @Override
                    public void onCompleted() {
                        Log.e("retrofit1", "completed.");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("retrofit1", "error");
                    }

                    @Override
                    public void onNext(CourseList list) {
                        Log.e("hint", "download success.");
                        Log.e("songList", list.getCourseInfoList().get(0).toString());
                        MyCourseinfo.setCourseInfo(list.getCourseInfoList());
                    }
                });
    }
    public void uploadCourseInfo(String st){
        init();
        Observable<String> observable = service.post(st);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("retrofit", "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("retrofit", "error");
                    }

                    @Override
                    public void onNext(String result) {
                        Log.e("hint", "transfer sucess");
                        Log.e("result", result);
                    }
                });
    }
    private void init(){
        if(init) return;
        init = true;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(ApiService.class);
    }
    private Transfer(String baseUrl){
        this.baseUrl = baseUrl;
    }
    public static Transfer getInstance(String baseUrl){
        if(transfer ==null){
            transfer = new Transfer(baseUrl);
        }
        return transfer;
    }
    public static Transfer getInstance(){
        if(transfer ==null){
            throw new NullPointerException("Base url has not been init.");
        }
        return transfer;
    }
}
