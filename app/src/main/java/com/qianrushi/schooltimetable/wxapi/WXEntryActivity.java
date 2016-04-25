package com.qianrushi.schooltimetable.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.qianrushi.schooltimetable.R;
import com.qianrushi.schooltimetable.base.BaseActivity;
import com.qianrushi.schooltimetable.utils.Util;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by lwx on 2016/4/22.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_blank);
        Util.getInstance().toast("微信响应页面");
        api = WXAPIFactory.createWXAPI(this, Util.APP_ID, false);
        api.registerApp(Util.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq arg0) {
        Util.getInstance().toast("微信" +"BaseReq:" + arg0.getType());
        switch (arg0.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Util.getInstance().toast("" + "ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Util.getInstance().toast("" + "ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                break;
        }
        finish();
    }
    @Override
    public void onResp(BaseResp arg0) {
        Util.getInstance().toast("微信" + "BaseResp:" + arg0.errCode);
        String result = "";
        switch (arg0.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "亲，分享成功了";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "认证失败";
                break;
            default:
                result = "errcode_unknown";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }

}
