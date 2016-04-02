package com.qianrushi.schooltimetable.initial.presenter;

import com.qianrushi.schooltimetable.model.UserInfo;

/**
 * Created by lwx on 2016/3/27.
 */
public class ConfirmInfo implements IConfirmInfo {
    @Override
    public UserInfo getUserInfo() {
        return UserInfo.getInstance();
    }
}
