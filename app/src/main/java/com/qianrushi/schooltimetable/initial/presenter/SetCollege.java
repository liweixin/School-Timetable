package com.qianrushi.schooltimetable.initial.presenter;

import com.qianrushi.schooltimetable.model.UserInfo;

/**
 * Created by lwx on 2016/3/27.
 */
public class SetCollege implements ISetCollege {
    @Override
    public void setCollege(String s) {
        UserInfo.getInstance().setCollege(s);
    }
}
