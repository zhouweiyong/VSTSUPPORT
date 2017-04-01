package com.vst.vstsupport.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;


public class PromptOperatDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private Button cancel_btn;
    private Button confirm_btn;
    private View view;
    private TextView title;
    private FrameLayout frameLayout;
    private Object object;
    private View line1;
    private View center_line;
    private LinearLayout confrim_cancle_btn_loy;

    public PromptOperatDialog(Context context, View view) {
        this(context, R.style.prompt_operat_dialog);
        this.context = context;
        this.view = view;
        init();
    }

    public PromptOperatDialog(Context context, int theme) {
        super(context, theme);
    }

    private void init() {
        setContentView(R.layout.dialog_prompt_operator);
        if (view != null) {
            frameLayout = (FrameLayout) findViewById(R.id.dialog_main_layout);
            frameLayout.addView(view);
        }
        title = (TextView) findViewById(R.id.title);
        cancel_btn = (Button) findViewById(R.id.btn_cancel);
        confirm_btn = (Button) findViewById(R.id.btn_confirm);
        line1 = findViewById(R.id.line1);
        center_line = findViewById(R.id.center_line);
        confrim_cancle_btn_loy = (LinearLayout) findViewById(R.id.confrim_cancle_btn_loy);
        cancel_btn.setOnClickListener(this);
    }

    public void setTitleGone() {
        title.setVisibility(View.GONE);
    }

    public void setCenterLineGone() {
        center_line.setVisibility(View.GONE);
    }

    public void setLineGone() {
        line1.setVisibility(View.GONE);
    }

    public void setCancelBtnGone() {
        cancel_btn.setVisibility(View.GONE);
    }

    public void setConfirmlBtnGone() {
        confrim_cancle_btn_loy.setVisibility(View.GONE);
    }

    public Object getTag() {
        return object;
    }

    public void setTag(Object object) {
        this.object = object;
    }

    public void setTitile(String titleStr) {
        title.setText(titleStr);
    }

    public void setCancel(String cancel) {
        cancel_btn.setText(cancel);
    }

    public void setConfirm(String confirm) {
        confirm_btn.setText(confirm);
    }

    public void setConfirmClick(View.OnClickListener listener) {
        confirm_btn.setOnClickListener(listener);
    }

    public void setCancelClick(View.OnClickListener listener) {
        cancel_btn.setOnClickListener(listener);
    }

    public Button getConfirmBtn() {
        return confirm_btn;
    }

    public Button getCancelBtn() {
        return cancel_btn;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }

}
