package com.vst.vstsupport.control.arrears.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.arrears.adapter.CommonSelectAdapter;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.inventory.activity.InventorySearchActivity;
import com.vst.vstsupport.control.inventory.adapter.InventoryDeptSelectAdapter;
import com.vst.vstsupport.control.inventory.adapter.InventorySecLineSelectAdapter;
import com.vst.vstsupport.control.inventory.adapter.LimitBrandAdapter;
import com.vst.vstsupport.mode.bean.BrandBean;
import com.vst.vstsupport.mode.bean.BusinessDepartmentBean;
import com.vst.vstsupport.mode.bean.SecendLevelLineBean;
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
import java.util.Arrays;

public class CommonSelectActivity extends BaseAct {

    private ListView select_lv;
    private LinearLayout layout_nodata;
    CommonSelectAdapter adapter;
    InventoryDeptSelectAdapter inventoryDeptSelectAdapter;
    InventorySecLineSelectAdapter inventorySecLineSelectAdapter;
    LimitBrandAdapter mLimitBrandAdapter;
    ArrayList<String> datas = new ArrayList<String>();
    ArrayList<BrandBean> mBrandBeans = new ArrayList<BrandBean>();
    String flag;
    String entrance;//判断是从哪个入口进来inventory_follow:库存跟进列表  inventory_look:库存查看列表  limit_follow:超期跟进列表  limit_look:应收查看
    private ArrayList<String> businessDepartmentList=new ArrayList<String>();
    private ArrayList<String> secendLevelLineList=new ArrayList<String>();
    private ArrayList<String> areaList=new ArrayList<String>();

    private ArrayList<BusinessDepartmentBean> departmentBeans=new ArrayList<BusinessDepartmentBean>();
    private ArrayList<SecendLevelLineBean> secendLevelLineBeans=new ArrayList<SecendLevelLineBean>();
    private String operation;
    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        flag = getIntent().getStringExtra("flag");
        entrance = getIntent().getStringExtra("entrance");
        operation = getIntent().getStringExtra("operation");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        TitleBarHelper titleBarHelper = new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty);

        layout_nodata = (LinearLayout) findViewById(R.id.layout_nodata);
        select_lv = (ListView) findViewById(R.id.select_lv);
        adapter = new CommonSelectAdapter(this);
        inventoryDeptSelectAdapter = new InventoryDeptSelectAdapter(this);
        inventorySecLineSelectAdapter = new InventorySecLineSelectAdapter(this);
        mLimitBrandAdapter = new LimitBrandAdapter(this);


        if (entrance.equals("inventory_follow")||entrance.equals("inventory_look")) {//库存积压的情况

            switch (flag) {
                case "1":
                    titleBarHelper.setTitleMsg("选择事业部");
                    departmentBeans= VstApplication.getInstance().getDepartmentBeans();
                    inventoryDeptSelectAdapter.setGroup(departmentBeans);
                    select_lv.setAdapter(inventoryDeptSelectAdapter);
                    showNullView(inventoryDeptSelectAdapter);
                    break;
                case "2":
                    titleBarHelper.setTitleMsg("选择二级线");
                    if (VstApplication.getInstance().getUserBean().getPosition()==1){//如果是产品经理，是一字符串逗号形式返回的
                        String secondStr=PreferHelper.getInstance().getString("secendLevelLineList");
                        if (!TextUtils.isEmpty(secondStr))
                            secendLevelLineList= changeDatasFormat(secondStr);
                        adapter.setGroup(secendLevelLineList);
                        select_lv.setAdapter(adapter);
                        showNullView(adapter);
                    }else{
                        secendLevelLineBeans =VstApplication.getInstance().getSecendLevelLineList();
                        inventorySecLineSelectAdapter.setGroup(secendLevelLineBeans);
                        select_lv.setAdapter(inventorySecLineSelectAdapter);
                        showNullView(inventorySecLineSelectAdapter);
                    }

                    break;
            }

        }else{//超期模块情况

            switch (flag) {

                case "1":
                    titleBarHelper.setTitleMsg("选择事业部");
                    departmentBeans= VstApplication.getInstance().getDepartmentBeans();
                    inventoryDeptSelectAdapter.setGroup(departmentBeans);
                    select_lv.setAdapter(inventoryDeptSelectAdapter);
                    showNullView(inventoryDeptSelectAdapter);
                    break;
                case "2":
                    titleBarHelper.setTitleMsg("选择二级线");
                    if (entrance.equals("limit_follow")){//超期列表情况
                        if (operation.equals("follow")) {//如果是销售，是一字符串逗号形式返回的
                            String secondStr=PreferHelper.getInstance().getString("secendLevelLineList");
                            if (!TextUtils.isEmpty(secondStr))
                                secendLevelLineList= changeDatasFormat(secondStr);
                            adapter.setGroup(secendLevelLineList);
                            showNullView(adapter);
                            select_lv.setAdapter(adapter);
                        }else{

                            secendLevelLineBeans =VstApplication.getInstance().getSecendLevelLineList();
                            inventorySecLineSelectAdapter.setGroup(secendLevelLineBeans);
                            select_lv.setAdapter(inventorySecLineSelectAdapter);
                            showNullView(inventorySecLineSelectAdapter);
                        }
                    }else{//应收查看情况
                        String secondStr=PreferHelper.getInstance().getString("secendLevelLineList");
                        if (!TextUtils.isEmpty(secondStr))
                            secendLevelLineList= changeDatasFormat(secondStr);
                        adapter.setGroup(secendLevelLineList);
                        showNullView(adapter);
                        select_lv.setAdapter(adapter);
                    }


                    break;
                case "3":
                    titleBarHelper.setTitleMsg("选择区域");
                    String areaStr=PreferHelper.getInstance().getString("areaList");
                    if (!TextUtils.isEmpty(areaStr))
                        areaList= changeDatasFormat(areaStr);
                    adapter.setGroup(areaList);
                    showNullView(adapter);
                    select_lv.setAdapter(adapter);
                    break;
                case "4":
                    titleBarHelper.setTitleMsg("选择品牌");
                    getBrand();

                    break;
            }
        }





        select_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    if (entrance.equals("inventory_follow")||entrance.equals("inventory_look")) {//库存积压的情况
                        intent.setClass(aty, InventorySearchActivity.class);
                        if (flag.equals("1")) {//flag  1:事业部，2：二级线
                            intent.putExtra("object", inventoryDeptSelectAdapter.getItem(position));
                        }else{
                            if (VstApplication.getInstance().getUserBean().getPosition()==1) {//如果是产品经理，是一字符串逗号形式返回的

                                intent.putExtra("name", adapter.getItem(position));//返回的是字符串

                            }else{
                                intent.putExtra("object", inventorySecLineSelectAdapter.getItem(position));//返回的是对象
                            }
                        }
                    }else if(entrance.equals("limit_follow")||entrance.equals("limit_look")){//超期模块
                        intent.setClass(aty, LimitSerachActivity.class);
                        if (flag.equals("1")) {//flag  1:事业部，2：二级线 3:区域
                            intent.putExtra("object", inventoryDeptSelectAdapter.getItem(position));
                        }else if (flag.equals("2")){//二级线
                            if (operation.equals("follow")) {//如果是销售，是一字符串逗号形式返回的

                                intent.putExtra("name", adapter.getItem(position));//返回的是字符串

                            }else{//领导
                                intent.putExtra("object", inventorySecLineSelectAdapter.getItem(position));//返回的是对象
                            }
                        }else{
                            intent.putExtra("name", adapter.getItem(position));//返回的是字符串
                        }
                    }else{
                        intent.setClass(aty, WLimitSearchActivity.class);
                        intent.putExtra("object", mLimitBrandAdapter.getItem(position));//返回的是对象
                    }


                    setResult(RESULT_OK, intent);
                    finish();

                }
        });
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
        setContentView(R.layout.activity_common_select);
    }

    private static ArrayList<String> changeDatasFormat(String str) {
        ArrayList<String> datas = new ArrayList<String>();
        String[] arrs = str.split(",");
        System.out.println(Arrays.asList(arrs));
        datas.addAll(Arrays.asList(arrs));
        return datas;
    }

    private void showNullView(BaseAdapter adapter){
        if (adapter.getCount()<=0){
            layout_nodata.setVisibility(View.VISIBLE);
            select_lv.setVisibility(View.GONE);
        }else{
            layout_nodata.setVisibility(View.GONE);
            select_lv.setVisibility(View.VISIBLE);
        }
    }

    private void getBrand(){
        showLoadingDialog("加载中...");
        AjaxParams params=new AjaxParams();
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());


        PostRequest<RequestResult<ArrayList<BrandBean>>> request = new PostRequest<RequestResult<ArrayList<BrandBean>>>(UrlConstants.GETBRAND, params, new OnNetSuccuss<RequestResult<ArrayList<BrandBean>>>() {
            @Override
            public void onSuccess(RequestResult<ArrayList<BrandBean>> response) {
                mBrandBeans=response.rs;
                mLimitBrandAdapter.setGroup(mBrandBeans);
                select_lv.setAdapter(mLimitBrandAdapter);
                showNullView(mLimitBrandAdapter);
                Log.i("vst", response.msg);
                dismissLoadingDialog();
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                Log.i("vst", okhttpError.exception+"");
                dismissLoadingDialog();
            }
        },new TypeToken<RequestResult<ArrayList<BrandBean>>>() {
        }.getType());
        executeRequest(request);
    }


}
