package com.vst.vstsupport.control.setting.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.base.BaseFra;
import com.vst.vstsupport.control.login.activity.LoginActivity;
import com.vst.vstsupport.control.login.utils.LoginUtils;
import com.vst.vstsupport.control.setting.activity.CopyRightActivity;
import com.vst.vstsupport.control.setting.version.VersionManager;
import com.vst.vstsupport.view.PromptOperatDialog;
import com.vstecs.android.funframework.eventbus.EventCenter;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/13
 * class description:请输入类描述
 */
public class SettingFragment extends BaseFra {


    private TextView tv_version_fs;
    private TextView tv_update_fs;
    private TextView tv_logout_fs;

    @Override
    protected View getContainerTargetView() {
        return null;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        TextView title = (TextView) parentView.findViewById(R.id.title_fs);
        title.setText(R.string.title_setting);
        tv_version_fs = (TextView) parentView.findViewById(R.id.tv_version_fs);
        tv_update_fs = (TextView) parentView.findViewById(R.id.tv_update_fs);
        tv_logout_fs = (TextView) parentView.findViewById(R.id.tv_logout_fs);
        tv_version_fs.setOnClickListener(this);
        tv_update_fs.setOnClickListener(this);
        tv_logout_fs.setOnClickListener(this);
    }


    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.tv_version_fs:
                showActivity(activity, CopyRightActivity.class);
                break;
            case R.id.tv_update_fs:
                VersionManager versionManager = new VersionManager(activity);
                versionManager.checkVersion(false, false, new Handler());
                break;
            case R.id.tv_logout_fs:
                logout();
                break;
        }
    }

    private void update() {
        View dialogView = View.inflate(activity, R.layout.dialog_content, null);
        TextView content_des_tv = (TextView) dialogView.findViewById(R.id.content_dialog);
        content_des_tv.setText("是否升级？");
        final PromptOperatDialog initDialog = new PromptOperatDialog(activity, dialogView);
        initDialog.setTitile("检测到新版本");
        initDialog.setCancel("取消");
        initDialog.setConfirm("立即更新");
        initDialog.setConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionManager versionManager = new VersionManager(activity);
                versionManager.checkVersion(false, false, new Handler());
                initDialog.dismiss();

            }
        });
        initDialog.show();
    }

    private void logout() {
        View dialogView = View.inflate(activity, R.layout.dialog_content, null);
        TextView content_des_tv = (TextView) dialogView.findViewById(R.id.content_dialog);
        content_des_tv.setText("是否退出登录？");
        final PromptOperatDialog initDialog = new PromptOperatDialog(activity, dialogView);
        initDialog.setTitile("退出登录");
        initDialog.setCancel("取消");
        initDialog.setConfirm("退出");
        initDialog.setConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtils.logout();
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
                initDialog.dismiss();

            }
        });
        initDialog.show();
    }

}
