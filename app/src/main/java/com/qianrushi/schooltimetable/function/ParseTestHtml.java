package com.qianrushi.schooltimetable.function;

import android.util.Log;

import com.qianrushi.schooltimetable.event.TestHtmlEvent;
import com.qianrushi.schooltimetable.event.TestParseEvent;
import com.qianrushi.schooltimetable.model.GradeInfo;
import com.qianrushi.schooltimetable.model.MyGradeInfoList;
import com.qianrushi.schooltimetable.model.MyTestInfoList;
import com.qianrushi.schooltimetable.model.TestInfo;
import com.qianrushi.schooltimetable.viewpager.fragment.Three.TestFragment;
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
 * Created by lwx on 2016/4/4.
 */
public class ParseTestHtml {
    private static ParseTestHtml parseTestHtml;
    String html;
    EventBus eventBus;
    private ParseTestHtml(){ eventBus = EventBus.getDefault(); eventBus.register(this);}
    public static ParseTestHtml getInstance(){
        if(parseTestHtml==null){
            parseTestHtml = new ParseTestHtml();
        }
        return parseTestHtml;
    }
    @Subscribe (threadMode = ThreadMode.BACKGROUND)
    public void onEvent(TestHtmlEvent event){
        parse(event.getGradeHtml());
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
        EventBus.getDefault().post(new TestParseEvent());
    }
}
