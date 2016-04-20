package com.qianrushi.schooltimetable.model;

import java.io.Serializable;

/**
 * Created by lwx on 2016/3/20.
 */
public class CourseInfo implements Serializable, ICourseInfo{

    private String name;
    private int startWeek, endWeek;
    private String location;
    private int startNum, endNum;
    private int day;

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", startWeek=" + startWeek +
                ", endWeek=" + endWeek +
                ", location='" + location + '\'' +
                ", startNum=" + startNum +
                ", endNum=" + endNum +
                ", day=" + day +
                '}';
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public int getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public int getEndWeek() {
        return endWeek;
    }

    public String getLocation() {
        return location;
    }

    public int getStartNum() {
        return startNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public static class Builder{
        private String name;
        private int startWeek, endWeek;
        private String location;
        private int startNum, endNum;
        private int day;

        public Builder(){}

        public Builder name(String val){
            name = val;
            return this;
        }
        public Builder startWeek(int val){
            startWeek = val;
            return this;
        }
        public Builder endWeek(int val){
            endWeek = val;
            return this;
        }
        public Builder location(String val){
            location = val;
            return this;
        }
        public Builder starNum(int val){
            startNum = val;
            return this;
        }
        public Builder endNum(int val){
            endNum = val;
            return this;
        }
        public Builder day(int val){
            day = val;
            return this;
        }

        public CourseInfo build(){
            return new CourseInfo(this);
        }
    }

    private CourseInfo(Builder builder){
        name = builder.name;
        startWeek = builder.startWeek;
        endWeek = builder.endWeek;
        location = builder.location;
        startNum = builder.startNum;
        endNum = builder.endNum;
        day = builder.day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseInfo)) return false;

        CourseInfo course = (CourseInfo) o;

        if (startWeek != course.startWeek) return false;
        if (endWeek != course.endWeek) return false;
        if (!name.equals(course.name)) return false;
        if (!location.equals(course.location)) return false;
        if (startNum != course.startNum) return false;
        if (day != course.day) return false;
        return endNum == course.endNum;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + startWeek;
        result = 31 * result + endWeek;
        result = 31 * result + location.hashCode();
        result = 31 * result + startNum;
        result = 31 * result + endNum;
        result = 31 * result + day;
        return result;
    }
}