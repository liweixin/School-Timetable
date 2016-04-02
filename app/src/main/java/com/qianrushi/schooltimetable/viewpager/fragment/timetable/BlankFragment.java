package com.qianrushi.schooltimetable.viewpager.fragment.timetable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qianrushi.schooltimetable.R;

/**
 * Created by lwx on 2016/3/28.
 */
public class BlankFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View mView = inflater.inflate(R.layout.fragment_blank, container, false);
        return mView;
    }
}
