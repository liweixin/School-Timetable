package com.qianrushi.schooltimetable.viewpager.fragment.One;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.activity.EditCourseActivity;
import com.qianrushi.schooltimetable.activity.SimulateLoginAcitivity;
import com.qianrushi.schooltimetable.utils.Util;
import com.qianrushi.schooltimetable.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lwx on 2016/3/28.
 */
public class ButtonFragment extends Fragment implements View.OnClickListener{
    private View mView;
    public static final int LOGIN = 512;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        mView = inflater.inflate(R.layout.fragment_button, container, false);
        mView.findViewById(R.id.import_course).setOnClickListener(this);
        mView.findViewById(R.id.add_course).setOnClickListener(this);
        mView.findViewById(R.id.delete_course).setOnClickListener(this);
        mView.findViewById(R.id.edit_course).setOnClickListener(this);
        return mView;
    }
    public IWXAPI wxApi;
    private static final int THUMB_SIZE = 150;
    public void WxSendRequest(int towhere, String sharestr) {
        /*Util.getInstance().toast("博饼微信分享");
        WXTextObject txtObject = new WXTextObject();
        txtObject.text = sharestr;
        WXMediaMessage msg = new WXMediaMessage(txtObject);
        msg.title = "标题";
        msg.description = "内容";

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;

        if (towhere == 0) {
            req.scene = SendMessageToWX.Req.WXSceneSession;//分享给好友
        } else {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
        }
        wxApi.sendReq(req);*/
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.share_pic);
        WXImageObject imgObj = new WXImageObject(BitmapFactory.decodeResource(getResources(), R.drawable.share_pic_big));

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        bmp.recycle();
        msg.description = "我正在使用能够在整个屏幕下显示所有课程的小课表，你也来试试吧(´・∀・｀)!";
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);  // 设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        wxApi.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.import_course:
                Toast.makeText(getActivity().getApplicationContext(), "Import course", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getActivity(), WebViewActivity.class);
                Intent intent = new Intent(getActivity(), SimulateLoginAcitivity.class);
                getActivity().startActivityForResult(intent, LOGIN);
                break;
            case R.id.add_course:
                Intent intent1 = new Intent(getActivity(), WXEntryActivity.class);
                intent1.putExtra("Mode", Util.ADD_COURSE);
                startActivity(intent1);
                wxApi = WXAPIFactory.createWXAPI(getContext(), Util.APP_ID);
                wxApi.registerApp(Util.APP_ID);
                WxSendRequest(1, "发送的消息");
                //Toast.makeText(getActivity().getApplicationContext(), "Add course.", Toast.LENGTH_LONG).show();
                break;
            case R.id.delete_course:
                Toast.makeText(getActivity().getApplicationContext(), "Delete course.", Toast.LENGTH_LONG).show();
                break;
            case R.id.edit_course:
                Intent intent2 = new Intent(getActivity(), EditCourseActivity.class);
                intent2.putExtra("Mode", Util.EDIT_COURSE);
                startActivity(intent2);
                Toast.makeText(getActivity().getApplicationContext(), "Edit course.", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
