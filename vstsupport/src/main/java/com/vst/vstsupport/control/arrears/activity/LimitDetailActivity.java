package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.arrears.adapter.LimitDetailMsgAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.LimitBean;
import com.vst.vstsupport.mode.bean.LimitDetailBean;
import com.vst.vstsupport.mode.bean.LimitDetailMsgBean;
import com.vst.vstsupport.mode.bean.PublishCommentBean;
import com.vst.vstsupport.utils.ClearableEditText;
import com.vst.vstsupport.utils.DateTimeTool;
import com.vst.vstsupport.utils.FormatUtil;
import com.vst.vstsupport.utils.RightTextUtil;
import com.vst.vstsupport.view.ListViewForScrollView;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.netstatus.NetUtils;

import java.util.ArrayList;
import java.util.Date;

public class LimitDetailActivity extends BaseAct {

    public TextView name_tv;
    public TextView has_calculate_tv;
    public TextView second_line_tv;
    public TextView count_total_tv;
    public TextView last_long_time_human_tv;
    public TextView company_dept_tv;
    public TextView dept_tv;
    public TextView deal_tv;
    public TextView clear_time_tv;
    public ImageView open_close_iv;
    public LinearLayout common_item_open_close_layout;
    public RelativeLayout genloy;


    public TextView des_day01to15_tv;
    public TextView des_day16to30_tv;
    public TextView des_day31to60_tv;
    public TextView des_day60up_tv;

    public TextView day01to15_tv;
    public TextView day16to30_tv;
    public TextView day31to60_tv;
    public TextView day60up_tv;

    private ListViewForScrollView comunicate_lv;
    private LimitDetailMsgAdapter adapter;
    private boolean isOpen;
    private ScrollView scrollloy;

    public RelativeLayout send_msg_loy;
    public ClearableEditText send_msg_et;
    public TextView send_msg_tv;
    private TitleBarHelper titleBarHelper;
    private RelativeLayout open_close_loy;
    LimitBean limitBean=new LimitBean();
    private LimitBean netLimitBean=new LimitBean();
    private ArrayList<LimitDetailMsgBean> msgDatas=new ArrayList<LimitDetailMsgBean>();
    public String sendFlag;//超期详情里面能否发送消息 1能 0 否 null:消息列表进入
    public static String  accountReceive;//领导在此处问询时，接收的业务员账号
    public long serverTime;
    private String operation;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        limitBean= (LimitBean) getIntent().getSerializableExtra("LimitBean");
        operation=getIntent().getStringExtra("operation");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLimitDetailData("0");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
//        CommonLimitBean bean=beans.getDatas().get(0);
        titleBarHelper= new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty);
        titleBarHelper.setTitleMsg("详情");
        titleBarHelper.setRightMsg("跟进");
        titleBarHelper.setOnRightClickListener(this);
        genloy = (RelativeLayout) findViewById(R.id.genloy);
        genloy.setVisibility(View.GONE);
        name_tv = (TextView) findViewById(R.id.name_tv);
        has_calculate_tv = (TextView) findViewById(R.id.has_calculate_tv);
        has_calculate_tv.setVisibility(View.GONE);
        second_line_tv = (TextView) findViewById(R.id.second_line_tv);
        count_total_tv = (TextView) findViewById(R.id.count_total_tv);
        last_long_time_human_tv = (TextView) findViewById(R.id.last_long_time_human_tv);
        company_dept_tv = (TextView) findViewById(R.id.company_dept_tv);
        dept_tv = (TextView) findViewById(R.id.dept_tv);
        if (operation.equals("follow")) {//销售进来
            dept_tv.setVisibility(View.GONE);
            company_dept_tv.setVisibility(View.GONE);
        }else{
            dept_tv.setVisibility(View.VISIBLE);
            company_dept_tv.setVisibility(View.VISIBLE);
        }
        deal_tv = (TextView) findViewById(R.id.deal_tv);
        clear_time_tv = (TextView) findViewById(R.id.clear_time_tv);
        open_close_iv = (ImageView) findViewById(R.id.open_close_iv);
        open_close_loy = (RelativeLayout) findViewById(R.id.open_close_loy);
        common_item_open_close_layout = (LinearLayout) findViewById(R.id.common_item_open_close_layout);
        scrollloy = (ScrollView) findViewById(R.id.scrollloy);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollloy.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 3000);
        open_close_loy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollloy.smoothScrollTo(0, 0);
                if (isOpen) {
                    common_item_open_close_layout.setVisibility(View.GONE);
                    open_close_iv.setImageResource(R.mipmap.common_down_icon);
                    isOpen = false;
                } else {
                    common_item_open_close_layout.setVisibility(View.VISIBLE);
                    open_close_iv.setImageResource(R.mipmap.common_up_icon);
                    isOpen = true;
                }
            }
        });


        des_day01to15_tv = (TextView) findViewById(R.id.des_day0to60_tv);
        des_day16to30_tv = (TextView) findViewById(R.id.des_day61to90_tv);
        des_day31to60_tv = (TextView) findViewById(R.id.des_day91to120_tv);
        des_day60up_tv = (TextView) findViewById(R.id.des_day120up_tv);

        day01to15_tv = (TextView) findViewById(R.id.day0to60_tv);
        day16to30_tv = (TextView) findViewById(R.id.day61to90_tv);
        day31to60_tv = (TextView) findViewById(R.id.day91to120_tv);
        day60up_tv = (TextView) findViewById(R.id.day120up_tv);
        comunicate_lv = (ListViewForScrollView) findViewById(R.id.comunicate_lv);
        adapter=new LimitDetailMsgAdapter(this);
        comunicate_lv.setAdapter(adapter);

        send_msg_loy = (RelativeLayout) findViewById(R.id.send_msg_loy);
        send_msg_et = (ClearableEditText) findViewById(R.id.send_msg_et);
        send_msg_tv = (TextView) findViewById(R.id.send_msg_tv);


    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId()==R.id.left){
            finish();
        }else if(v.getId()==R.id.right){
            Intent intent=new Intent(aty,FollowUpActivity.class);
            intent.putExtra("flag","limit_look");
            intent.putExtra("limitBean",limitBean);
            showActivity(aty, intent);
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
        return genloy;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_limit_detail);
    }

    private void getLimitDetailData(String messageFlag ){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
        params.putCommonTypeParam("customerName",""+ limitBean.customerName);
        params.putCommonTypeParam("secendLevelLineList",""+ limitBean.secendLevelLine);
        params.putCommonTypeParam("personId",""+ limitBean.personId);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());
        params.putCommonTypeParam("messageFlag", "" + messageFlag);//0：表示超期列表进入，1：表示消息列表进入 	发送消息的标志，从消息列表跳入需要，区分业务员能否再次页面回复消息
        PostRequest<RequestResult<LimitDetailBean>> request = new PostRequest<RequestResult<LimitDetailBean>>(UrlConstants.INTOOVERDUEDETAIL, params, new OnNetSuccuss<RequestResult<LimitDetailBean>>() {
            @Override
            public void onSuccess(RequestResult<LimitDetailBean> response) {
                Log.i("vst", response.msg);
                if (response.rs!=null){
                    netLimitBean=response.rs.overdueData;
                    serverTime=response.rs.nowTime;
                    if (netLimitBean!=null){
                        setTopViewData();
                        accountReceive=netLimitBean.accountReceive;
                    }else{
                        setNullloy();
                    }

                    msgDatas=response.rs.overdueMessage;
                    sendFlag=response.rs.sendFlag;


                    setSendLoy();
                    adapter.setGroup(msgDatas);
                    genloy.setVisibility(View.VISIBLE);
//                    showToastMsg(response.msg);
                }else {
                    setNullloy();
                }
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception+"");
                showErrorView();
                dismissLoadingDialog();
            }
        },new TypeToken<RequestResult<LimitDetailBean>>(){}.getType());
        executeRequest(request);
    }

        private void setNullloy(){
            toggleShowEmpty(true, "暂无结果", null, R.mipmap.check_look_no_data_icon);
            titleBarHelper.setRightGone();
        }


    private void setTopViewData() {
        des_day01to15_tv.setText("0-15天");
        des_day16to30_tv.setText("16-30天");
        des_day31to60_tv.setText("31-60天");
        des_day60up_tv.setText("60天以上");
        name_tv.setText(netLimitBean.customerName);
        second_line_tv.setText(netLimitBean.secendLevelLine);
        count_total_tv.setText(FormatUtil.limitFunFormat(netLimitBean.totalOverdueMoney));

        day01to15_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day1to15));
        day16to30_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day16to30));
        day31to60_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day31to60));
        day60up_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day60up));


        String currentDay = DateTimeTool.getWeekOfDate(new Date(serverTime));
        if (operation.equals("follow")) {//销售进来
            String week = "星期一,星期六,星期日";
//            if ((!week.contains(currentDay))&&(!TextUtils.isEmpty(netLimitBean.solution))) {//销售于周六、周日、周一可在线点“跟进”，填写 “解决方案”和“清理时间”。其他时间隐藏按钮
//                titleBarHelper.setRightGone();
//            }

            if(week.contains(currentDay)){
                if(TextUtils.isEmpty(netLimitBean.solution)){
                    titleBarHelper.setRightVisible();
                }else{
                    RightTextUtil.setRightTextStyle(titleBarHelper);
                }
            }else{
                RightTextUtil.setRightTextStyle(titleBarHelper);
            }

        }else{
            titleBarHelper.setRightGone();
        }
        last_long_time_human_tv.setText(Html.fromHtml("<font color='#999999'>最长逾期天数：</font>" + netLimitBean.totalOverdueDay + "天<font color='#999999'> &nbsp&nbsp| &nbsp&nbsp 业务员：</font>" + netLimitBean.saleName));

        deal_tv.setText(Html.fromHtml("" + (TextUtils.isEmpty(netLimitBean.solution) ? "暂未填写" : netLimitBean.solution)));

        clear_time_tv.setText(Html.fromHtml("<font color='#999999'>清理时间: &nbsp</font>" + (TextUtils.isEmpty(netLimitBean.dealTime) ? "暂未填写" : netLimitBean.dealTime)));
        company_dept_tv.setText(Html.fromHtml("<font color='#999999'>区 &nbsp&nbsp 域  &nbsp&nbsp  ：</font>" + netLimitBean.branchCompany));
        dept_tv.setText(Html.fromHtml("<font color='#999999'>事   &nbsp&nbsp       业  &nbsp&nbsp&nbsp 部&nbsp&nbsp ：</font>" + netLimitBean.businessDepartment));
    }


    //发送消息接口
    public void publishComments (final String sourceAccount, final String account){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();

//        {"customerName":"江西省通信产业服务有限公司物流分公司","token":"3bad6af0fa4b8b330d162e19938ee981","secendLevelLineList":"HPCDX","personId":"1351"}
        params.putCommonTypeParam("customerName", "" + limitBean.customerName);
        params.putCommonTypeParam("unionSecdCode", "" + limitBean.secendLevelLine);
        params.putCommonTypeParam("sourceAccount", "" + sourceAccount);//接受者账号
        params.putCommonTypeParam("personId", "" + limitBean.personId);//销售人员BPS ID(库存积压类型时，次字段代表事业部名称)
        params.putCommonTypeParam("content", "" + getInputStr(send_msg_et));
        params.putCommonTypeParam("commentsType", "1");//评论类型（1：应收 2:库存项目类型跟进 3:库存流量类型跟进）
        params.putCommonTypeParam("account", "" + account);//发送者账号
        params.putCommonTypeParam("isvstuser", "" + VstApplication.getInstance().getUserBean().getIsvstuser());//是否伟仕用户 0-不是 1-是

        PostRequest<RequestResult<PublishCommentBean>> request = new PostRequest<RequestResult<PublishCommentBean>>(UrlConstants.PUBLISHCOMMENTS, params, new OnNetSuccuss<RequestResult<PublishCommentBean>>() {
            @Override
            public void onSuccess(RequestResult<PublishCommentBean> response) {
                Log.i("vst", response.msg);
//                showToastMsg(response.msg);
                if (response.rs!=null){
                    LimitDetailMsgBean limitDetailMsgBean=new LimitDetailMsgBean();
                    limitDetailMsgBean.SEND_ACCOUNT=response.rs.account;
                    limitDetailMsgBean.CONTENT=response.rs.content;
                    limitDetailMsgBean.SOURCE_ACCOUNT=response.rs.sourceAccount;
                    limitDetailMsgBean.CREATE_DATE=response.rs.times;
                    adapter.addItem(limitDetailMsgBean);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollloy.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    },600);
                    send_msg_et.setText("");
                }

                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception + "");
                dismissLoadingDialog();
            }
        }, new TypeToken<RequestResult<PublishCommentBean>>() {
        }.getType());
        executeRequest(request);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void setSendLoy(){

        if (!sendFlag.equals("0")) {//领导
            send_msg_loy.setVisibility(View.VISIBLE);
            send_msg_loy.setVisibility(View.VISIBLE);


            send_msg_et.setHint("回复" + accountReceive+":");
//            send_msg_et.setSelection(send_msg_et.getHint().length());
            send_msg_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        limitDetailActivity.publishComments(getItem(position).SOURCE_ACCOUNT,VstApplication.getInstance().getUserBean().getAccount());
                    if (!TextUtils.isEmpty(getInputStr(send_msg_et)))
                    publishComments(accountReceive, VstApplication.getInstance().getUserBean().getAccount());
                }
            });
        } else {
            send_msg_loy.setVisibility(View.GONE);
        }
    }

}
