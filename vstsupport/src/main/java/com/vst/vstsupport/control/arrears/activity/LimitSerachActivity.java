package com.vst.vstsupport.control.arrears.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.mode.bean.BusinessDepartmentBean;
import com.vst.vstsupport.mode.bean.LimitFollowSearchBean;
import com.vst.vstsupport.mode.bean.SecendLevelLineBean;
import com.vst.vstsupport.mode.bean.SecendLevelLineRs;
import com.vst.vstsupport.utils.PreferHelper;
import com.vst.vstsupport.view.ChangeDatePopwindow;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.netstatus.NetUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class LimitSerachActivity extends BaseAct {

    private EditText account_name_et;
    private EditText sales_man_et;
    private RelativeLayout start_date_loy;
    private RelativeLayout end_date_loy;
    private RelativeLayout dept_loy;
    private RelativeLayout area_loy;
    private RelativeLayout second_line_loy;
    private TextView start_date_tv;
    private TextView end_date_tv;

    private TextView dept_tv;
    private TextView second_line_tv;
    private TextView area_tv;
    private Button btn_confirm;
    private String flag;//判断是从哪个入口进来inventory_follow:库存跟进列表  inventory_look:库存查看列表  limit_follow:超期跟进列表  limit_look:应收查看
    private String startDateStr;//开始时间
    private String endDateStr;//结束时间
    private LinearLayout area_line;
    private LinearLayout dept_line;
    private LinearLayout second_line;
    private LinearLayout account_name_et_line;
    private LinearLayout line_lint_date_start;
    private LinearLayout line_lint_date_end;

    private ArrayList<SecendLevelLineBean> secendLevelLineList;
    private BusinessDepartmentBean businessDepartmentBean;
    private String operation;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        flag = getIntent().getStringExtra("flag");
        operation=getIntent().getStringExtra("operation");
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty).setTitleMsg("查询");
        start_date_loy = (RelativeLayout) findViewById(R.id.start_date_loy);
        start_date_loy.setOnClickListener(this);
        end_date_loy = (RelativeLayout) findViewById(R.id.end_date_loy);
        end_date_loy.setOnClickListener(this);
        findViewById(R.id.end_date_loy).setOnClickListener(this);
        dept_loy = (RelativeLayout) findViewById(R.id.dept_loy);
        dept_loy.setOnClickListener(this);
        second_line_loy = (RelativeLayout) findViewById(R.id.second_line_loy);
        second_line_loy.setOnClickListener(this);
        area_loy = (RelativeLayout) findViewById(R.id.area_loy);
        area_loy.setOnClickListener(this);
        account_name_et = (EditText) findViewById(R.id.account_name_et);
        sales_man_et = (EditText) findViewById(R.id.sales_man_et);
        start_date_tv = (TextView) findViewById(R.id.start_date_tv);
        end_date_tv = (TextView) findViewById(R.id.end_date_tv);
        dept_tv = (TextView) findViewById(R.id.dept_tv);
        second_line_tv = (TextView) findViewById(R.id.second_line_tv);
        area_tv = (TextView) findViewById(R.id.area_tv);
        dept_line = (LinearLayout) findViewById(R.id.dept_line);
        area_line = (LinearLayout) findViewById(R.id.area_line);
        second_line = (LinearLayout) findViewById(R.id.second_line);
        line_lint_date_start = (LinearLayout) findViewById(R.id.line_lint_date_start);
        line_lint_date_end = (LinearLayout) findViewById(R.id.line_lint_date_end);
        account_name_et_line = (LinearLayout) findViewById(R.id.account_name_et_line);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        if (VstApplication.getInstance().getUserBean().getIsvstuser() == 1) {//是vst员工
            //vst没有二级线 区域的说法
            second_line_loy.setVisibility(View.GONE);
            second_line.setVisibility(View.GONE);
            area_loy.setVisibility(View.GONE);
            area_line.setVisibility(View.GONE);

            if (flag.equals("limit_follow")) {//超期跟进的情况（超期跟进列表查询）
                if (operation.equals("ask")) {//超期跟进领导的情况
                    dept_loy.setVisibility(View.VISIBLE);
                    dept_line.setVisibility(View.VISIBLE);
                } else {//业务员
                    dept_loy.setVisibility(View.GONE);
                    dept_line.setVisibility(View.GONE);
                }

            }
//            else {//应收查看列表查询
//                dept_loy.setVisibility(View.GONE);
//                dept_line.setVisibility(View.GONE);
//            }

        } else {//佳杰员工 原型一致
            second_line_loy.setVisibility(View.VISIBLE);
            second_line.setVisibility(View.VISIBLE);
            if (flag.equals("limit_follow")) {//超期跟进的情况（超期跟进列表查询）
                start_date_loy.setVisibility(View.GONE);
                end_date_loy.setVisibility(View.GONE);
                line_lint_date_start.setVisibility(View.GONE);
                line_lint_date_end.setVisibility(View.GONE);

            }else{//应收查看列表查询
                start_date_loy.setVisibility(View.VISIBLE);
                end_date_loy.setVisibility(View.VISIBLE);
                line_lint_date_start.setVisibility(View.VISIBLE);
                line_lint_date_end.setVisibility(View.VISIBLE);
            }

            if (operation.equals("ask")) {//超期跟进领导的情况
                dept_loy.setVisibility(View.VISIBLE);
                dept_line.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(PreferHelper.getInstance().getString("areaList"))) {
                    area_loy.setVisibility(View.GONE);
                    area_line.setVisibility(View.GONE);
                } else {
                    area_loy.setVisibility(View.VISIBLE);
                    area_line.setVisibility(View.VISIBLE);
                }
            } else {//超期跟进销售的情况
                dept_loy.setVisibility(View.GONE);
                area_loy.setVisibility(View.GONE);
                area_line.setVisibility(View.GONE);
                dept_line.setVisibility(View.GONE);
            }

//            else {//应收查看列表查询
//                dept_loy.setVisibility(View.GONE);
//                area_loy.setVisibility(View.GONE);
//                area_line.setVisibility(View.GONE);
//                dept_line.setVisibility(View.GONE);
//            }
        }


    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.start_date_loy:
                selectDate(0);
                break;
            case R.id.end_date_loy:
                selectDate(1);
                break;
            case R.id.dept_loy:
                startSelectActivity("1", 201, flag);
                break;
            case R.id.second_line_loy:
                if (VstApplication.getInstance().getUserBean().getIsvstuser() == 1) {//是vst员工
                    if (PreferHelper.getInstance().getString("secendLevelLineList") != null) {
                        startSelectActivity("2", 202, flag);
                    }
                } else {//佳杰的情况 原型一致
                    if (operation.equals("ask")) {//佳杰超期跟进领导的情况

                        if (secendLevelLineList != null && businessDepartmentBean != null) {
                            startSelectActivity("2", 202, flag);
                        } else {
                            showToastMsg("请选择事业部");
                        }
                    } else {//佳杰业务员

                        if (PreferHelper.getInstance().getString("secendLevelLineList") != null) {
                            startSelectActivity("2", 202, flag);
                        }
                    }

                }


                break;
            case R.id.area_loy:
                startSelectActivity("3", 203, flag);
                break;
            case R.id.btn_confirm:
                if (!TextUtils.isEmpty(startDateStr) && !TextUtils.isEmpty(endDateStr)) {
                    try {
                        if (calculateTimeStmap(endDateStr) < calculateTimeStmap(startDateStr)) {
                            showToastMsg("开始时间不能大于结束时间");
                            return;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


                if (flag.equals("limit_follow")) {
                    LimitFollowSearchBean searchBean = new LimitFollowSearchBean();
                    searchBean.areaName = area_tv.getText().toString();
                    searchBean.businessDepartment = dept_tv.getText().toString();
                    searchBean.customerName = getInputStr(account_name_et);
                    searchBean.saleName = getInputStr(sales_man_et);
//                    searchBean.stratDate = start_date_tv.getText().toString();
//                    searchBean.endDate = end_date_tv.getText().toString();
                    searchBean.secendLevelLine = second_line_tv.getText().toString();
                    searchBean.areaName = area_tv.getText().toString();
                    searchBean.businessDepartment = dept_tv.getText().toString();
                    EventBus.getDefault().post(new EventCenter<LimitFollowSearchBean>(2, searchBean));

                }

                else {
                    LimitFollowSearchBean searchBean = new LimitFollowSearchBean();
                    searchBean.customerName = getInputStr(account_name_et);
                    searchBean.saleName = getInputStr(sales_man_et);
                    searchBean.stratDate = startDateStr;
                    searchBean.endDate = endDateStr;
                    searchBean.secendLevelLine = second_line_tv.getText().toString();
                    searchBean.areaName = area_tv.getText().toString();
                    searchBean.businessDepartment = dept_tv.getText().toString();
                    EventBus.getDefault().post(new EventCenter<LimitFollowSearchBean>(5, searchBean));
                }

                finish();
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startSelectActivity(String flag, int requestCode, String entrance) {
        Intent intent = new Intent(aty, CommonSelectActivity.class);
        intent.putExtra("flag", flag);//flag区分是事业部，二级线，区域
        intent.putExtra("entrance", entrance);
        intent.putExtra("operation",operation);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 201) {
            if (data != null) {
                businessDepartmentBean = (BusinessDepartmentBean) data.getSerializableExtra("object");
                dept_tv.setText(businessDepartmentBean.businessDepartment);
                if (VstApplication.getInstance().getUserBean().getIsvstuser() == 0) {//佳杰员工才联动查二级线
                    getsecendLevelLineData();
                }

            }
        }
        if (requestCode == 202) {
            if (data != null) {
                if (data.getStringExtra("name") != null) {
                    second_line_tv.setText(data.getStringExtra("name"));
                } else {
                    SecendLevelLineBean secendLevelLineBean = (SecendLevelLineBean) data.getSerializableExtra("object");
                    second_line_tv.setText(secendLevelLineBean.secendLevelLine);
                }
            }
        }
        if (requestCode == 203) {
            if (data != null) {
                area_tv.setText(data.getStringExtra("name"));
            }
        }
    }

    private void getsecendLevelLineData() {
        showLoadingDialog("加载中...");
        AjaxParams params = new AjaxParams();
        params.putCommonTypeParam("token", "" + VstApplication.getInstance().getUserBean().getToken());
        params.putCommonTypeParam("businessDepartmentId", "" + businessDepartmentBean.businessDepartmentId);//事业部ID


        PostRequest<RequestResult<SecendLevelLineRs>> request = new PostRequest<RequestResult<SecendLevelLineRs>>(UrlConstants.GETSECONDLEVELLINE, params, new OnNetSuccuss<RequestResult<SecendLevelLineRs>>() {
            @Override
            public void onSuccess(RequestResult<SecendLevelLineRs> response) {

                Log.i("vst", response.msg);
                if (response.rs != null) {
                    secendLevelLineList = response.rs.secendLevelLineList;

                    VstApplication.getInstance().setSecendLevelLineList(secendLevelLineList);
//                    showToastMsg(response.msg);
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
        }, new TypeToken<RequestResult<SecendLevelLineRs>>() {
        }.getType());
        executeRequest(request);
    }

    private Long calculateTimeStmap(String dateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse("" + dateStr);
        long timeStemp = date.getTime();
        return timeStemp;
    }


    private String[] selectDate(final int flag) {
        final String[] str = new String[10];
        ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(
                aty);
//        mChangeBirthDialog.setDate("2016", "1", "1");
        mChangeBirthDialog.setDate(mChangeBirthDialog.getYear(), mChangeBirthDialog.getMonth(), mChangeBirthDialog.getDay());
        mChangeBirthDialog.showAtLocation(start_date_loy, Gravity.BOTTOM, 0, 0);
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {
                // TODO Auto-generated method stub
                showToastMsg(year + "-" + month + "-" + day);
                StringBuilder sb = new StringBuilder();
                sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, month.length() - 1)).append("-").append(day.substring(0, day.length() - 1));
                str[0] = year + "-" + month + "-" + day;
                str[1] = sb.toString();

                if (flag == 0) {
                    start_date_tv.setText(str[0]);
                    startDateStr = str[1];
                } else {
                    end_date_tv.setText(str[0]);
                    endDateStr = str[1];
                }

            }
        });
        return str;
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
        setContentView(R.layout.activity_common_serach);
    }


}
