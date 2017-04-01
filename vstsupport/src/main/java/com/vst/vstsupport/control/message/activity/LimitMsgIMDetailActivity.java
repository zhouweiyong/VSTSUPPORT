package com.vst.vstsupport.control.message.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.vst.vstsupport.control.arrears.activity.FollowUpActivity;
import com.vst.vstsupport.control.arrears.adapter.LimitIMDetailMsgAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.LimitBean;
import com.vst.vstsupport.mode.bean.LimitDetailBean;
import com.vst.vstsupport.mode.bean.LimitDetailMsgBean;
import com.vst.vstsupport.mode.bean.PublishCommentBean;
import com.vst.vstsupport.mode.bean.ReceiveMessageContent;
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

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:消息详情，领导问询-应收，销售回复-应收
 */
public class LimitMsgIMDetailActivity extends BaseAct {

    public TextView name_tv;
    public TextView has_calculate_tv;
    public TextView second_line_tv;
    public TextView count_total_tv;
    public TextView last_long_time_human_tv;
    public TextView company_dept_tv;
    public TextView dept_tv;
    public TextView deal_tv;
    public TextView deal_des_tv;
    public TextView clear_time_tv;
    public TextView devide_line;
    public ImageView open_close_iv;
    public LinearLayout common_item_open_close_layout;
    public RelativeLayout genloy;
    public RelativeLayout open_close_loy;


    public TextView des_day01to15_tv;
    public TextView des_day16to30_tv;
    public TextView des_day31to60_tv;
    public TextView des_day60up_tv;

    public TextView day01to15_tv;
    public TextView day16to30_tv;
    public TextView day31to60_tv;
    public TextView day60up_tv;
    public RelativeLayout send_msg_loy;
    public EditText send_msg_et;
    public TextView send_msg_tv;
    public TextView count_total_des_tv;
    public String sendFlag;//超期详情里面能否发送消息 1能 0 否 null:消息列表进入
    public String accountReceive;////领导在此处问询时，接收的业务员账号
    LimitBean limitBean=new LimitBean();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private ListViewForScrollView comunicate_lv;
    private LimitIMDetailMsgAdapter adapter;
    private boolean isOpen;
    private ScrollView scrollloy;
    private TitleBarHelper titleBarHelper;
    private String title;
    private LimitBean netLimitBean=new LimitBean();
    private ArrayList<LimitDetailMsgBean> msgDatas=new ArrayList<LimitDetailMsgBean>();
    public long serverTime;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
//        flag=getIntent().getStringExtra("flag");
        title=getIntent().getStringExtra("title");
        limitBean= (LimitBean) getIntent().getSerializableExtra("LimitBean");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarHelper= new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty);
        titleBarHelper.setTitleMsg(TextUtils.isEmpty(title) ? "" : title);
        genloy = (RelativeLayout) findViewById(R.id.genloy);
        genloy.setVisibility(View.GONE);
        count_total_des_tv = (TextView) findViewById(R.id.count_total_des_tv);
        has_calculate_tv = (TextView) findViewById(R.id.has_calculate_tv);
        has_calculate_tv.setTextColor(Color.parseColor("#999999"));
        has_calculate_tv.setBackgroundResource(R.drawable.selector_limit_btn_bg_unselect);
        has_calculate_tv.setVisibility(View.GONE);
        company_dept_tv = (TextView) findViewById(R.id.company_dept_tv);
        dept_tv = (TextView) findViewById(R.id.dept_tv);
        deal_tv = (TextView) findViewById(R.id.deal_tv);
        deal_des_tv = (TextView) findViewById(R.id.deal_des_tv);
        clear_time_tv = (TextView) findViewById(R.id.clear_time_tv);
        devide_line = (TextView) findViewById(R.id.devide_line);
        last_long_time_human_tv = (TextView) findViewById(R.id.last_long_time_human_tv);

        des_day01to15_tv = (TextView) findViewById(R.id.des_day0to60_tv);
        des_day16to30_tv = (TextView) findViewById(R.id.des_day61to90_tv);
        des_day31to60_tv = (TextView) findViewById(R.id.des_day91to120_tv);
        des_day60up_tv = (TextView) findViewById(R.id.des_day120up_tv);


        name_tv = (TextView) findViewById(R.id.name_tv);
        open_close_loy = (RelativeLayout) findViewById(R.id.open_close_loy);

        second_line_tv = (TextView) findViewById(R.id.second_line_tv);
        count_total_tv = (TextView) findViewById(R.id.count_total_tv);



        open_close_iv = (ImageView) findViewById(R.id.open_close_iv);
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



        day01to15_tv = (TextView) findViewById(R.id.day0to60_tv);
        day16to30_tv = (TextView) findViewById(R.id.day61to90_tv);
        day31to60_tv = (TextView) findViewById(R.id.day91to120_tv);
        day60up_tv = (TextView) findViewById(R.id.day120up_tv);
        comunicate_lv = (ListViewForScrollView) findViewById(R.id.comunicate_lv);
        adapter=new LimitIMDetailMsgAdapter(this);
        comunicate_lv.setAdapter(adapter);
        send_msg_loy = (RelativeLayout) findViewById(R.id.send_msg_loy);
        send_msg_et = (EditText) findViewById(R.id.send_msg_et);
        send_msg_tv = (TextView) findViewById(R.id.send_msg_tv);

        getLimitDetailData("1");
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId()==R.id.left){
            finish();
        }else if(v.getId()==R.id.right){
            Intent intent=new Intent(aty,FollowUpActivity.class);
            intent.putExtra("flag", "limit_look");
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
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == 1013) {
            addTuiSongItem(netLimitBean,eventCenter);
        }

    }

    /***
     * 添加推送Item
     * @param limitBean
     * @param eventCenter
     */
    private void addTuiSongItem(LimitBean limitBean,EventCenter eventCenter){
        ReceiveMessageContent receiveMessageContent= (ReceiveMessageContent) eventCenter.getData();
        LimitDetailMsgBean limitDetailMsgBean = new LimitDetailMsgBean();
        limitDetailMsgBean.SEND_ACCOUNT = receiveMessageContent.getAccount();
        limitDetailMsgBean.CONTENT =receiveMessageContent.getContent();
        limitDetailMsgBean.SOURCE_ACCOUNT =receiveMessageContent.getSourceAccount();
        limitDetailMsgBean.CREATE_DATE = receiveMessageContent.getNowTime();

        if (receiveMessageContent.getPersonId().equals(limitBean.personId)&&
                receiveMessageContent.getCustomerName().equals(limitBean.customerName)&&
                receiveMessageContent.getUnionSecdCode().equals(limitBean.secendLevelLine)){
            adapter.addItem(limitDetailMsgBean);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollloy.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 600);
        }


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
        setContentView(R.layout.activity_common_imdetail);
    }

    private void getLimitDetailData(String messageFlag ){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
        params.putCommonTypeParam("customerName", "" + limitBean.customerName);
        params.putCommonTypeParam("secendLevelLineList", "" + limitBean.secendLevelLine);
        params.putCommonTypeParam("personId", "" + limitBean.personId);
        if (VstApplication.getInstance().getUserBean().getPosition()==2||VstApplication.getInstance().getUserBean().getPosition()==3||VstApplication.getInstance().getUserBean().getPosition()==4){
            params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());//都需要传递，当登录返回的position字段为2,3，4时，传递当前账号，其余情况传递消息列表返回的title的值
        }else{
            params.putCommonTypeParam("account", "" + title );
        }

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


                    }else {
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

    private void setTopViewData() {
        name_tv.setText(netLimitBean.customerName);
        second_line_tv.setText(netLimitBean.secendLevelLine);
        count_total_tv.setText(FormatUtil.limitFunFormat(netLimitBean.totalOverdueMoney));

        day01to15_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day1to15));
        day16to30_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day16to30));
        day31to60_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day31to60));
        day60up_tv.setText(FormatUtil.limitFunFormat(netLimitBean.day60up));

        String currentDay = DateTimeTool.getWeekOfDate(new Date(serverTime));
        if (VstApplication.getInstance().getUserBean().getPosition()==0) {//销售进来
            String week = "星期一,星期六,星期日";
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
        if (VstApplication.getInstance().getUserBean().getPosition()!=0){//领导问询
            titleBarHelper.setTitleMsg(TextUtils.isEmpty(title) ? "" : title);
            last_long_time_human_tv.setText(Html.fromHtml("<font color='#999999'>最长逾期天数：</font>" + netLimitBean.totalOverdueDay + "天<font color='#999999'> &nbsp&nbsp | &nbsp&nbsp 业务员：</font>" + netLimitBean.saleName));
            company_dept_tv.setVisibility(View.VISIBLE);
            deal_tv.setVisibility(View.VISIBLE);
            deal_des_tv.setVisibility(View.VISIBLE);
            clear_time_tv.setVisibility(View.VISIBLE);
            company_dept_tv.setText(Html.fromHtml("<font color='#999999'>区 &nbsp&nbsp 域  &nbsp&nbsp  ：</font>" + netLimitBean.branchCompany));
            dept_tv.setText(Html.fromHtml("<font color='#999999'>事   &nbsp&nbsp       业  &nbsp&nbsp&nbsp 部&nbsp&nbsp ：</font>" + netLimitBean.businessDepartment ));
            deal_tv.setText(Html.fromHtml((TextUtils.isEmpty(netLimitBean.solution) ? "暂未填写" : netLimitBean.solution)));
            clear_time_tv.setText(Html.fromHtml("<font color='#999999'>清理时间：</font>" + (TextUtils.isEmpty(netLimitBean.dealTime) ? "暂未填写" : netLimitBean.dealTime)));

            des_day01to15_tv.setText("0-15天");
            des_day16to30_tv.setText("16-30天");
            des_day31to60_tv.setText("31-60天");
            des_day60up_tv.setText("60天以上");

        }
        else  {//销售回复

            titleBarHelper.setTitleMsg(TextUtils.isEmpty(title)?"":title);
            last_long_time_human_tv.setText(Html.fromHtml("<font color='#999999'>最长逾期天数：</font>" + netLimitBean.totalOverdueDay + "天<font color='#999999'>&nbsp&nbsp | &nbsp&nbsp 业务员：</font>" + netLimitBean.saleName));
            company_dept_tv.setVisibility(View.GONE);
            dept_tv.setVisibility(View.GONE);
            deal_tv.setVisibility(View.GONE);
            deal_des_tv.setVisibility(View.GONE);
            clear_time_tv.setVisibility(View.GONE);
            devide_line.setVisibility(View.GONE);
            des_day01to15_tv.setText("0-15天");
            des_day16to30_tv.setText("16-30天");
            des_day31to60_tv.setText("31-60天");
            des_day60up_tv.setText("60天以上");
        }
    }

    //发送消息接口
    public void publishComments (String sourceAccount,String account){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
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
                if (response.rs!=null) {
                    LimitDetailMsgBean limitDetailMsgBean = new LimitDetailMsgBean();
                    limitDetailMsgBean.SEND_ACCOUNT = response.rs.account;
                    limitDetailMsgBean.CONTENT = response.rs.content;
                    limitDetailMsgBean.SOURCE_ACCOUNT = response.rs.sourceAccount;
                    limitDetailMsgBean.CREATE_DATE = response.rs.times;
                    adapter.addItem(limitDetailMsgBean);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scrollloy.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 600);
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

    private void setNullloy(){
        toggleShowEmpty(true, "暂无结果", null, R.mipmap.check_look_no_data_icon);
        titleBarHelper.setRightGone();
    }

    private void setSendLoy(){

        if (sendFlag.equals("0")) {
            send_msg_loy.setVisibility(View.GONE);
        } else {
            send_msg_loy.setVisibility(View.VISIBLE);
            send_msg_loy.setVisibility(View.VISIBLE);
            send_msg_et.setHint("回复" + title+":");
//            send_msg_et.setSelection(send_msg_et.getText().length());
            send_msg_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        limitDetailActivity.publishComments(getItem(position).SOURCE_ACCOUNT,VstApplication.getInstance().getUserBean().getAccount());
                    if (!TextUtils.isEmpty(getInputStr(send_msg_et)))
                    publishComments(title, VstApplication.getInstance().getUserBean().getAccount());
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollloy.fullScroll(ScrollView.FOCUS_DOWN);
//                        }
//                    });
                }
            });
        }
    }
}
