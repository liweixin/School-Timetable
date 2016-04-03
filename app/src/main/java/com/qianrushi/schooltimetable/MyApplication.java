package com.qianrushi.schooltimetable;

import android.app.Application;

import com.qianrushi.schooltimetable.utils.Util;

/**
 * Created by lwx on 2016/4/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Util.getInstance().init(getApplicationContext());
    }
}
