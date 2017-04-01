package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.arrears.adapter.WLimitTimeFollowAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.WLimitBean;
import com.vst.vstsupport.mode.bean.WLimitFollowSearchBean;
import com.vst.vstsupport.mode.bean.WLimitRsBean;
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
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:伟仕的超期跟进列表
 */
public class WLimitTimeFollowActivity extends BaseAct {


    public String operation ;//超期模块 判断是销售还是领导
    public long serverTime;
    private PullToRefreshListView kujungenjin_ptr_lv;
    private WLimitTimeFollowAdapter adapter;
    private ArrayList<WLimitBean> datas = new ArrayList<WLimitBean>();
    private WLimitFollowSearchBean searchBean = new WLimitFollowSearchBean();
    private boolean isSearch;
    private TitleBarHelper titleBarHelper;
    private TextView finish_time_tv;
    private String limitTime;

    @Override
    protected void onResume() {
        super.onResume();
        CURRENT_PAGE = 1;
        getLimitFollowListData(searchBean.brandName, searchBean.saleName, searchBean.customerName);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarHelper = new TitleBarHelper(this, R.string.common_empty, R.string.limit_time_expire, R.string.common_empty);
        titleBarHelper.setTitleMsg("超期跟进");
        titleBarHelper.setOnRightClickListener(this);
        kujungenjin_ptr_lv = (PullToRefreshListView) findViewById(R.id.kujungenjin_ptr_lv);
        finish_time_tv = (TextView) findViewById(R.id.finish_time_tv);
        adapter = new WLimitTimeFollowAdapter(aty);
        kujungenjin_ptr_lv.setAdapter(adapter);

        kujungenjin_ptr_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE = 1;
                getLimitFollowListData(searchBean.brandName, searchBean.saleName, searchBean.customerName);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE++;
                getLimitFollowListData(searchBean.brandName, searchBean.saleName, searchBean.customerName);
            }
        });

        kujungenjin_ptr_lv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPos = position - ((ListView) parent).getHeaderViewsCount();
                if (realPos >= 0 && realPos < adapter.getCount()) {
                    Intent intent = new Intent(aty, WLimitDetailActivity.class);
                    intent.putExtra("WLimitBean", adapter.getItem(realPos));
                    intent.putExtra("operation",operation);
                    showActivity(aty, intent);
                }
            }
        });

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
        searchBean = (WLimitFollowSearchBean) eventCenter.getData();
        getLimitFollowListData(searchBean.brandName, searchBean.saleName, searchBean.customerName);
        isSearch = true;
    }

    @Override
    protected View getContainerTargetView() {
        return kujungenjin_ptr_lv;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_jlimit_time_follow);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.left) {
            finish();
        } else if (v.getId() == R.id.right) {
            Intent intent = new Intent(aty, WLimitSearchActivity.class);
            intent.putExtra("flag", "limit_follow_w");
            showActivity(aty, intent);
        }
    }

    private void getLimitFollowListData(String brandName, String saleName, String customerName) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("currentPage", "" + CURRENT_PAGE);
        params.putCommonTypeParam("showCount", "" + PAGE_SIZE);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());

        if (!TextUtils.isEmpty(brandName))
            params.putCommonTypeParam("brandName", brandName);
        if (!TextUtils.isEmpty(saleName))
            params.putCommonTypeParam("saleName", saleName);
        if (!TextUtils.isEmpty(customerName))
            params.putCommonTypeParam("customerName", customerName);
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());//是否是vstuser


        PostRequest<RequestResult<WLimitRsBean>> request = new PostRequest<RequestResult<WLimitRsBean>>(UrlConstants.VSTOVERDUELIST, params, new OnNetSuccuss<RequestResult<WLimitRsBean>>() {
            @Override
            public void onSuccess(RequestResult<WLimitRsBean> response) {

                Log.i("vst", response.msg);
                if (response.rs != null) {
                    datas = response.rs.overDueList;
                    operation = response.rs.operation;
                    serverTime = response.rs.nowTime;
                    limitTime=response.rs.limitTime;
                    finish_time_tv.setText("截数日期 "+limitTime);
                    if (CURRENT_PAGE == 1) {
                        adapter.setGroup(datas);
                    } else {
                        adapter.addItems(datas);
                    }

                    changeRefreshMode(response.totalPage);

                }

                setNullLoy();
                kujungenjin_ptr_lv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        kujungenjin_ptr_lv.onRefreshComplete();
                        Log.d("cy", "qqqqqqqqqqqqq");
                    }
                }, 1000);
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception + "");
                kujungenjin_ptr_lv.onRefreshComplete();
                dismissLoadingDialog();
            }
        }, new TypeToken<RequestResult<WLimitRsBean>>() {
        }.getType());
        executeRequest(request);
    }


    private void changeRefreshMode(int totalPage) {
        if (totalPage == 0) {
            kujungenjin_ptr_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else if (CURRENT_PAGE >= totalPage) {
            kujungenjin_ptr_lv.onRefreshComplete();
            kujungenjin_ptr_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            kujungenjin_ptr_lv.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }


    private void setNullLoy() {

        if (adapter.getCount() <= 0) {
            finish_time_tv.setVisibility(View.GONE);
            if (isSearch) {
                toggleShowEmpty(true, "查不到相关数据", null, R.mipmap.search_no_data_icon, false);
            } else {
                toggleShowEmpty(true, "暂无数据", null, R.mipmap.check_look_no_data_icon, false);
                titleBarHelper.setRightGone();
            }

        }
        else {
            getVaryViewHelperController().restore();
        }
    }

}
