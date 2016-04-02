package com.qianrushi.schooltimetable.initial.presenter;

import com.qianrushi.schooltimetable.model.UserInfo;

/**
 * Created by lwx on 2016/3/27.
 */
public class SetAdmissionTimeAndEducation implements ISetAdmissionTimeAndEducation {
    @Override
    public void setAdmissionTime(String s) {
        UserInfo.getInstance().setAdmissionTime(s);
    }

    @Override
    public void setEducation(String s) {
        UserInfo.getInstance().setEducation(s);
    }
}
