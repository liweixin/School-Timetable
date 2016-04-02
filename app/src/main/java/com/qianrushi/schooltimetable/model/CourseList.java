package com.qianrushi.schooltimetable.model;

import java.util.List;

/**
 * Created by lwx on 2016/4/1.
 */
public class CourseList {
    List<CourseInfo> courseInfoList;
    public CourseList(List<CourseInfo> list){
        courseInfoList = list;
    }
    public List<CourseInfo> getCourseInfoList(){
        return courseInfoList;
    }
    public void setCourseInfoList(List<CourseInfo> o){
        courseInfoList = o;
    }
}
