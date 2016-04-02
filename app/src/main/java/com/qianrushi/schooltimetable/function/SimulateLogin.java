package com.qianrushi.schooltimetable.function;

/**
 * Created by lwx on 2016/4/3.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.qianrushi.schooltimetable.activity.SimulateLoginAcitivity;

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

public class SimulateLogin {

    String sid, reurl, se, version;
    String username;
    String password;
    String captcha;
    HttpURLConnection connection;
    String courseHtml, gradeHtml, testHtml;
    boolean login;
    char errorCode='0';
    Bitmap bitmap;
    static Activity callback;

    private static SimulateLogin simulateLogin;
    private SimulateLogin(){
        init();
    }
    public static SimulateLogin getInstance(){
        if(simulateLogin==null){
            throw new IllegalStateException("Call getInstacn(Object o) to init this class.");
        }
        return simulateLogin;
    }
    public static SimulateLogin getInstance(Activity callback){
        if(simulateLogin==null){
            SimulateLogin.callback = callback;
            simulateLogin = new SimulateLogin();
        }
        return simulateLogin;
    }

    public String getCourseHtml(){
        if(!login) throw new IllegalStateException("请求数据前必须登录");
        if(courseHtml==null){
            getCourse();
        }
        return courseHtml;
    }
    public String getGradeHtml(){
        if(!login) throw new IllegalStateException("请求数据前必须登录");
        if(gradeHtml==null){
            initSearchGradeInfo();
        }
        return gradeHtml;
    }
    public String getTestHtml(){
        if(!login) throw new IllegalStateException("请求数据前必须登录");
        if (testHtml==null) {
            initSearchTestInfo();
        }
        return testHtml;
    }
    public void login(String username, String password, String captcha){
        this.username = username;
        this.password = password;
        this.captcha = captcha;
        uLogin();
    }
    public boolean hasLogin(){
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
    public void init() {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        getViewState();
    }
    public void refreshCaptcha(){
        captcha();
    }

    private void getViewState(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //HttpURLConnection connection = null;
                try {
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
                    loginAspx();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void loginAspx(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //HttpURLConnection connection = null;
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
            }
        }).start();
    }
    private void captcha(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("https://jaccount.sjtu.edu.cn/jaccount/captcha");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                ((SimulateLoginAcitivity)callback).getCaptcha();
            }
        }).start();
    }
    private void uLogin(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
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
                        get(reurl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void get(final String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    String path2="";
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                        if(header.getKey()!=null&&header.getKey().equals("Location")){
                            path2 = header.getValue().toString().substring(1, header.getValue().toString().length()-1);
                        }
                        Log.e("get", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length() - 1));
                    }

                    url = new URL(path2);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                        Log.e("get", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length() - 1));
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
                    //getCourse();
                    //获取课程，成绩，考试信息
                    ((SimulateLoginAcitivity) callback).onLoginFinished();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getCourse(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://electsys.sjtu.edu.cn/edu/newsboard/newsinside.aspx");
                    String path2="";
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    for (Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
                        if(header.getKey()!=null&&header.getKey().equals("Location")){
                            path2 = header.getValue().toString().substring(1, header.getValue().toString().length()-1);
                        }
                        Log.e("get", header.getKey() + "=" + header.getValue().toString().substring(1, header.getValue().toString().length() - 1));
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
                    ((SimulateLoginAcitivity)callback).parseCourseList(courseHtml);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    String EVENTVALIDATION="";
    String VIEWSTATE="";
    String VIEWSTATEGENERATOR="";
    private void searchGradeInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initSearchGradeInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    searchGradeInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void searchTestInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void initSearchTestInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                    searchTestInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}