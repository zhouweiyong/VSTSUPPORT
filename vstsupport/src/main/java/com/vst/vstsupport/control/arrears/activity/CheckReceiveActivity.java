package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.arrears.adapter.CheckReceiveAdapter;
import com.vst.vstsupport.control.arrears.adapter.LimitCheckReceiveAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.inventory.activity.InventorySearchActivity;
import com.vst.vstsupport.mode.bean.InventoryLookSearchBean;
import com.vst.vstsupport.mode.bean.InventoryLookUpBean;
import com.vst.vstsupport.mode.bean.InventoryLookUpRsBean;
import com.vst.vstsupport.mode.bean.LimitCheckReceiveBean;
import com.vst.vstsupport.mode.bean.LimitCheckRsBean;
import com.vst.vstsupport.mode.bean.LimitFollowSearchBean;
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

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:应收查看flag=limit,库存查看flag=inventroy
 */
public class CheckReceiveActivity extends BaseAct {

    public String flag;
    private PullToRefreshListView check_receive_lv;
    private CheckReceiveAdapter adapter;//库存查看适配器
    private LimitCheckReceiveAdapter limitCheckReceiveAdapter;//应收查看适配器
    private ArrayList<LimitCheckReceiveBean> receivableList = new ArrayList<LimitCheckReceiveBean>();
    private ArrayList<LimitCheckReceiveBean> receivableFiveDay = new ArrayList<LimitCheckReceiveBean>();
    private ArrayList<InventoryLookUpBean> inventoryDatas = new ArrayList<InventoryLookUpBean>();
    private InventoryLookSearchBean inventoryLookSearchBean = new InventoryLookSearchBean();
    private LimitFollowSearchBean limitFollowSearchBean = new LimitFollowSearchBean();
    private boolean isSearch;
    private TitleBarHelper titleBarHelper;
    public String operation;
    private LinearLayout sort_ll;
    private TextView sort_by_num_tv;
    private TextView sort_by_day_tv;
    private ImageView sort_by_num_iv;
    private ImageView sort_by_day_iv;
    private RelativeLayout sort_by_num_rl;
    private RelativeLayout sort_by_day_rl;
    private boolean isNumAsend;//true 升序 false 降序
    private boolean isDayAsend;//true 升序 false 降序
    private int selectColorId = Color.parseColor("#e94415");
    private int unselesctColorId = Color.parseColor("#646464");

    private static final int SELECT_NUM = 0;
    private static final int SELECT_DAY = 1;
    private int lastSelect = SELECT_NUM;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        flag = getIntent().getStringExtra("flag");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarHelper = new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty);
        titleBarHelper.setRightMsg("查询");
        titleBarHelper.setOnRightClickListener(this);

        check_receive_lv = (PullToRefreshListView) findViewById(R.id.check_receive_lv);
        sort_ll = (LinearLayout) findViewById(R.id.sort_ll);
        //初始化排序
        inventoryLookSearchBean.orderBy = orderBy.moneyDown + "";
        if (flag.equals("limit")) {
            titleBarHelper.setTitleMsg("应收查看");
            limitCheckReceiveAdapter = new LimitCheckReceiveAdapter(this);
            check_receive_lv.setAdapter(limitCheckReceiveAdapter);
            getReceivableList(limitFollowSearchBean.businessDepartment, limitFollowSearchBean.areaName, limitFollowSearchBean.stratDate, limitFollowSearchBean.endDate, limitFollowSearchBean.secendLevelLine, limitFollowSearchBean.saleName, limitFollowSearchBean.customerName);
        } else {
            titleBarHelper.setTitleMsg("库存查看");
            adapter = new CheckReceiveAdapter(this);
            check_receive_lv.setAdapter(adapter);
            sort_ll.setVisibility(View.VISIBLE);
            setSort_llView();
            getInventoryList(inventoryLookSearchBean.businessDepartment, inventoryLookSearchBean.secendLevelLine, inventoryLookSearchBean.productName, inventoryLookSearchBean.startDate, inventoryLookSearchBean.endDate, orderBy.moneyDown + "");
        }

        check_receive_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE = 1;
                if (flag.equals("limit")) {
                    getReceivableList(limitFollowSearchBean.businessDepartment, limitFollowSearchBean.areaName, limitFollowSearchBean.stratDate, limitFollowSearchBean.endDate, limitFollowSearchBean.secendLevelLine, limitFollowSearchBean.saleName, limitFollowSearchBean.customerName);
                } else {
                    getInventoryList(inventoryLookSearchBean.businessDepartment, inventoryLookSearchBean.secendLevelLine, inventoryLookSearchBean.productName, inventoryLookSearchBean.startDate, inventoryLookSearchBean.endDate, inventoryLookSearchBean.orderBy);
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE++;
                if (flag.equals("limit")) {
                    getReceivableList(limitFollowSearchBean.businessDepartment, limitFollowSearchBean.areaName, limitFollowSearchBean.stratDate, limitFollowSearchBean.endDate, limitFollowSearchBean.secendLevelLine, limitFollowSearchBean.saleName, limitFollowSearchBean.customerName);
                } else {
                    getInventoryList(inventoryLookSearchBean.businessDepartment, inventoryLookSearchBean.secendLevelLine, inventoryLookSearchBean.productName, inventoryLookSearchBean.startDate, inventoryLookSearchBean.endDate, inventoryLookSearchBean.orderBy);
                }
            }
        });


    }

    private void setSort_llView() {
        sort_by_num_tv = (TextView) sort_ll.findViewById(R.id.sort_by_num_tv);
        sort_by_day_tv = (TextView) sort_ll.findViewById(R.id.sort_by_day_tv);

        sort_by_day_iv = (ImageView) findViewById(R.id.sort_by_day_iv);
        sort_by_num_iv = (ImageView) findViewById(R.id.sort_by_num_iv);
        sort_by_day_rl = (RelativeLayout) findViewById(R.id.sort_by_day_rl);
        sort_by_num_rl = (RelativeLayout) findViewById(R.id.sort_by_num_rl);

        sort_by_num_iv.setOnClickListener(this);
        sort_by_day_iv.setOnClickListener(this);
        sort_by_num_tv.setOnClickListener(this);
        sort_by_day_tv.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.right) {
            Intent intent = new Intent();
            if (flag.equals("limit")) {
                intent.setClass(aty, LimitSerachActivity.class);
                intent.putExtra("flag", "limit_look");
                intent.putExtra("operation", operation);
            } else {
                intent.setClass(aty, InventorySearchActivity.class);
                intent.putExtra("flag", "inventory_look");
            }

            showActivity(aty, intent);
        } else if (v.getId() == R.id.sort_by_day_iv || v.getId() == R.id.sort_by_day_tv) {
            if (lastSelect == SELECT_NUM) {
                isDayAsend = false;
                lastSelect = SELECT_DAY;
            }
            sort_by_num_tv.setTextColor(unselesctColorId);
            sort_by_day_tv.setTextColor(selectColorId);
            sort_by_num_iv.setImageResource(R.mipmap.default_unselect_sort_icon);

            if (isDayAsend) {
                sort_by_day_iv.setImageResource(R.mipmap.default_ascend_sort_icon);
                isDayAsend = false;
                inventoryLookSearchBean.orderBy = orderBy.daysUp + "";
            } else {
                sort_by_day_iv.setImageResource(R.mipmap.default_decend_sort_icon);
                isDayAsend = true;
                inventoryLookSearchBean.orderBy = orderBy.daysDown + "";
            }
            CURRENT_PAGE = 1;
            getInventoryList(inventoryLookSearchBean.businessDepartment, inventoryLookSearchBean.secendLevelLine, inventoryLookSearchBean.productName, inventoryLookSearchBean.startDate, inventoryLookSearchBean.endDate, inventoryLookSearchBean.orderBy);


        } else if (v.getId() == R.id.sort_by_num_iv || v.getId() == R.id.sort_by_num_tv) {
            if (lastSelect == SELECT_DAY) {
                lastSelect = SELECT_NUM;
                isNumAsend = true;
            }
            sort_by_num_tv.setTextColor(selectColorId);
            sort_by_day_tv.setTextColor(unselesctColorId);
            sort_by_day_iv.setImageResource(R.mipmap.default_unselect_sort_icon);
            if (!isNumAsend) {
                sort_by_num_iv.setImageResource(R.mipmap.default_ascend_sort_icon);
                isNumAsend = true;
                inventoryLookSearchBean.orderBy = orderBy.moneyUp + "";
            } else {
                sort_by_num_iv.setImageResource(R.mipmap.default_decend_sort_icon);
                isNumAsend = false;
                inventoryLookSearchBean.orderBy = orderBy.moneyDown + "";
            }
            CURRENT_PAGE = 1;
            getInventoryList(inventoryLookSearchBean.businessDepartment, inventoryLookSearchBean.secendLevelLine, inventoryLookSearchBean.productName, inventoryLookSearchBean.startDate, inventoryLookSearchBean.endDate, inventoryLookSearchBean.orderBy);

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
        CURRENT_PAGE = 1;
        if (!flag.equals("limit")) {
            inventoryLookSearchBean = (InventoryLookSearchBean) eventCenter.getData();
            getInventoryList(inventoryLookSearchBean.businessDepartment, inventoryLookSearchBean.secendLevelLine, inventoryLookSearchBean.productName, inventoryLookSearchBean.startDate, inventoryLookSearchBean.endDate, inventoryLookSearchBean.orderBy);
            isSearch = true;
        } else {
            limitFollowSearchBean = (LimitFollowSearchBean) eventCenter.getData();
            getReceivableList(limitFollowSearchBean.businessDepartment, limitFollowSearchBean.areaName, limitFollowSearchBean.stratDate, limitFollowSearchBean.endDate, limitFollowSearchBean.secendLevelLine, limitFollowSearchBean.saleName, limitFollowSearchBean.customerName);
            isSearch = true;
        }
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
        setContentView(R.layout.activity_check_receive);
    }

    //获取应收查看列表
    private void getReceivableList(String businessDepartment, String areaName, String stratDate, String endDate, String secendLevelLine, String saleName, String customerName) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("currentPage", "" + CURRENT_PAGE);
        params.putCommonTypeParam("showCount", "" + PAGE_SIZE);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());
//        params.putCommonTypeParam("secendLevelLine", "" + secendLevelLine);

        if (!TextUtils.isEmpty(businessDepartment))
            params.putCommonTypeParam("businessDepartment", businessDepartment);
        if (!TextUtils.isEmpty(areaName))
            params.putCommonTypeParam("areaName", areaName);
        if (!TextUtils.isEmpty(stratDate))
            params.putCommonTypeParam("startDate", stratDate);
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
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());
        PostRequest<RequestResult<LimitCheckRsBean>> request = new PostRequest<RequestResult<LimitCheckRsBean>>(UrlConstants.RECEIVABLELIST, params, new OnNetSuccuss<RequestResult<LimitCheckRsBean>>() {
            @Override
            public void onSuccess(RequestResult<LimitCheckRsBean> response) {
                Log.i("vst", response.msg);
                if (response.rs != null) {
                    removeSp();
                    receivableList = response.rs.receivableList;
                    receivableFiveDay = response.rs.receivableFiveDay;
                    operation = response.rs.operation;
//                    if (!TextUtils.isEmpty(response.rs.secendLevelLineList)) {
//                        PreferHelper.getInstance().setString("secendLevelLineList", response.rs.secendLevelLineList);
//                    }

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
                        if (receivableFiveDay != null) {
                            limitCheckReceiveAdapter.setGroup(receivableFiveDay);
                            limitCheckReceiveAdapter.addItems(receivableList);
                        } else {
                            limitCheckReceiveAdapter.setGroup(receivableList);
                        }

                    } else {
                        if (receivableFiveDay != null)
                            limitCheckReceiveAdapter.addItems(receivableFiveDay);
                        limitCheckReceiveAdapter.addItems(receivableList);
                    }

//                    changeLimitRefreshMode(response.totalPage);
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
        }, new TypeToken<RequestResult<LimitCheckRsBean>>() {
        }.getType());
        executeRequest(request);
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

    private void changeLimitRefreshMode(int totalSize) {
        if (totalSize == 0) {
            check_receive_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else if (limitCheckReceiveAdapter.getCount() >= totalSize) {
            check_receive_lv.onRefreshComplete();
            check_receive_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        } else {
            check_receive_lv.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }

    //获取库存查看列表
    private void getInventoryList(String businessDepartment, String secendLevelLine, String productName, String stratDate, String endDate, String orderBy) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("currentPage", "" + CURRENT_PAGE);
        params.putCommonTypeParam("showCount", "" + PAGE_SIZE);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());
        params.putCommonTypeParam("position", "" + VstApplication.getInstance().getUserBean().getPosition());
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());
        if (!TextUtils.isEmpty(businessDepartment))
            params.putCommonTypeParam("businessDepartment", businessDepartment);
        if (!TextUtils.isEmpty(secendLevelLine))
            params.putCommonTypeParam("secendLevelLine", secendLevelLine);
        if (!TextUtils.isEmpty(productName))
            params.putCommonTypeParam("productName", productName);
        if (!TextUtils.isEmpty(stratDate))
            params.putCommonTypeParam("startDate", stratDate);
        if (!TextUtils.isEmpty(endDate))
            params.putCommonTypeParam("endDate", endDate);
        if (!TextUtils.isEmpty(orderBy))
            params.putCommonTypeParam("orderBy", orderBy);
        PostRequest<RequestResult<InventoryLookUpRsBean>> request = new PostRequest<RequestResult<InventoryLookUpRsBean>>(UrlConstants.GETINVENTORYLIST, params, new OnNetSuccuss<RequestResult<InventoryLookUpRsBean>>() {
            @Override
            public void onSuccess(RequestResult<InventoryLookUpRsBean> response) {

                Log.i("vst", response.msg);
                if (response.rs != null) {
                    removeSp();
                    inventoryDatas = response.rs.inventoryList;
                    if (!TextUtils.isEmpty(response.rs.secendLevelLineSring)) {
                        PreferHelper.getInstance().setString("secendLevelLineList", response.rs.secendLevelLineSring);
                    }
                    if (CURRENT_PAGE == 1) {
                        adapter.setGroup(inventoryDatas);
                    } else {
                        adapter.addItems(inventoryDatas);
                    }

                    changeRefreshMode(response.totalPage);

                }

                setNullLoy(adapter);
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
        }, new TypeToken<RequestResult<InventoryLookUpRsBean>>() {
        }.getType());
        executeRequest(request);
    }

    public enum orderBy {
        moneyUp,//:金额升序


        moneyDown,//: 金额降序


        daysUp,//: 在库天数升序


        daysDown //: 在库天数降序
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

    }

    private void setNullLoy(BaseAdapter adapter) {

        if (adapter.getCount() <= 0) {

            if (isSearch) {
                toggleShowEmpty(true, "查不到相关数据", null, R.mipmap.search_no_data_icon, false);
            } else {
                toggleShowEmpty(true, "暂无数据", null, R.mipmap.check_look_no_data_icon, false);
                titleBarHelper.setRightGone();
            }

        } else {
            getVaryViewHelperController().restore();
        }
    }

}
