package com.qianrushi.schooltimetable.initial.presenter;

import com.qianrushi.schooltimetable.model.UserInfo;

/**
 * Created by lwx on 2016/3/27.
 */
public class SetSchool implements ISetSchool {
    @Override
    public void setSchool(String s) {
        UserInfo.getInstance().setSchool(s);
    }
}
