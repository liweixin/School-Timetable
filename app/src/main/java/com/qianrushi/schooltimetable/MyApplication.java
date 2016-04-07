package com.qianrushi.schooltimetable;

import android.app.Application;

import com.qianrushi.schooltimetable.function.ParseGradeHtml;
import com.qianrushi.schooltimetable.function.ParseTestHtml;
import com.qianrushi.schooltimetable.model.EncodeAndDecode;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;

/**
 * Created by lwx on 2016/4/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Util.getInstance().init(getApplicationContext());
        //从sharedpreference读入数据
        MyCourseinfo.setCourseInfo(EncodeAndDecode.readProduct());
        //注册监听事件
        ParseGradeHtml.getInstance();
        ParseTestHtml.getInstance();
    }
}
