package com.qianrushi.schooltimetable.event;

/**
 * Created by lwx on 2016/4/7.
 */
public class GradeHtmlEvent {
    private String gradeHtml;
    public GradeHtmlEvent(String s){
        gradeHtml = s;
    }

    public String getGradeHtml() {
        return gradeHtml;
    }

    public void setGradeHtml(String gradeHtml) {
        this.gradeHtml = gradeHtml;
    }
}
