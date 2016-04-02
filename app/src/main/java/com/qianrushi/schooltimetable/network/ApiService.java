package com.qianrushi.schooltimetable.network;

import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.model.MyCourseinfo;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public interface ApiService {
    @GET("courselist")
    Observable<CourseList> get();
    @FormUrlEncoded
    @POST("courselist")
    Observable<String> post(@Field("courseList") String st);
}