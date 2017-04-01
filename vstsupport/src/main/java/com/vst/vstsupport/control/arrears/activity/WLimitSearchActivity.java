package com.vst.vstsupport.control.arrears.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.BrandBean;
import com.vst.vstsupport.mode.bean.WLimitFollowSearchBean;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.netstatus.NetUtils;

import de.greenrobot.event.EventBus;

public class WLimitSearchActivity extends BaseAct {

    private EditText account_name_et;
    private EditText sales_man_et;
    private TextView brand_tv;
    private RelativeLayout brand_loy;
    private Button btn_confirm;
    private String flag;//判断是从哪个入口进来inventory_follow:库存跟进列表  inventory_look:库存查看列表  limit_follow:超期跟进列表  limit_look:应收查看


    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        flag = getIntent().getStringExtra("flag");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty).setTitleMsg("查询");
        brand_loy = (RelativeLayout) findViewById(R.id.brand_loy);
        brand_loy.setOnClickListener(this);
        account_name_et = (EditText) findViewById(R.id.account_name_et);
        sales_man_et = (EditText) findViewById(R.id.sales_man_et);
        brand_tv = (TextView) findViewById(R.id.brand_tv);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);


    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.brand_loy:
                startSelectActivity("4", 204, flag);
                break;
            case R.id.btn_confirm:
                WLimitFollowSearchBean searchBean = new WLimitFollowSearchBean();
                if (!brand_tv.getText().toString().equals("请选择")){
                    searchBean.brandName = brand_tv.getText().toString();
                }
                searchBean.customerName = getInputStr(account_name_et);
                searchBean.saleName = getInputStr(sales_man_et);
                EventBus.getDefault().post(new EventCenter<WLimitFollowSearchBean>(4, searchBean));

                finish();
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startSelectActivity(String flag, int requestCode, String entrance) {
        Intent intent = new Intent(aty, CommonSelectActivity.class);
        intent.putExtra("flag", flag);//flag区分是事业部，二级线，区域
        intent.putExtra("entrance", entrance);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 204) {
            if (data != null) {
                BrandBean brandBean = (BrandBean) data.getSerializableExtra("object");
                brand_tv.setText(brandBean.brandName);
            }
        }
    }


    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
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
        setContentView(R.layout.activity_wlimit_search);
    }


}
