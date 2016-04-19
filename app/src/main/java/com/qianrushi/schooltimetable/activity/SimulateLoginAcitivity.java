package com.qianrushi.schooltimetable.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.function.ParseCourseHtml;
import com.qianrushi.schooltimetable.function.SimulateLogin;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SimulateLoginAcitivity extends AppCompatActivity {

    ImageView iv;
    EditText et, un, pw;
    Button ulogin, refresh;
    TextView tv;
    SimulateLogin simulateLogin;
    List<CourseInfo> courseList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simulate_login);
        iv = (ImageView) findViewById(R.id.iv);
        et = (EditText) findViewById(R.id.captcha_edit);
        un = (EditText) findViewById(R.id.username_edit);
        pw = (EditText) findViewById(R.id.password_edit);
        simulateLogin = SimulateLogin.getInstance(this, iv);
        refresh = (Button) findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLogin.refreshCaptcha().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Bitmap>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                iv.setImageBitmap(bitmap);
                            }
                        });
                                //simulateLogin.refreshCaptcha();
            }
        });
        ulogin = (Button) findViewById(R.id.signin_button);
        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLogin.login(un.getText().toString(), pw.getText().toString(), et.getText().toString(), tv, SimulateLoginAcitivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "logining", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    public void getCaptcha(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                iv.setImageBitmap(simulateLogin.getBitmap());
            }
        });
    }
    public void parseCourseList(final String html){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Document doc = Jsoup.parse(html, "http://electsys.sjtu.edu.cn/edu/");
                    Log.d("html", doc.toString());
                    Elements elements = doc.getElementsByClass("alltab");
                    if(elements==null||elements.size()==0) return;
                    final Elements allcourse;
                    if(elements.size()==2){
                        allcourse = elements.last().getElementsByTag("tr");
                    } else if(elements.size()==1){
                        allcourse = elements.first().getElementsByTag("tr");
                    } else {
                        throw new IllegalArgumentException("Elements.size() is illegal.");
                    } //选择本学期的课表
                    allcourse.remove(allcourse.size()-1);
                    allcourse.remove(0);//删掉多余的第十四节课和表格中表示星期的第一行
                    boolean[][] hasCourse = new boolean[8][14];//星期1-7的第1-13节课是否有课
                    for(int j=1; j<=13; j++){
                        Element course = allcourse.get(j-1);
                        Elements list = course.getElementsByClass("classmain");
                        int i=1;
                        for(int k=0; k<list.size(); k++){
                            while(hasCourse[i][j]) i++;//i will never bigger than 7 if the input is legal
                            Element element = list.get(k);
                            Log.d("course", element.text());
                            int duration;
                            if(element.hasAttr("rowspan")){
                                duration = Integer.parseInt(element.attr("rowspan"));//课程节数
                                for(int l=0; l<duration; l++){
                                    hasCourse[i][j+l] = true;
                                }
                            } else { //第i周第j节没课 (￣∀￣)
                                i++; //别忘了也把星期加一！！
                                continue;
                            }
                            String content = element.text();
                            int num = getClassNum(content);
                            CourseInfo.Builder builder = new CourseInfo.Builder()
                                    .starNum(j)
                                    .endNum(j+duration-1)
                                    .day(i);
                            if(num==1){
                                addCourse(content, builder);
                            } else {
                                int p=0;
                                int index;
                                while((p<content.length())&&(index=content.indexOf("]", p))>=0){
                                    addCourse(content.substring(p, index+1), builder);
                                    p = index + 2;
                                }
                            }
                        }
                    }
                    //设置返回值并退出活动
                    Intent intent = new Intent();
                    intent.putExtra("courseList", (Serializable)courseList);
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public int getClassNum(String content){
        return content.split("]").length;
    }
    public void addCourse(String content, CourseInfo.Builder builder){
        ParseCourseHtml ac = new ParseCourseHtml(content);
        courseList.add(builder.location(ac.getLocation())
                .startWeek(ac.getStartWeek())
                .endWeek(ac.getEndWeek())
                .name(ac.getCourseName()).build());
    }
}
