package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.FollowUpBean;
import com.vst.vstsupport.mode.bean.InventoryBean;
import com.vst.vstsupport.mode.bean.LimitBean;
import com.vst.vstsupport.mode.bean.WLimitBean;
import com.vst.vstsupport.mode.bean.test.NullBean;
import com.vst.vstsupport.view.ChangeDatePopwindow;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.netstatus.NetUtils;

import de.greenrobot.event.EventBus;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:超期跟进flag=limit,库存跟进flag=inventroy
 */
public class FollowUpActivity extends BaseAct {

    private EditText reply_et;
    private TextView clear_time_tv;
    private TextView ocuppay_fund_tv;
    private Button btn_confirm;
    private String flag;
    private LimitBean limitBean;
    private WLimitBean mWLimitBean;
    private InventoryBean inventoryBean;
    private int position;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        flag=getIntent().getStringExtra("flag");
        mWLimitBean= (WLimitBean) getIntent().getSerializableExtra("mWLimitBean");
        limitBean= (LimitBean) getIntent().getSerializableExtra("limitBean");
        inventoryBean= (InventoryBean) getIntent().getSerializableExtra("inventoryBean");
        position = intent.getIntExtra("position",-1);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty).setTitleMsg("跟进");
        reply_et= (EditText) findViewById(R.id.reply_et);
        reply_et.setHint(flag .equals("limit_look")||flag .equals("limit_look_w")?"解决方案":"清理策略");
        clear_time_tv= (TextView) findViewById(R.id.clear_time_tv);
        ocuppay_fund_tv= (TextView) findViewById(R.id.ocuppay_fund_tv);
        ocuppay_fund_tv.setVisibility(flag .equals("limit_look")||flag .equals("limit_look_w") ? View.GONE : View.VISIBLE);
        btn_confirm= (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        clear_time_tv.setOnClickListener(this);
        ocuppay_fund_tv.setOnClickListener(this);
    }


    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()){
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(getInputStr(reply_et))){
                    showToastMsg("请输入清理策略");
                    return;
                }

                if (TextUtils.isEmpty(clear_time_tv.getText().toString())){
                    showToastMsg("请输入清理时间");
                    return;
                }
                if (flag.equals("limit_look")){
                    getFollowUpData();
                }else if(flag.equals("limit_look_w")){
                    getVstOverdueFollow();
                }else{

//                    if (!ocuppay_fund_tv.isSelected()){
//                        showToastMsg("请勾选是否占用资金池");
//                        return;
//                    }
                    getInventorFollowUpData();
                }

                break;
            case R.id.left:
            finish();
                break;
            case R.id.clear_time_tv:
                ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(
                        aty);
                mChangeBirthDialog.setDate(mChangeBirthDialog.getYear(), mChangeBirthDialog.getMonth(), mChangeBirthDialog.getDay());
                mChangeBirthDialog.showAtLocation(clear_time_tv, Gravity.BOTTOM, 0, 0);
                mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

                    @Override
                    public void onClick(String year, String month, String day) {
                        // TODO Auto-generated method stub
                        showToastMsg(year + "-" + month + "-" + day);
                        StringBuilder sb = new StringBuilder();
                        sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, month.length() - 1)).append("-").append(day.substring(0,day.length()-1));
                        clear_time_tv.setText(sb.toString());
                    }
                });
                break;
            case R.id.ocuppay_fund_tv:
                if (ocuppay_fund_tv.isSelected()){
                    ocuppay_fund_tv.setSelected(false);
                }else{
                    ocuppay_fund_tv.setSelected(true);
                }

                break;

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
        setContentView(R.layout.activity_follow_up);
    }

    //佳杰超期跟进
    private void getFollowUpData(){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
        params.putCommonTypeParam("customerName",""+ limitBean.customerName);
        params.putCommonTypeParam("secendLevelLine",""+ limitBean.secendLevelLine);
        params.putCommonTypeParam("personId",""+ limitBean.personId);
        params.putCommonTypeParam("solution",""+ getInputStr(reply_et));
        params.putCommonTypeParam("remark",""+ clear_time_tv.getText().toString());

        params.putCommonTypeParam("isvstuser",""+VstApplication.getInstance().getUserBean().getIsvstuser());//是否是vstuser
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());//是否是vstuser


        PostRequest<RequestResult<FollowUpBean>> request = new PostRequest<RequestResult<FollowUpBean>>(UrlConstants.OVERDUEFOLLW, params, new OnNetSuccuss<RequestResult<FollowUpBean>>() {
            @Override
            public void onSuccess(RequestResult<FollowUpBean> response) {

                Log.i("vst", response.msg);
                if (response.rs!=null){
                    FollowUpBean limitRsBean=response.rs;
                    showToastMsg(response.msg);
                    EventBus.getDefault().post(new EventCenter<Integer>(1014,position));
                    finish();
                }
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception+"");
                dismissLoadingDialog();
            }
        },new TypeToken<RequestResult<FollowUpBean>>(){}.getType());
        executeRequest(request);
    }

    //佳杰库存跟进
    private void getInventorFollowUpData(){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
        params.putCommonTypeParam("businessDepartment",""+ inventoryBean.businessDepartment);
        params.putCommonTypeParam("secendLevelLine",""+ inventoryBean.secendLevelLine);
        params.putCommonTypeParam("type",""+ inventoryBean.type);
//        if (inventoryBean.type.equals("项目")){
//
//        }else if(inventoryBean.type.equals("流量")){
//            params.putCommonTypeParam("productName",""+ inventoryBean.productName);
//        }
        params.putCommonTypeParam("projectSerial",""+inventoryBean.projectSerial);
        params.putCommonTypeParam("solution",""+ clear_time_tv.getText().toString());
        params.putCommonTypeParam("reason",""+ getInputStr(reply_et));

        params.putCommonTypeParam("fundDesc",""+(ocuppay_fund_tv.isSelected()?"1":"0"));//1是 0否

        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());


        PostRequest<RequestResult<NullBean>> request = new PostRequest<RequestResult<NullBean>>(UrlConstants.INVENTORYFOLLOW, params, new OnNetSuccuss<RequestResult<NullBean>>() {
            @Override
            public void onSuccess(RequestResult<NullBean> response) {
                showToastMsg(response.msg);
                finish();
                Log.i("vst", response.msg);
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception+"");
                dismissLoadingDialog();
            }
        },new TypeToken<RequestResult<NullBean>>() {
        }.getType());
        executeRequest(request);
    }

    /**
     * 伟仕的跟进
     */
    private void getVstOverdueFollow(){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
        params.putCommonTypeParam("customerId",""+ mWLimitBean.customerId);
        params.putCommonTypeParam("brandId",""+ mWLimitBean.brandId);
        params.putCommonTypeParam("personId",""+ mWLimitBean.personId);
        params.putCommonTypeParam("remark",""+ clear_time_tv.getText().toString());
        params.putCommonTypeParam("solution",""+ getInputStr(reply_et));
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());


        PostRequest<RequestResult<NullBean>> request = new PostRequest<RequestResult<NullBean>>(UrlConstants.VSTOVERDUEFOLLOW, params, new OnNetSuccuss<RequestResult<NullBean>>() {
            @Override
            public void onSuccess(RequestResult<NullBean> response) {
                showToastMsg(response.msg);
                finish();
                Log.i("vst", response.msg);
                dismissLoadingDialog();
                EventBus.getDefault().post(new EventCenter<Integer>(1014,position));
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception+"");
                dismissLoadingDialog();
            }
        },new TypeToken<RequestResult<NullBean>>() {
        }.getType());
        executeRequest(request);
    }
}
