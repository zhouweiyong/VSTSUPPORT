package com.vst.vstsupport.control.inventory.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.inventory.adapter.InventoryFollowAdapter;
import com.vst.vstsupport.mode.bean.BusinessDepartmentBean;
import com.vst.vstsupport.mode.bean.InventoryBean;
import com.vst.vstsupport.mode.bean.InventoryFollowSearchBean;
import com.vst.vstsupport.mode.bean.InventoryRsBean;
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

public class InventoryFollowActivity extends BaseAct {

    public String operation;
    public long serverTime;
    private PullToRefreshListView kujungenjin_ptr_lv;
    private InventoryFollowAdapter adapter;
    private ArrayList<InventoryBean> datas;
    private InventoryFollowSearchBean bean=new InventoryFollowSearchBean();
    private ArrayList<BusinessDepartmentBean> businessDepartmentList;
    private boolean isSearch;
    private TitleBarHelper titleBarHelper;

    private LinearLayout sort_ll;
    private TextView sort_by_num_tv;
    private TextView sort_by_day_tv;
    private ImageView sort_by_num_iv;
    private ImageView sort_by_day_iv;
    private RelativeLayout sort_by_num_rl;
    private RelativeLayout sort_by_day_rl;
    private boolean isNumAsend=true;//true 升序 false 降序
    private boolean isDayAsend=true;//true 升序 false 降序
    private int selectColorId= Color.parseColor("#e94415");
    private int unselesctColorId= Color.parseColor("#646464");

    private static final int SELECT_NUM = 0;
    private static final int SELECT_DAY=1;
    private int lastSelect = SELECT_NUM;

    @Override
    protected void onResume() {
        super.onResume();
        getInventoryListData(bean.businessDepartment,bean.secendLevelLine,bean.projectSerial,orderBy.moneyDown+"");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarHelper= new TitleBarHelper(this, R.string.common_empty, R.string.limit_time_expire, R.string.common_empty);
        titleBarHelper.setTitleMsg("库存跟进");
        titleBarHelper.setOnRightClickListener(this);
        kujungenjin_ptr_lv= (PullToRefreshListView) findViewById(R.id.kujungenjin_ptr_lv);
        sort_ll= (LinearLayout) findViewById(R.id.sort_ll);
        sort_ll.setVisibility(View.VISIBLE);
        setSort_llView();
        adapter=new InventoryFollowAdapter(aty);
        kujungenjin_ptr_lv.setAdapter(adapter);

        bean.orderBy=orderBy.moneyDown+"";
        kujungenjin_ptr_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE = 1;
                getInventoryListData(bean.businessDepartment, bean.secendLevelLine, bean.projectSerial, bean.orderBy);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                CURRENT_PAGE++;
                getInventoryListData(bean.businessDepartment, bean.secendLevelLine, bean.projectSerial, bean.orderBy);
            }
        });

        kujungenjin_ptr_lv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int realPos = position - ((ListView) parent).getHeaderViewsCount();
                if (realPos >= 0 && realPos < adapter.getCount()) {
                    Intent intent = new Intent(aty, InventoryDetailActivity.class);
                    intent.putExtra("InventoryBean", adapter.getItem(realPos));
                    showActivity(aty, intent);
                }
            }
        });


    }

    private void setSort_llView(){
        sort_by_num_tv= (TextView) sort_ll.findViewById(R.id.sort_by_num_tv);
        sort_by_day_tv= (TextView) sort_ll.findViewById(R.id.sort_by_day_tv);

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
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.LEFT;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;

    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        bean= (InventoryFollowSearchBean) eventCenter.getData();
        CURRENT_PAGE=1;
        getInventoryListData(bean.businessDepartment,bean.secendLevelLine,bean.projectSerial, bean.orderBy);
        isSearch=true;
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
        setContentView(R.layout.activity_inventory_follow);
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId()==R.id.left){
            finish();
        }else if(v.getId()==R.id.right){
            Intent intent=new Intent(aty,InventorySearchActivity.class);
            intent.putExtra("flag","inventory_follow");
            showActivity(aty, intent);
        }else if(v.getId() == R.id.sort_by_day_iv||v.getId() == R.id.sort_by_day_tv){
            if (lastSelect==SELECT_NUM){
                isDayAsend = false;
                lastSelect = SELECT_DAY;
            }
            sort_by_num_tv.setTextColor(unselesctColorId);
            sort_by_day_tv.setTextColor(selectColorId);
            sort_by_num_iv.setImageResource(R.mipmap.default_unselect_sort_icon);

                if (isDayAsend){
                    sort_by_day_iv.setImageResource(R.mipmap.default_ascend_sort_icon);
                    isDayAsend=false;
                    bean.orderBy=orderBy.daysUp+"";
                }else{
                    sort_by_day_iv.setImageResource(R.mipmap.default_decend_sort_icon);
                    isDayAsend=true;
                    bean.orderBy=orderBy.daysDown+"";
                }
                CURRENT_PAGE = 1;
                getInventoryListData(bean.businessDepartment, bean.secendLevelLine, bean.projectSerial, bean.orderBy);


        }else if(v.getId() == R.id.sort_by_num_iv||v.getId() == R.id.sort_by_num_tv){
            if (lastSelect == SELECT_DAY){
                lastSelect = SELECT_NUM;
                isNumAsend=false;
            }
            sort_by_num_tv.setTextColor(selectColorId);
            sort_by_day_tv.setTextColor(unselesctColorId);
            sort_by_day_iv.setImageResource(R.mipmap.default_unselect_sort_icon);
                if (isNumAsend){
                    sort_by_num_iv.setImageResource(R.mipmap.default_ascend_sort_icon);
                    isNumAsend=false;
                    bean.orderBy=orderBy.moneyUp+"";
                }else{
                    sort_by_num_iv.setImageResource(R.mipmap.default_decend_sort_icon);
                    isNumAsend=true;
                    bean.orderBy=orderBy.moneyDown+"";
                }
                CURRENT_PAGE = 1;
                getInventoryListData(bean.businessDepartment, bean.secendLevelLine, bean.projectSerial, bean.orderBy);

        }

    }

    private void getInventoryListData(String businessDepartment,String secendLevelLine,String projectSerial,String orderBy) {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("currentPage",""+CURRENT_PAGE);
        params.putCommonTypeParam("showCount",""+PAGE_SIZE);
        params.putCommonTypeParam("account", "" + VstApplication.getInstance().getUserBean().getAccount());
        params.putCommonTypeParam("position", "" + VstApplication.getInstance().getUserBean().getPosition());
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());//是否是vstuser

        if (!TextUtils.isEmpty(businessDepartment))
            params.putCommonTypeParam("businessDepartment",businessDepartment);
        if (!TextUtils.isEmpty(secendLevelLine))
            params.putCommonTypeParam("secendLevelLine",secendLevelLine);
        if (!TextUtils.isEmpty(projectSerial))
            params.putCommonTypeParam("projectSerial",projectSerial);
        if (!TextUtils.isEmpty(orderBy))
            params.putCommonTypeParam("orderBy",orderBy);

        PostRequest<RequestResult<InventoryRsBean>> request = new PostRequest<RequestResult<InventoryRsBean>>(UrlConstants.INVENTORYLIST, params, new OnNetSuccuss<RequestResult<InventoryRsBean>>() {
            @Override
            public void onSuccess(RequestResult<InventoryRsBean> response) {

                Log.i("vst", response.msg);
                if (response.rs != null) {
                    datas = response.rs.inventoryList;
                    operation = response.rs.operation;
                    serverTime = response.rs.nowTime;
                    removeSp();
                    if (response.rs.businessDepartmentList!=null) {
                        VstApplication.getInstance().setDepartmentBeans(response.rs.businessDepartmentList);
                    }

                    if (!TextUtils.isEmpty(response.rs.secendLevelLineSring)) {
                        PreferHelper.getInstance().setString("secendLevelLineList",response.rs.secendLevelLineSring);
                    }
                    if (CURRENT_PAGE==1){
                        adapter.setGroup(datas);
                    }else {
                        adapter.addItems(datas);
                    }
                    changeRefreshMode(response.totalPage);
                }
                setNullLoy();
                kujungenjin_ptr_lv.onRefreshComplete();
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception + "");
                showErrorView();
                dismissLoadingDialog();
                kujungenjin_ptr_lv.onRefreshComplete();
            }
        }, new TypeToken<RequestResult<InventoryRsBean>>() {
        }.getType());
        executeRequest(request);
    }

    private void removeSp() {
        if (!TextUtils.isEmpty(PreferHelper.getInstance().getString("businessDepartmentList"))){
            PreferHelper.getInstance().remove("businessDepartmentList");
        }
        if (!TextUtils.isEmpty(PreferHelper.getInstance().getString("secendLevelLineList"))){
            PreferHelper.getInstance().remove("secendLevelLineList");
        }
        if (!TextUtils.isEmpty(PreferHelper.getInstance().getString("areaList"))){
            PreferHelper.getInstance().remove("areaList");
        }
        if (VstApplication.getInstance().getDepartmentBeans()!=null){
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

        }else{
            getVaryViewHelperController().restore();
        }
    }

    public enum orderBy{
        moneyUp ,//:金额升序


        moneyDown,//: 金额降序


        daysUp,//: 在库天数升序


        daysDown //: 在库天数降序
    }


}
