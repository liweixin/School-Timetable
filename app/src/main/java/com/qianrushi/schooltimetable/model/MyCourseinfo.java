package com.qianrushi.schooltimetable.model;

import android.util.Log;

import java.util.List;

/**
 * Created by lwx on 2016/4/1.
 */
public class MyCourseinfo {
    private static List<CourseInfo> courseInfoList;
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
}
