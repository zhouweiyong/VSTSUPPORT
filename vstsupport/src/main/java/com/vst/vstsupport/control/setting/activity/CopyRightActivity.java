package com.vst.vstsupport.control.setting.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.utils.webview.WebActivity;
import com.vst.vstsupport.view.TitleBarHelper;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.netstatus.NetUtils;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/19
 * class description:请输入类描述
 */
public class CopyRightActivity extends BaseAct {
    private TextView tv_copy_right;
    private String url;

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View getContainerTargetView() {
        return null;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_copy_right);
    }

    @Override
    protected void initWidget() {
        url = VstApplication.getInstance().getUserBean().getTeamIntroductionUrl();
        TitleBarHelper titleBarHelper = new TitleBarHelper(CopyRightActivity.this, R.string.title_copy_right);
        titleBarHelper.setLeftMsg("");
        if (!TextUtils.isEmpty(url)) {
           // titleBarHelper.setRightMsg("制作团队");
            titleBarHelper.setRightImg(R.mipmap.icon_group);
        }
        titleBarHelper.setOnRightImgClickListener(this);

        tv_copy_right = (TextView) findViewById(R.id.tv_copy_right);
        try {
            String pName = "com.vst.vstsupport";
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(pName, PackageManager.GET_CONFIGURATIONS);
            tv_copy_right.setText(String.format("伟仕佳杰 V %s", packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void widgetClick(View v) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("flag", "4");
        showActivity(this, intent);
    }
}
