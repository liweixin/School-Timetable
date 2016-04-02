package com.qianrushi.schooltimetable.viewpager.fragment.timetable;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.function.AnalyseContent;
import com.qianrushi.schooltimetable.model.CourseInfo;
import com.qianrushi.schooltimetable.model.MyCourseinfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    List<CourseInfo> courseList = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("WebView", "onPageStarted");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "onPageFinished ");
                Log.d("PageURL", url);
                if (url.equals("http://electsys.sjtu.edu.cn/edu/student/sdtMain.aspx")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("http://electsys.sjtu.edu.cn/edu/newsboard/newsinside.aspx");
                        }
                    });
                }
                if (url.equals("http://electsys.sjtu.edu.cn/edu/newsboard/newsinside.aspx")) {
                    view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                            "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl("http://electsys.sjtu.edu.cn/edu/");
    }
    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(final String html) {
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView) findViewById(R.id.text)).setText(courseList.toString());
                            }
                        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            /*Intent intent = new Intent(this, TimetableActivity.class);
            if(courseList==null){
                throw new NullPointerException("CourseList is null in WebViewActivity.");
            }
            intent.putExtra("courseList", (Serializable) courseList);
            startActivity(intent);*/
            Log.d("课程信息", courseList.get(0).getName()+"获取成功");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
