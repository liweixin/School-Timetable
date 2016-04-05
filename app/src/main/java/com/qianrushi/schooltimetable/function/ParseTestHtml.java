package com.qianrushi.schooltimetable.function;

import android.util.Log;

import com.qianrushi.schooltimetable.model.GradeInfo;
import com.qianrushi.schooltimetable.model.MyGradeInfoList;
import com.qianrushi.schooltimetable.model.MyTestInfoList;
import com.qianrushi.schooltimetable.model.TestInfo;
import com.qianrushi.schooltimetable.viewpager.fragment.Three.TestFragment;
import com.qianrushi.schooltimetable.viewpager.fragment.Two.GradeFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by lwx on 2016/4/4.
 */
public class ParseTestHtml {
    private static ParseTestHtml parseTestHtml;
    String html;
    TestFragment callback;
    private ParseTestHtml(){}
    public static ParseTestHtml getInstance(){
        if(parseTestHtml==null){
            parseTestHtml = new ParseTestHtml();
        }
        return parseTestHtml;
    }
    public void init(TestFragment callback){
        this.callback = callback;
        //html = SimulateLogin.getInstance().getTestHtml(this);
    }
    public void onResult(String html){
        this.html = html;
        parse(html);
        Log.e("html", html);
    }
    public void parse(String html){
        final Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByAttributeValue("id", "gridMain");
        if(elements==null||elements.size()==0||elements.size()>2) throw new IllegalStateException("id dgScore error");
        Elements testList = elements.get(0).getElementsByTag("tr");
        testList.remove(0);
        List<TestInfo> list = MyTestInfoList.getInstance().getList();
        list.clear();
        for(Element test:testList){
            Elements infos = test.getElementsByTag("td");
            list.add(new TestInfo(infos.get(0).text(),
                    infos.get(1).text(),
                    infos.get(2).text(),
                    infos.get(3).text(),
                    infos.get(4).text()));
        }
        Log.e("size", testList.size() + "");
        callback.notifyDataSetChanged();
    }
}
