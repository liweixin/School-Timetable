package com.qianrushi.schooltimetable.event;

/**
 * Created by lwx on 2016/4/18.
 */
public class RefreshTimeTableEvent {
    boolean refresh;
    public RefreshTimeTableEvent(boolean b){
        refresh = b;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}
