package com.qianrushi.schooltimetable.model;

/**
 * Created by lwx on 2016/4/4.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.qianrushi.schooltimetable.utils.Util;

import org.apache.commons.codec.binary.Base64;

public class EncodeAndDecode {

    private EncodeAndDecode(Activity activity) {
    }

    public static List<CourseInfo> readProduct() {
        List<CourseInfo> list=null;
        SharedPreferences preferences = Util.getInstance().getContext().getSharedPreferences("base64",
                Activity.MODE_PRIVATE);
        String courseListBase64 = preferences.getString("courseList", "");
        //读取字节
        if(courseListBase64.length()==0){
            return null;
        }
        byte[] base64 = Base64.decodeBase64(courseListBase64.getBytes());
        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                list = (List<CourseInfo>) bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public static void saveProduct(List<CourseInfo> list) {
        SharedPreferences preferences = Util.getInstance().getContext().getSharedPreferences("base64",
                Activity.MODE_PRIVATE);
        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            //将对象写入字节流
            oos.writeObject(list);
            //将字节流编码成base64的字符窜
            String courseListBase64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            Editor editor = preferences.edit();
            editor.putString("courseList", courseListBase64);
            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.i("ok", "存储成功");
    }
}

