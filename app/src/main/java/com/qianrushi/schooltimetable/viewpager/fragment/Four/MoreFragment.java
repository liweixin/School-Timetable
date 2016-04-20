package com.qianrushi.schooltimetable.viewpager.fragment.Four;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.activity.GenerateQRCodeActivity;
import com.qianrushi.schooltimetable.activity.SimulateLoginAcitivity;
import com.qianrushi.schooltimetable.function.SimulateLogin;
import com.qianrushi.schooltimetable.function.zxing.BitmapUtil;
import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.network.Transfer;
import com.qianrushi.schooltimetable.utils.Util;
import com.zxing.activity.CaptureActivity;

/**
 * Created by lwx on 2016/4/3.
 */
public class MoreFragment extends Fragment {
    View rootView;
    Button setting, scanQRCode, generateQRCode, logout, login;
    public static final int SCAN_QRCODE = 100;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.fragment_more, container, false);
        setting = (Button) rootView.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        scanQRCode = (Button) rootView.findViewById(R.id.scan_qrcode);
        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(getActivity(),
                        CaptureActivity.class);
                getActivity().startActivityForResult(openCameraIntent, SCAN_QRCODE);
            }
        });
        generateQRCode = (Button) rootView.findViewById(R.id.generate_qrcode);
        generateQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), GenerateQRCodeActivity.class));
            }
        });
        logout = (Button) rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimulateLogin.logout();
                Util.getInstance().toast("注销成功");
            }
        });
        login = (Button) rootView.findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SimulateLogin.hasLogin()){
                    Util.getInstance().toast("你已经登录过了0.0");
                } else {
                    startActivity(new Intent(getContext(), SimulateLoginAcitivity.class));
                }
            }
        });
        return rootView;
    }
    public void onScanResult(int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Transfer.getInstance(scanResult).downloadCourseInfo();
        }
    }
}
