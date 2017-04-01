package com.vst.vstsupport.control.login.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vst.vstsupport.R;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.validation.EmailValidation;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.netstatus.NetUtils;
import com.vstecs.android.funframework.validation.EditTextValidator;
import com.vstecs.android.funframework.validation.ValidationModel;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/14
 * class description:请输入类描述
 */
public class FindPwdActivity extends BaseAct {

    private Button btn_afp;
    private EditText et_email_afp;
    private EditTextValidator editTextValidator;
    private String userName;

    @Override
    protected void initData(Intent intent) {
        userName = intent.getStringExtra("userName");
    }

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
        setContentView(R.layout.activity_find_pwd);
    }

    @Override
    protected void initWidget() {
        TitleBarHelper titleBarHelper = new TitleBarHelper(this, R.string.title_find_pwd);
        titleBarHelper.setLeftMsg("");
        btn_afp = (Button) findViewById(R.id.btn_afp);
        et_email_afp = (EditText) findViewById(R.id.et_email_afp);
        btn_afp.setOnClickListener(this);

        editTextValidator = new EditTextValidator(this).
                add(new ValidationModel(et_email_afp, new EmailValidation())).
                execute();
    }

    @Override
    public void widgetClick(View v) {
        if (editTextValidator.validate()) {
            String email = getInputStr(et_email_afp);
            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "找回密码");
            intent.putExtra(Intent.EXTRA_TEXT, String.format("忘记密码，需要找回密码，账号：%s",userName));
            intent.setType("image/*");
            intent.setType("message/rfc882");
            Intent.createChooser(intent, "Choose Email Client");
            startActivity(intent);
        }
    }
}
