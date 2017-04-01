package com.vst.vstsupport.control.message.activity;

import android.content.Intent;
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
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.inventory.adapter.InventoryDetailMsgAdapter;
import com.vst.vstsupport.mode.bean.InventoryBean;
import com.vst.vstsupport.mode.bean.InventoryDetailBean;
import com.vst.vstsupport.mode.bean.InventoryDetailMsgBean;
import com.vst.vstsupport.mode.bean.InventoryFlowSituation;
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
 * Description:消息详情，领导问询-库存，产品回复-库存
 */
public class InventoryMsgIMDetailActivity extends BaseAct {

    public RelativeLayout send_msg_loy;
    public EditText send_msg_et;
    public TextView send_msg_tv;
//    private String flag;
    public String sendFlag="";//超期详情里面能否发送消息 1能 0 否 null:消息列表进入
    public String pmAccount;//产品经理账号
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private TextView name_tv;
    private TextView has_calculate_tv;
    private TextView second_line_tv;
    private TextView count_total_tv;
    private TextView last_long_time_human_tv;
    private TextView company_dept_tv;
    private TextView dept_tv;
    private TextView deal_tv;
    private TextView deal_des_tv;
    private TextView clear_time_tv;
    private ImageView open_close_iv;
    private LinearLayout common_item_open_close_layout;
    private RelativeLayout genloy;
    private TextView des_day01to15_tv;
    private TextView des_day16to30_tv;
    private TextView des_day31to60_tv;
    private TextView des_day60up_tv;
    private TextView day01to15_tv;
    private TextView day16to30_tv;
    private TextView day31to60_tv;
    private TextView day60up_tv;
    private TextView count_total_des_tv;
    private InventoryFlowSituation flowSituation;
    private TitleBarHelper titleBarHelper;
    private ListViewForScrollView comunicate_lv;
    private InventoryDetailMsgAdapter adapter;
    private boolean isOpen;
    private ScrollView scrollloy;
    private InventoryBean netInventoryBean = new InventoryBean();
    private InventoryBean inventoryBean = new InventoryBean();
    private ArrayList<InventoryDetailMsgBean> msgDatas = new ArrayList<InventoryDetailMsgBean>();
    private String title;
    private RelativeLayout open_close_loy;
    public long serverTime;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
//        flag=getIntent().getStringExtra("flag");
        inventoryBean = (InventoryBean) getIntent().getSerializableExtra("InventoryBean");
        title=getIntent().getStringExtra("title");
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
        has_calculate_tv.setVisibility(View.GONE);
        company_dept_tv = (TextView) findViewById(R.id.company_dept_tv);
        dept_tv = (TextView) findViewById(R.id.dept_tv);
        deal_tv = (TextView) findViewById(R.id.deal_tv);
        deal_des_tv = (TextView) findViewById(R.id.deal_des_tv);
        clear_time_tv = (TextView) findViewById(R.id.clear_time_tv);
        last_long_time_human_tv = (TextView) findViewById(R.id.last_long_time_human_tv);
        des_day01to15_tv = (TextView) findViewById(R.id.des_day0to60_tv);
        des_day16to30_tv = (TextView) findViewById(R.id.des_day61to90_tv);
        des_day31to60_tv = (TextView) findViewById(R.id.des_day91to120_tv);
        des_day60up_tv = (TextView) findViewById(R.id.des_day120up_tv);
        name_tv = (TextView) findViewById(R.id.name_tv);
        second_line_tv = (TextView) findViewById(R.id.second_line_tv);
        count_total_tv = (TextView) findViewById(R.id.count_total_tv);
        open_close_iv = (ImageView) findViewById(R.id.open_close_iv);
        open_close_loy = (RelativeLayout) findViewById(R.id.open_close_loy);
        common_item_open_close_layout = (LinearLayout) findViewById(R.id.common_item_open_close_layout);
        scrollloy = (ScrollView) findViewById(R.id.scrollloy);
        scrollloy.smoothScrollTo(0, 0);
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

        send_msg_loy = (RelativeLayout) findViewById(R.id.send_msg_loy);
        send_msg_et = (EditText) findViewById(R.id.send_msg_et);
        send_msg_tv = (TextView) findViewById(R.id.send_msg_tv);
        getInventoryDetailData("1");
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId()==R.id.left){
            finish();
        }else if(v.getId()==R.id.right){
            Intent intent = new Intent(aty, FollowUpActivity.class);
            intent.putExtra("flag", "inventroy");
            intent.putExtra("inventoryBean", inventoryBean);
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
//            ReceiveMessageContent receiveMessageContent= (ReceiveMessageContent) eventCenter.getData();
//            InventoryDetailMsgBean inventoryDetailMsgBean = new InventoryDetailMsgBean();
//            inventoryDetailMsgBean.sendAccount =receiveMessageContent.getAccount();
//            inventoryDetailMsgBean.content = receiveMessageContent.getContent();
//            inventoryDetailMsgBean.sourceAccount = receiveMessageContent.getSourceAccount();
//            inventoryDetailMsgBean.createDate =receiveMessageContent.getNowTime() ;
//            adapter.addItem(inventoryDetailMsgBean);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    scrollloy.fullScroll(ScrollView.FOCUS_DOWN);
//                }
//            }, 600);
            addTuiSongItem(netInventoryBean,eventCenter);
        }

    }

    /***
     * 添加推送Item
     * @param netInventoryBean
     * @param eventCenter
     */
    private void addTuiSongItem(InventoryBean netInventoryBean,EventCenter eventCenter){
        ReceiveMessageContent receiveMessageContent= (ReceiveMessageContent) eventCenter.getData();
        InventoryDetailMsgBean inventoryDetailMsgBean = new InventoryDetailMsgBean();
        inventoryDetailMsgBean.sendAccount = receiveMessageContent.getAccount();
        inventoryDetailMsgBean.content =receiveMessageContent.getContent();
        inventoryDetailMsgBean.sourceAccount =receiveMessageContent.getSourceAccount();
        inventoryDetailMsgBean.createDate = receiveMessageContent.getNowTime();

        if (receiveMessageContent.getPersonId().equals(netInventoryBean.businessDepartment)&&
                receiveMessageContent.getCustomerName().equals(netInventoryBean.projectSerial)&&
                receiveMessageContent.getUnionSecdCode().equals(netInventoryBean.secendLevelLine)){
            adapter.addItem(inventoryDetailMsgBean);
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
        setContentView(R.layout.activity_inventory_msg_imdetail);
    }

    private void getInventoryDetailData(String messageFlag) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("secendLevelLine", "" + inventoryBean.secendLevelLine);
        params.putCommonTypeParam("type", "" + inventoryBean.type);
        params.putCommonTypeParam("projectSerial", "" + inventoryBean.projectSerial);
//        params.putCommonTypeParam("projectName", "" + inventoryBean.projectName);
        params.putCommonTypeParam("businessDepartment", "" + inventoryBean.businessDepartment);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());
        params.putCommonTypeParam("position", "" + VstApplication.getInstance().getUserBean().getPosition());
        params.putCommonTypeParam("messageFlag", "" + messageFlag);//0：表示积压列表进入，1：表示消息列表进入 	发送消息的标志，从消息列表跳入需要，区分产品经理能否页面回复消息
        PostRequest<RequestResult<InventoryDetailBean>> request = new PostRequest<RequestResult<InventoryDetailBean>>(UrlConstants.INTOINVENTORYDETAIL, params, new OnNetSuccuss<RequestResult<InventoryDetailBean>>() {
            @Override
            public void onSuccess(RequestResult<InventoryDetailBean> response) {
                Log.i("vst", response.msg);
                if (response.rs != null) {
                    netInventoryBean = response.rs.inventoryData;
                    if (netInventoryBean!=null&&netInventoryBean.pmAccount!=null){
                        pmAccount=netInventoryBean.pmAccount;
                        adapter=new InventoryDetailMsgAdapter(InventoryMsgIMDetailActivity.this,pmAccount);
                        comunicate_lv.setAdapter(adapter);
                        msgDatas = response.rs.inventoryMessage;
                        sendFlag = response.rs.sendFlag;
                        serverTime=response.rs.nowTime;
                        setSendLoy();
                        flowSituation = response.rs.flowSituation;
                        setTopViewData();
                        adapter.setGroup(msgDatas);
                        genloy.setVisibility(View.VISIBLE);
                    }else{
                        setNullloy();
                    }
                }else {
                    setNullloy();
                }
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception + "");
                showErrorView();
                dismissLoadingDialog();
            }
        }, new TypeToken<RequestResult<InventoryDetailBean>>() {
        }.getType());
        executeRequest(request);
    }

    private void setNullloy(){
        toggleShowEmpty(true, "暂无结果", null, R.mipmap.check_look_no_data_icon);
        titleBarHelper.setRightGone();
    }

    private void setTopViewData() {
        titleBarHelper.setTitleMsg("" + title);
        company_dept_tv.setVisibility(View.GONE);
        dept_tv.setVisibility(View.GONE);
        deal_tv.setVisibility(View.GONE);
        deal_des_tv.setVisibility(View.GONE);
        clear_time_tv.setVisibility(View.GONE);
        count_total_des_tv.setText("在库金额：");
//        des_day01to15_tv.setText("0-60天");
//        des_day16to30_tv.setText("61-90天");
        des_day31to60_tv.setText("60-120天");
        des_day60up_tv.setText("120天以上");
        name_tv.setText(netInventoryBean.projectSerial);
        second_line_tv.setText(netInventoryBean.secendLevelLine);
        count_total_tv.setText(FormatUtil.inventoryFunFormat(netInventoryBean.totalMoney));
//        day01to15_tv.setText((TextUtils.isEmpty(netInventoryBean.day0to60) ? "----" : "￥" +MoneyTool.commaSsymbolFormat( netInventoryBean.day0to60)));
//        day16to30_tv.setText((TextUtils.isEmpty(netInventoryBean.day61to90) ? "----" : "￥" +MoneyTool.commaSsymbolFormat( netInventoryBean.day61to90)));
        day31to60_tv.setText(FormatUtil.inventoryFunFormat(netInventoryBean.day60to120));
        day60up_tv.setText((FormatUtil.inventoryFunFormat(netInventoryBean.day120up)));
        String currentDay = DateTimeTool.getWeekOfDate(new Date(serverTime));
        if (VstApplication.getInstance().getUserBean().getPosition()==1) {//产品经理进来
            String week = "星期五,星期六,星期日";
            if(week.contains(currentDay)){
                if(TextUtils.isEmpty(flowSituation.solution)){
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
        last_long_time_human_tv.setText(Html.fromHtml("<font color='#999999'>最长在库天数：</font>" + netInventoryBean.totalMoney + "天<font color='#999999'> &nbsp&nbsp| &nbsp&nbsp 产品经理：</font>" + netInventoryBean.pmName));
    }

    //发送消息接口
    public void publishComments(String sourceAccount, String account) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();

        if (inventoryBean.type.equals("流量")) {
            params.putCommonTypeParam("commentsType", "3");//评论类型（1：应收 2:库存项目类型跟进 3:库存流量类型跟进）
        } else {
            params.putCommonTypeParam("commentsType", "2");//评论类型（1：应收 2:库存项目类型跟进 3:库存流量类型跟进）
        }
        params.putCommonTypeParam("customerName", "" + inventoryBean.projectSerial);
        params.putCommonTypeParam("unionSecdCode", "" + inventoryBean.secendLevelLine);
        params.putCommonTypeParam("sourceAccount", "" + sourceAccount);//接受者账号
        params.putCommonTypeParam("personId", "" + inventoryBean.businessDepartment);//销售人员BPS ID(库存积压类型时，次字段代表事业部名称)
        params.putCommonTypeParam("content", "" + getInputStr(send_msg_et));
        params.putCommonTypeParam("account", "" + account);//发送者账号
        params.putCommonTypeParam("isvstuser", "" + VstApplication.getInstance().getUserBean().getIsvstuser());//是否伟仕用户 0-不是 1-是
//        params.putCommonTypeParam("productName", ""+inventoryBean.productName );//产品名称(应收时传空)
        PostRequest<RequestResult<PublishCommentBean>> request = new PostRequest<RequestResult<PublishCommentBean>>(UrlConstants.PUBLISHCOMMENTS, params, new OnNetSuccuss<RequestResult<PublishCommentBean>>() {
            @Override
            public void onSuccess(RequestResult<PublishCommentBean> response) {
                Log.i("vst", response.msg);
//                showToastMsg(response.msg);
                if (response.rs!=null) {
                    InventoryDetailMsgBean inventoryDetailMsgBean = new InventoryDetailMsgBean();
                    inventoryDetailMsgBean.sendAccount = response.rs.account;
                    inventoryDetailMsgBean.content = response.rs.content;
                    inventoryDetailMsgBean.sourceAccount = response.rs.sourceAccount;
                    inventoryDetailMsgBean.createDate = response.rs.times;
                    adapter.addItem(inventoryDetailMsgBean);
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

