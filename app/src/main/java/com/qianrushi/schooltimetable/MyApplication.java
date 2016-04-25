package com.qianrushi.schooltimetable;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;
import android.widget.Toast;

import com.qianrushi.schooltimetable.event.RefreshTimeTableEvent;
import com.qianrushi.schooltimetable.function.ParseGradeHtml;
import com.qianrushi.schooltimetable.function.ParseTestHtml;
import com.qianrushi.schooltimetable.model.EncodeAndDecode;
import com.qianrushi.schooltimetable.model.MyCourseinfo;
import com.qianrushi.schooltimetable.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * Created by lwx on 2016/4/4.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Util.getInstance().init(getApplicationContext());
        //从sharedpreference读入数据
        MyCourseinfo.setCourseInfo(EncodeAndDecode.readProduct());
        //注册监听事件
        ParseGradeHtml.getInstance();
        ParseTestHtml.getInstance();
        if(!getCertificateSHA1Fingerprint().equals("C5:D2:B4:6F:8C:BC:65:B4:C8:C2:F9:2E:F9:91:59:CD:BE:B2:E7:38")){
            Toast.makeText(getApplicationContext(), "检测到应用被重新打包，自动退出", Toast.LENGTH_LONG).show();
            //退出应用代码
        } else {
            Toast.makeText(getApplicationContext(), "签名校验通过", Toast.LENGTH_LONG).show();
        }
        Log.e("reuslt", getCertificateSHA1Fingerprint());
    }
    public void getSignInfo(){
        try{
            Signature[] sigs = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures;
            for (Signature sig : sigs)
            {
                Log.e("MyApp", "Signature hashcode : " + sig.hashCode());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getCertificateSHA1Fingerprint() {
        PackageManager pm = getPackageManager();
        String packageName = getPackageName();
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        InputStream input = new ByteArrayInputStream(cert);
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(c.getEncoded());
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }

    public static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1) h = "0" + h;
            if (l > 2) h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1)) str.append(':');
        }
        return str.toString();
    }
}
