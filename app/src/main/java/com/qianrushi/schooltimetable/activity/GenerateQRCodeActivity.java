package com.qianrushi.schooltimetable.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.function.zxing.BitmapUtil;
import com.qianrushi.schooltimetable.model.CourseList;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.network.Transfer;

/**
 * Created by lwx on 2016/4/4.
 */
public class GenerateQRCodeActivity extends BaseActivity{
    protected int mScreenWidth ;
    public String url = "http://202.120.36.190:8088/";
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.generate_qrcode);
        uploadCourseInfo();
        init();
        //postCourseInfo();
        generateQRCode();
    }
    public void init(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }
    public void generateQRCode(){
        String uri = url;
        ImageView iv = (ImageView) findViewById(R.id.qrcode);
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
    private void uploadCourseInfo(){
        Gson gson = new Gson();
        CourseList list = new CourseList(MyCourseinfo.getInstace());
        String st = gson.toJson(list);
        Transfer.getInstance(url).uploadCourseInfo(st);
    }
}
