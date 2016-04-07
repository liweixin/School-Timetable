package com.qianrushi.schooltimetable.function;

import android.util.Log;

import com.qianrushi.schooltimetable.event.GradeHtmlEvent;
import com.qianrushi.schooltimetable.event.GradeParseEvent;
import com.qianrushi.schooltimetable.model.GradeInfo;
import com.qianrushi.schooltimetable.model.MyGradeInfoList;
import com.qianrushi.schooltimetable.viewpager.fragment.Two.GradeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by lwx on 2016/4/3.
 */
public class ParseGradeHtml {
    private static ParseGradeHtml parseGradeHtml;
    String html;
    EventBus eventBus;
    private ParseGradeHtml(){ eventBus = EventBus.getDefault(); eventBus.register(this);}
    public static ParseGradeHtml getInstance(){
        if(parseGradeHtml==null){
            parseGradeHtml = new ParseGradeHtml();
        }
        return parseGradeHtml;
    }
    @Subscribe (threadMode = ThreadMode.BACKGROUND)
    public void onEvent(GradeHtmlEvent event){
        html = event.getGradeHtml();
        parse(html);
    }
    public void parse(String html){
        Log.e("grade", html);
        final Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByAttributeValue("id", "dgScore");
        if(elements==null||elements.size()==0||elements.size()>2) throw new IllegalStateException("id dgScore error");
        Elements gradeList = elements.get(0).getElementsByTag("tr");
        gradeList.remove(0);
        List<GradeInfo> list = MyGradeInfoList.getInstance().getList();
        list.clear();
        for(Element course:gradeList){
            Elements infos = course.getElementsByTag("td");
            list.add(new GradeInfo(infos.get(0).text(),
                    infos.get(1).text(),
                    Double.parseDouble(infos.get(2).text()),
                    Double.parseDouble(infos.get(3).text()),
                    infos.get(4).text(),
                    infos.get(5).text(),
                    Boolean.parseBoolean(infos.get(6).text())));
        }
        Log.e("size", gradeList.size() + "");
        EventBus.getDefault().post(new GradeParseEvent());
    }
}
