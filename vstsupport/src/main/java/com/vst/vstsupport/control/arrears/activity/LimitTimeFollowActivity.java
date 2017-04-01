package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.arrears.adapter.LimitTimeFollowAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.LimitBean;
import com.vst.vstsupport.mode.bean.LimitFollowSearchBean;
import com.vst.vstsupport.mode.bean.LimitRsBean;
import com.vst.vstsupport.utils.PreferHelper;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.netstatus.NetUtils;

import java.util.ArrayList;

public class LimitTimeFollowActivity extends BaseAct {


    public String operation ;//超期模块 判断是销售还是领导
    public long serverTime;
    private PullToRefreshListView kujungenjin_ptr_lv;
    private LimitTimeFollowAdapter adapter;
    private ArrayList<LimitBean> datas = new ArrayList<LimitBean>();
    private LimitFollowSearchBean searchBean = new LimitFollowSearchBean();
    private boolean isSearch;
    private TitleBarHelper titleBarHelper;

    @Override
    protected void onResume() {
        super.onResume();
        CURRENT_PAGE = 1;
        getLimitFollowListData(searchBean.businessDepartment, searchBean.areaName, searchBean.stratDate, searchBean.endDate, searchBean.secendLevelLine, searchBean.saleName, searchBean.customerName);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarHelper = new TitleBarHelper(this, R.string.common_empty, R.string.limit_time_expire, R.string.common_empty);
        titleBarHelper.setTitleMsg("超期跟进");
        titleBarHelper.setOnRightClickListener(this);
        kujungenjin_ptr_lv = (PullToRefreshListView) findViewById(R.id.kujungenjin_ptr_lv);
        adapter = new LimitTimeFollowAdapter(aty);
        kujungenjin_ptr_lv.setAdapter(adapter);

        kujungenjin_ptr_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE = 1;
                getLimitFollowListData(searchBean.businessDepartment, searchBean.areaName, searchBean.stratDate, searchBean.endDate, searchBean.secendLevelLine, searchBean.saleName, searchBean.customerName);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE++;
                getLimitFollowListData(searchBean.businessDepartment, searchBean.areaName, searchBean.stratDate, searchBean.endDate, searchBean.secendLevelLine, searchBean.saleName, searchBean.customerName);
            }
        });

        kujungenjin_ptr_lv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPos = position - ((ListView) parent).getHeaderViewsCount();
                if (realPos >= 0 && realPos < adapter.getCount()) {
                    Intent intent = new Intent(aty, LimitDetailActivity.class);
                    intent.putExtra("LimitBean", adapter.getItem(realPos));
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
        searchBean = (LimitFollowSearchBean) eventCenter.getData();
        CURRENT_PAGE=1;
        getLimitFollowListData(searchBean.businessDepartment, searchBean.areaName, searchBean.stratDate, searchBean.endDate, searchBean.secendLevelLine, searchBean.saleName, searchBean.customerName);
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
        setContentView(R.layout.activity_limit_time_follow);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.left) {
            finish();
        } else if (v.getId() == R.id.right) {
            Intent intent = new Intent(aty, LimitSerachActivity.class);
            intent.putExtra("flag", "limit_follow");
            intent.putExtra("operation",operation);
            showActivity(aty, intent);
        }
    }

    private void getLimitFollowListData(String businessDepartment, String areaName, String stratDate, String endDate, String secendLevelLine, String saleName, String customerName) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("currentPage", "" + CURRENT_PAGE);
        params.putCommonTypeParam("showCount", "" + PAGE_SIZE);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());

        if (!TextUtils.isEmpty(businessDepartment))
            params.putCommonTypeParam("businessDepartment", businessDepartment);
        if (!TextUtils.isEmpty(areaName))
            params.putCommonTypeParam("areaName", areaName);
        if (!TextUtils.isEmpty(stratDate))
            params.putCommonTypeParam("stratDate", stratDate);
        if (!TextUtils.isEmpty(endDate))
            params.putCommonTypeParam("endDate", endDate);
        if (!TextUtils.isEmpty(secendLevelLine))
            params.putCommonTypeParam("secendLevelLine", secendLevelLine);
        if (!TextUtils.isEmpty(saleName))
            params.putCommonTypeParam("saleName", saleName);
        if (!TextUtils.isEmpty(customerName))
            params.putCommonTypeParam("customerName", customerName);
        params.putCommonTypeParam("symbol", "" + VstApplication.getInstance().getUserBean().getSymbol());//账户编码
        params.putCommonTypeParam("isvstuser", "" + VstApplication.getInstance().getUserBean().getIsvstuser());//是否是vstuser
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());//是否是vstuser


        PostRequest<RequestResult<LimitRsBean>> request = new PostRequest<RequestResult<LimitRsBean>>(UrlConstants.OVERDUELIST, params, new OnNetSuccuss<RequestResult<LimitRsBean>>() {
            @Override
            public void onSuccess(RequestResult<LimitRsBean> response) {

                Log.i("vst", response.msg);
                if (response.rs != null) {
                    LimitRsBean limitRsBean = response.rs;
                    datas = response.rs.overdueList;
                    operation = response.rs.operation;
                    serverTime = response.rs.nowTime;
                    removeSp();
                    if (response.rs.businessDepartmentList != null) {
                        VstApplication.getInstance().setDepartmentBeans(response.rs.businessDepartmentList);
                    }

                    if (!TextUtils.isEmpty(response.rs.secendLevelLineList)) {

                        PreferHelper.getInstance().setString("secendLevelLineList", response.rs.secendLevelLineList);
                    }
                    if (!TextUtils.isEmpty(response.rs.areaList)) {

                        PreferHelper.getInstance().setString("areaList", response.rs.areaList);
                    }
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
        }, new TypeToken<RequestResult<LimitRsBean>>() {
        }.getType());
        executeRequest(request);
    }

    private void removeSp() {
        if (!TextUtils.isEmpty(PreferHelper.getInstance().getString("businessDepartmentList"))) {
            PreferHelper.getInstance().remove("businessDepartmentList");
        }
        if (!TextUtils.isEmpty(PreferHelper.getInstance().getString("secendLevelLineList"))) {
            PreferHelper.getInstance().remove("secendLevelLineList");
        }
        if (!TextUtils.isEmpty(PreferHelper.getInstance().getString("areaList"))) {
            PreferHelper.getInstance().remove("areaList");
        }
        if (VstApplication.getInstance().getDepartmentBeans() != null) {
            VstApplication.getInstance().getDepartmentBeans().clear();
        }
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
