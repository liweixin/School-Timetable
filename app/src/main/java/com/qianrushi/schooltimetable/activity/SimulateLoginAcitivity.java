package com.qianrushi.schooltimetable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.function.AnalyseContent;
import com.qianrushi.schooltimetable.function.SimulateLogin;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.MyCourseinfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimulateLoginAcitivity extends AppCompatActivity {

    ImageView iv;
    EditText et, un, pw;
    Button ulogin, gradeInfo, testInfo, refresh;
    TextView tv;
    final SimulateLogin simulateLogin = SimulateLogin.getInstance(this);
    List<CourseInfo> courseList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simulate_login);
        iv = (ImageView) findViewById(R.id.iv);
        et = (EditText) findViewById(R.id.edit_text);
        un = (EditText) findViewById(R.id.username);
        pw = (EditText) findViewById(R.id.password);
        tv = (TextView) findViewById(R.id.text_view);
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLogin.refreshCaptcha();
            }
        });
        ulogin = (Button) findViewById(R.id.submit);
        ulogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLogin.login(un.getText().toString(), pw.getText().toString(), et.getText().toString());
            }
        });
        gradeInfo = (Button) findViewById(R.id.grade_info);
        gradeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLogin.getGradeHtml();
            }
        });
        testInfo = (Button) findViewById(R.id.test_info);
        testInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simulateLogin.getTestHtml();
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
    public void onLoginFinished(){
        simulateLogin.getCourseHtml();
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
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView) findViewById(R.id.text)).setText(courseList.toString());
                        }
                    });*/
                    //设置返回值并退出活动
                    Intent intent = new Intent();
                    intent.putExtra("courseList", (Serializable)courseList);
                    setResult(RESULT_OK, intent);
                    MyCourseinfo.setCourseInfo(courseList);
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
        AnalyseContent ac = new AnalyseContent(content);
        courseList.add(builder.location(ac.getLocation())
                .startWeek(ac.getStartWeek())
                .endWeek(ac.getEndWeek())
                .name(ac.getCourseName()).build());
    }
}
