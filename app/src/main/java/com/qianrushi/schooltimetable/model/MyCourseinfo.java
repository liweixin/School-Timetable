package com.qianrushi.schooltimetable.model;

import android.util.Log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lwx on 2016/4/1.
 */
public class MyCourseinfo implements Serializable{
    private static List<CourseInfo> courseInfoList;
    private static boolean shown = false;
    private MyCourseinfo(){};
    public static List<CourseInfo> getInstace(){
        if(courseInfoList==null){
            //search in datebase.
        }
        return courseInfoList;
    }
    public static void setCourseInfo(List<CourseInfo> courseInfoList){
        if(courseInfoList!=null){
            Log.e("attention", "MyCourseInfo has init before.");
        }
        MyCourseinfo.courseInfoList = courseInfoList;
    }

    public static boolean isShown() {
        return shown;
    }

    public static void setShown(boolean shown) {
        MyCourseinfo.shown = shown;
    }
}
