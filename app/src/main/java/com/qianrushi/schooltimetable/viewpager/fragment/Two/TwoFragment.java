package com.qianrushi.schooltimetable.viewpager.fragment.Two;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.function.zxing.BitmapUtil;
import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.network.Transfer;

import java.io.Serializable;

/**
 * Created by lwx on 2016/3/24.
 */
public class TwoFragment extends Fragment {
    View rootView;
    protected int mScreenWidth ;
    public String url = "http://202.120.36.190:8088/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        rootView = inflater.inflate(R.layout.layout2, container, false);
        Button generate = (Button) rootView.findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                postCourseInfo();
                generateQRCode();
            }
        });
        uploadCourseInfo();
        return rootView;
    }

    private void uploadCourseInfo(){
        Gson gson = new Gson();
        CourseList list = new CourseList(MyCourseinfo.getInstace());
        String st = gson.toJson(list);
        Transfer.getInstance(url).uploadCourseInfo(st);
    }

    public void postCourseInfo(){

    }
    public void init(){
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }
    public void generateQRCode(){
        String uri = url;
        ImageView iv = (ImageView) rootView.findViewById(R.id.qrcode);
//		Bitmap bitmap = BitmapUtil.create2DCoderBitmap(uri, mScreenWidth/2, mScreenWidth/2);
        Bitmap bitmap;
        try {
            bitmap = BitmapUtil.createQRCode(uri, mScreenWidth);

            if(bitmap != null){
                iv.setImageBitmap(bitmap);
            }

        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
