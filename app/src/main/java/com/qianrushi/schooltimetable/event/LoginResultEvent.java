package com.qianrushi.schooltimetable.event;

/**
 * Created by lwx on 2016/4/20.
 */
public class LoginResultEvent {
    int errorCode;
    public LoginResultEvent(int i){
        errorCode = i;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
