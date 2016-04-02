package com.qianrushi.schooltimetable.viewpager.fragment.Three;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.network.Transfer;
import com.zxing.activity.CaptureActivity;

import java.util.List;

/**
 * Created by lwx on 2016/3/24.
 */
public class ThreeFragment extends Fragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.layout3, container, false);
        Button scan = (Button) rootView.findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getActivity(),
                        CaptureActivity.class);
                getActivity().startActivityForResult(openCameraIntent, 100);
            }
        });
        return rootView;
    }
    public void result(int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            TextView tv = (TextView) rootView.findViewById(R.id.result);
            tv.setText(scanResult);
            Transfer.getInstance(scanResult).downloadCourseInfo();

        }
    }
}
