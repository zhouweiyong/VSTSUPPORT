package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.arrears.adapter.WLimitCheckReceiveAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.WLimitCheckReceiveBean;
import com.vst.vstsupport.mode.bean.WLimitCheckRsBean;
import com.vst.vstsupport.mode.bean.WLimitFollowSearchBean;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.netstatus.NetUtils;

import java.util.ArrayList;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:应收查看flag=limit,库存查看flag=inventroy
 */
public class WCheckReceiveActivity extends BaseAct {

    private PullToRefreshListView check_receive_lv;
    private WLimitCheckReceiveAdapter limitCheckReceiveAdapter;//应收查看适配器
    private ArrayList<WLimitCheckReceiveBean> receivableList = new ArrayList<WLimitCheckReceiveBean>();
    private ArrayList<WLimitCheckReceiveBean> receivableFiveDay = new ArrayList<WLimitCheckReceiveBean>();
    private WLimitFollowSearchBean limitFollowSearchBean = new WLimitFollowSearchBean();
    private boolean isSearch;
    private TitleBarHelper titleBarHelper;
    private TextView finish_time_tv;
    private String limitTime;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarHelper = new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty);
        titleBarHelper.setRightMsg("查询");
        titleBarHelper.setOnRightClickListener(this);
        check_receive_lv = (PullToRefreshListView) findViewById(R.id.check_receive_lv);
        finish_time_tv = (TextView) findViewById(R.id.finish_time_tv);
        titleBarHelper.setTitleMsg("应收查看");
        limitCheckReceiveAdapter = new WLimitCheckReceiveAdapter(this);
        check_receive_lv.setAdapter(limitCheckReceiveAdapter);
        getReceivableList(limitFollowSearchBean.brandName, limitFollowSearchBean.customerName, limitFollowSearchBean.saleName);

        check_receive_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE = 1;
                getReceivableList(limitFollowSearchBean.brandName, limitFollowSearchBean.customerName, limitFollowSearchBean.saleName);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE++;
                getReceivableList(limitFollowSearchBean.brandName, limitFollowSearchBean.customerName, limitFollowSearchBean.saleName);
            }
        });


    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.right) {
            Intent intent = new Intent();
            intent.setClass(aty, WLimitSearchActivity.class);
            intent.putExtra("flag", "limit_follow_w");
            showActivity(aty, intent);
        }
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

        limitFollowSearchBean = (WLimitFollowSearchBean) eventCenter.getData();
        getReceivableList(limitFollowSearchBean.brandName, limitFollowSearchBean.customerName, limitFollowSearchBean.saleName);
        isSearch = true;
    }

    @Override
    protected View getContainerTargetView() {
        return check_receive_lv;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_wcheck_receive);
    }

    //获取应收查看列表
    private void getReceivableList(String brandName, String customerName, String saleName) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("currentPage", "" + CURRENT_PAGE);
        params.putCommonTypeParam("showCount", "" + PAGE_SIZE);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());
        if (!TextUtils.isEmpty(brandName))
        params.putCommonTypeParam("brandName", "" + brandName);
        if (!TextUtils.isEmpty(saleName))
        params.putCommonTypeParam("saleName", saleName);
        if (!TextUtils.isEmpty(customerName))
        params.putCommonTypeParam("customerName", customerName);
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());
        PostRequest<RequestResult<WLimitCheckRsBean>> request = new PostRequest<RequestResult<WLimitCheckRsBean>>(UrlConstants.VSTRECEIVEABLELIST, params, new OnNetSuccuss<RequestResult<WLimitCheckRsBean>>() {
            @Override
            public void onSuccess(RequestResult<WLimitCheckRsBean> response) {
                Log.i("vst", response.msg);
                if (response.rs != null) {
                    receivableList = response.rs.receivableList;
                    receivableFiveDay = response.rs.receivableThreeDay;
                    ArrayList<WLimitCheckReceiveBean> temp=setFlag2List(receivableFiveDay);
                    limitTime=response.rs.limitTime;
                    finish_time_tv.setText("截数日期 "+limitTime);
                    if (CURRENT_PAGE == 1) {
                        if (receivableFiveDay != null) {
                            limitCheckReceiveAdapter.setGroup(temp);
                            limitCheckReceiveAdapter.addItems(receivableList);
                        } else {
                            limitCheckReceiveAdapter.setGroup(receivableList);
                        }

                    } else {
                        if (receivableFiveDay != null)
                            limitCheckReceiveAdapter.addItems(temp);
                        limitCheckReceiveAdapter.addItems(receivableList);
                    }

                    changeRefreshMode(response.totalPage);

                }
                setNullLoy(limitCheckReceiveAdapter);
                dismissLoadingDialog();
                check_receive_lv.onRefreshComplete();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception + "");
                dismissLoadingDialog();
                check_receive_lv.onRefreshComplete();
                showErrorView();
            }
        }, new TypeToken<RequestResult<WLimitCheckRsBean>>() {
        }.getType());
        executeRequest(request);
    }

    private ArrayList<WLimitCheckReceiveBean> setFlag2List(ArrayList<WLimitCheckReceiveBean> receivableFiveDay) {
        for (int i=0;i<receivableFiveDay.size();i++){
            receivableFiveDay.get(i).flag="1";
        }
        return receivableFiveDay;
    }


    private void changeRefreshMode(int totalPage) {
        if (totalPage == 0) {
            check_receive_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else if (CURRENT_PAGE >= totalPage) {
            check_receive_lv.onRefreshComplete();
            check_receive_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            check_receive_lv.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }


    private void setNullLoy(BaseAdapter adapter) {

        if (adapter.getCount() <= 0) {
            finish_time_tv.setVisibility(View.GONE);
            if (isSearch) {
                toggleShowEmpty(true, "查不到相关数据", null, R.mipmap.search_no_data_icon, false);
            } else {
                toggleShowEmpty(true, "暂无数据", null, R.mipmap.check_look_no_data_icon, false);
                titleBarHelper.setRightGone();
            }

        }
    }

}
