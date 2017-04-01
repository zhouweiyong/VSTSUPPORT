package com.vst.vstsupport.control.login.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.login.utils.LoginUtils;
import com.vst.vstsupport.control.main.activity.MainActivity;
import com.vst.vstsupport.control.setting.version.VersionManager;
import com.vst.vstsupport.utils.PreferHelper;
import com.vst.vstsupport.view.ClearableEditText;
import com.vst.vstsupport.view.PromptOperatDialog;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.params.encryption.MD5Security;
import com.vstecs.android.funframework.netstatus.NetUtils;
import com.vstecs.android.funframework.validation.EditTextValidator;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/14
 * class description:请输入类描述
 */
public class LoginActivity extends BaseAct implements CompoundButton.OnCheckedChangeListener {
    private ClearableEditText cet_name_al;
    private ClearableEditText cet_pwd_al;
    private CheckBox cb_auto_login_al;
    private Button btn_al;
    private TextView tv_find_pwd_al;
    private EditTextValidator editTextValidator;
    private boolean isExit = false;
    private boolean isAutoLogin = false;
    /**
     * 是否退出应用
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            isExit = false;
        }
    };

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return null;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == 1 && eventCenter.getData().equals("success")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (eventCenter.getEventCode() == 1 && eventCenter.getData().equals("fail")) {

        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                showToastMsg("再按一次退出应用");
                isExit = true;
                mHandler.sendEmptyMessageDelayed(0, 1000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initWidget() {
        cet_name_al = (ClearableEditText) findViewById(R.id.cet_name_al);
        cet_pwd_al = (ClearableEditText) findViewById(R.id.cet_pwd_al);
        cb_auto_login_al = (CheckBox) findViewById(R.id.cb_auto_login_al);
        btn_al = (Button) findViewById(R.id.btn_al);
        tv_find_pwd_al = (TextView) findViewById(R.id.tv_find_pwd_al);
        btn_al.setOnClickListener(this);
        tv_find_pwd_al.setOnClickListener(this);
        cb_auto_login_al.setOnCheckedChangeListener(this);

        String userName = PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_NAME);
        if (!TextUtils.isEmpty(userName)) {
            cet_name_al.setText(userName);
        }

//        editTextValidator = new EditTextValidator(this).
//                add(new ValidationModel(cet_name_al, new UserNameValidation())).
//                add(new ValidationModel(cet_pwd_al, new PassWordValidation())).
//                execute();

        cet_name_al.setText(PreferHelper.getInstance().getString(PreferHelper.KEY_LOGIN_NAME));

        if (LoginUtils.isAutoLogin()) {
            cb_auto_login_al.setChecked(true);
            new LoginUtils(false, LoginActivity.this).autoLogin();
        }
//        VersionManager versionManager = new VersionManager(this);
//        versionManager.checkVersion(true, false,handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VersionManager.HANDLER_HAVE_NEW:
                    break;
                case VersionManager.HANDLER_NO_HAVE_NEW:

                    break;
            }
        }
    };

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_al:
                String userName = getInputStr(cet_name_al);
                String pwd = getInputStr(cet_pwd_al);
                if (TextUtils.isEmpty(userName)) {
                    showToastMsg("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    showToastMsg("请输入密码");
                    return;
                }
                //new LoginUtils().login("junjie.cai", MD5Security.getMd5_32_UP("123456"));
                new LoginUtils(isAutoLogin, this).login(getInputStr(cet_name_al), MD5Security.getMd5_32_UP(getInputStr(cet_pwd_al)));
//                new LoginUtils(isAutoLogin, this).login("qi.luo", MD5Security.getMd5_32_UP("123456"));
//                new LoginUtils(isAutoLogin, this).login("zhang.yingming", MD5Security.getMd5_32_UP("1"));
//                new LoginUtils(isAutoLogin, this).login("sen.li", MD5Security.getMd5_32_UP("8"));
//                new LoginUtils(isAutoLogin, this).login("chen.jie1", MD5Security.getMd5_32_UP("123456"));
//                new LoginUtils(isAutoLogin, this).login("junjie.cai", MD5Security.getMd5_32_UP("123456"));
                // new LoginUtils(isAutoLogin, this).login("susie.chen", MD5Security.getMd5_32_UP("1"));
                break;
            case R.id.tv_find_pwd_al:
//                Intent intent = new Intent(this,FindPwdActivity.class);
//                intent.putExtra("userName",getInputStr(cet_name_al));
//                showActivity(this,intent);

                View dialogView = View.inflate(this, R.layout.dialog_content, null);
                TextView content_des_tv = (TextView) dialogView.findViewById(R.id.content_dialog);
                content_des_tv.setText("请到BPS系统中找回密码!");
                final PromptOperatDialog initDialog = new PromptOperatDialog(this, dialogView);
                initDialog.setTitleGone();
                initDialog.setCancelBtnGone();
                initDialog.setConfirm("确定");
                initDialog.setConfirmClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initDialog.dismiss();
                    }
                });
                initDialog.show();


                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isAutoLogin = isChecked;
    }
}
