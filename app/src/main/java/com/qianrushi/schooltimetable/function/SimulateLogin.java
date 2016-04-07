package com.qianrushi.schooltimetable.function;

/**
 * Created by lwx on 2016/4/3.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianrushi.schooltimetable.activity.SimulateLoginAcitivity;
import com.qianrushi.schooltimetable.event.GradeHtmlEvent;
import com.qianrushi.schooltimetable.event.TestHtmlEvent;
import com.qianrushi.schooltimetable.event.TestParseEvent;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SimulateLogin {

    String sid, reurl, se, version;
    String username;
    String password;
    String captcha;
    HttpURLConnection connection;
    String courseHtml, gradeHtml, testHtml;
    static boolean login;
    char errorCode='0';
    Bitmap bitmap;
    static Activity callback;
    static ImageView iv;

    private static SimulateLogin simulateLogin;
    private SimulateLogin(){
        init(iv);
    }
    public static SimulateLogin getInstance(){
        if(simulateLogin==null){
            throw new IllegalStateException("Call getInstacn(Object o) to init this class.");
        }
        return simulateLogin;
    }
    public static SimulateLogin getInstance(Activity callback, ImageView iv){
        if(simulateLogin==null){
            SimulateLogin.callback = callback;
            SimulateLogin.iv = iv;
            simulateLogin = new SimulateLogin();
        }
        return simulateLogin;
    }

    public void login(String username, String password, String captcha, final TextView tv, final SimulateLoginAcitivity callback){
        this.username = username;
        this.password = password;
        this.captcha = captcha;
        Observable<String> observable = getSubimitObservable();
        observable.flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return get(s);
            }})
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return getCourse();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        tv.setText(s);
                        callback.parseCourseList(s);
                    }
                });
        //uLogin();
    }
    public static boolean hasLogin(){
        return login;
    }
    public int getErrorCode(){
        return errorCode-'0';
    }
    public Bitmap getBitmap(){
        if(bitmap==null){
            throw new IllegalStateException("Init method should call before getBitmap.");
        }
        return bitmap;
    }
    public void init(final ImageView iv) {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        Observable<String> observable = getInitObservable();
        observable.flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return loginAspx();
            }
        })
                .flatMap(new Func1<String, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(String s) {
                        return captcha();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        iv.setImageBitmap(bitmap);
                    }
                });
    }
    public Observable<Bitmap> refreshCaptcha(){
        return captcha();
    }
    public Observable<String> getInitObservable(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    String s = getViewState();
                    subscriber.onNext(s);
                    subscriber.onCompleted();
                } catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
    public Observable<String> getSubimitObservable(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    String s = uLogin();
                    subscriber.onNext(s);
                    subscriber.onCompleted();
                } catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
    private String getViewState() throws Exception{
        URL url = new URL("http://electsys.sjtu.edu.cn/edu/index.aspx");
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        String result = response.toString();
        Log.e("getViewState", result);
        for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            Log.e("header", header.getKey() + "=" + header.getValue());
        }
        return result;
    }
    private Observable<String> loginAspx(){
        try {
            String url2="";
            URL url = new URL("http://electsys.sjtu.edu.cn/edu/login.aspx");
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                Log.e(header.getKey(), header.getValue() + "");
                if(header.getKey()!=null&&header.getKey().equals("Location")){
                    url2 = header.getValue().toString().substring(1, header.getValue().toString().length()-1);
                }
            }

            url = new URL(url2);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                Log.e("header2", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length() - 1));
            }

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            String result = response.toString();
            Log.e("ulogin", result);
            Document doc = Jsoup.parse(result);
            Elements elements = doc.getElementsByAttributeValue("type", "hidden");
            sid = elements.get(0).attr("value");
            reurl = elements.get(1).attr("value");
            Log.e("reurl", reurl);
            se = elements.get(2).attr("value");
            version = elements.get(3).attr("value");
            captcha();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just(reurl);
    }
    private Observable<Bitmap> captcha(){
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    URL url = new URL("https://jaccount.sjtu.edu.cn/jaccount/captcha");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private String uLogin() throws Exception{
        URL url = new URL("https://jaccount.sjtu.edu.cn/jaccount/ulogin");
        connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        connection.setDoOutput(true);
        // 表单参数与get形式一样
        StringBuilder params = new StringBuilder();
        params.append("captcha").append("=").append(captcha).append("&")
                .append("imageField.x").append("=").append(31).append("&")
                .append("imageField.y").append("=").append(7).append("&")
                .append("pass").append("=").append(password).append("&")
                .append("returl").append("=").append(reurl).append("&")
                .append("se").append("=").append(se).append("&")
                .append("sid").append("=").append(sid).append("&")
                .append("user").append("=").append(username).append("&")
                .append("v").append("=").append("null").append("&");
        byte[] bypes = params.toString().getBytes();
        connection.getOutputStream().write(bypes);// 输入参数
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(in, "GBK"));
        StringBuffer response = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        String result = response.toString();
        Log.e("ulogin", result);
        int start = result.indexOf("('") + 2;
        final int end = result.indexOf("')");
        String reurl = result.substring(start, end);
        int pos;
        //判断登录是否成功
        if((pos=reurl.indexOf("loginfail="))>0){
            captcha();//refresh captcha
            errorCode = reurl.charAt(pos+"loginfail=".length());
        } else {
            //无错误码，登陆成功，执行后续操作
            char errorCode='0';
            //get(reurl);
        }
        return  reurl;
    }
    private Observable<String> get(final String path){
        try {
            URL url = new URL(path);
            String path2="";
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                if(header.getKey()!=null&&header.getKey().equals("Location")){
                    path2 = header.getValue().toString().substring(1, header.getValue().toString().length()-1);
                }
                Log.e("get", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length()-1));
            }

            url = new URL(path2);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                Log.e("get", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length()-1));
            }

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in, "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            String result = response.toString();
            Log.e("get", result);
            //成功转换为登录状态，执行后续操作
            login = true;
            return Observable.just(result);
            //getCourse();
            //获取课程，成绩，考试信息
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just("null");
    }
    private Observable<String> getCourse(){
        try {
            URL url = new URL("http://electsys.sjtu.edu.cn/edu/newsboard/newsinside.aspx");
            String path2="";
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                if(header.getKey()!=null&&header.getKey().equals("Location")){
                    path2 = header.getValue().toString().substring(1, header.getValue().toString().length()-1);
                }
                Log.e("get", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length()-1));
            }

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in, "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            final String result = response.toString();
            Log.e("get", result);
            courseHtml = result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just(courseHtml);
    }
    String EVENTVALIDATION="";
    String VIEWSTATE="";
    String VIEWSTATEGENERATOR="";
    private Observable<String> getGradeObservable(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    String s = initSearchGradeInfo();
                    subscriber.onNext(s);
                    subscriber.onCompleted();
                } catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });
    }
    private Observable<String> getTestObserver(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try{
                    String s = initSearchTestInfo();
                    subscriber.onNext(s);
                    subscriber.onCompleted();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void getGrade(){
        Observable<String> observable = getGradeObservable();
        observable.flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return searchGradeInfo();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        EventBus.getDefault().post(new GradeHtmlEvent(s));
                    }
                });
    }
    public void getTest(){
        Observable<String> observable = getTestObserver();
        observable.flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return searchTestInfo();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                EventBus.getDefault().post(new TestHtmlEvent(s));
            }
        });
    }
    private Observable<String> searchGradeInfo(){
        try {
            URL url = new URL("http://electsys.sjtu.edu.cn/edu/StudentScore/B_StudentScoreQuery.aspx");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            // 表单参数与get形式一样
            StringBuilder params = new StringBuilder();
            params.append("__EVENTVALIDATION").append("=").append(EVENTVALIDATION).append("&")
                    .append("__VIEWSTATE").append("=").append(VIEWSTATE).append("&")
                    .append("__VIEWSTATEGENERATOR").append("=").append(VIEWSTATEGENERATOR).append("&")
                    .append("btnSearch").append("=").append("++%E6%9F%A5++%E8%AF%A2++").append("&")
                    .append("ddlXN").append("=").append("2015-2016").append("&")
                    .append("ddlXQ").append("=").append("1").append("&")
                    .append("txtKCDM").append("=").append("");
            Log.e("params", params.toString());
            byte[] bypes = params.toString().getBytes();
            connection.getOutputStream().write(bypes);// 输入参数
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in, "utf-8"));
            final StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            final String result = response.toString();
            Log.e("searchGradeInfo", result);
            gradeHtml = result;
            //callback.onResult(gradeHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just(gradeHtml);
    }
    private String initSearchGradeInfo() throws Exception{
        URL url = new URL("http://electsys.sjtu.edu.cn/edu/StudentScore/B_StudentScoreQuery.aspx");
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(in, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        final String result = response.toString();
        Document doc = Jsoup.parse(result);
        Elements elements = doc.getElementsByAttributeValue("type", "hidden");
        VIEWSTATE = URLEncoder.encode(elements.get(0).attr("value"), "utf-8");
        Log.e("0", VIEWSTATE);
        VIEWSTATEGENERATOR = URLEncoder.encode(elements.get(1).attr("value"), "utf-8");
        Log.e("1", VIEWSTATEGENERATOR);
        EVENTVALIDATION = URLEncoder.encode(elements.get(2).attr("value"), "utf-8");
        Log.e("2", EVENTVALIDATION);
        Log.e("initGradeSearchInfo", result);
        //searchGradeInfo(callback);
        return result;
    }
    private Observable<String> searchTestInfo(){
        try {
            URL url = new URL("http://electsys.sjtu.edu.cn/edu/student/examArrange/examArrange.aspx");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);
            // 表单参数与get形式一样
            StringBuilder params = new StringBuilder();
            params.append("__EVENTVALIDATION").append("=").append(EVENTVALIDATION).append("&")
                    .append("__VIEWSTATE").append("=").append(VIEWSTATE).append("&")
                    .append("__VIEWSTATEGENERATOR").append("=").append(VIEWSTATEGENERATOR).append("&")
                    .append("btnQuery").append("=").append("%E6%9F%A5+%E8%AF%A2").append("&")
                    .append("dpXn").append("=").append("2015-2016").append("&")
                    .append("dpXq").append("=").append("1");
            Log.e("params", params.toString());
            byte[] bypes = params.toString().getBytes();
            connection.getOutputStream().write(bypes);// 输入参数
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(in, "utf-8"));
            final StringBuffer response = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            final String result = response.toString();
            Log.e("searchTestInfo", result);
            testHtml = result;
            //callback.onResult(testHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Observable.just(testHtml);
    }
    private String initSearchTestInfo() throws Exception{
        URL url = new URL("http://electsys.sjtu.edu.cn/edu/student/examArrange/examArrange.aspx");
        connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(in, "utf-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        final String result = response.toString();
        Document doc = Jsoup.parse(result);
        Elements elements = doc.getElementsByAttributeValue("type", "hidden");
        VIEWSTATE = URLEncoder.encode(elements.get(0).attr("value"), "utf-8");
        Log.e("0", VIEWSTATE);
        VIEWSTATEGENERATOR = URLEncoder.encode(elements.get(1).attr("value"), "utf-8");
        Log.e("1", VIEWSTATEGENERATOR);
        EVENTVALIDATION = URLEncoder.encode(elements.get(2).attr("value"), "utf-8");
        Log.e("2", EVENTVALIDATION);
        Log.e("initSearchTestInfo", result);
        //searchTestInfo(callback);
        return result;
    }
}