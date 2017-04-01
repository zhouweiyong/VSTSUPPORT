package com.vst.vstsupport.control.message.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.common.TitleBarHelper;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.base.BaseAct;
import com.vst.vstsupport.control.message.adapter.CommonMsgSimpleAdapter;
import com.vst.vstsupport.control.message.adapter.Into90InvAdapter;
import com.vst.vstsupport.control.message.adapter.IntoTotalInvAdapter;
import com.vst.vstsupport.control.message.adapter.MsgFollowAdapter;
import com.vst.vstsupport.mode.bean.Into90InvBean;
import com.vst.vstsupport.mode.bean.Into90InvItemBean;
import com.vst.vstsupport.mode.bean.IntoTotalInvBean;
import com.vst.vstsupport.mode.bean.IntoTotalInvItemBean;
import com.vst.vstsupport.mode.bean.OverdueBean;
import com.vst.vstsupport.mode.bean.OverdueListItemBean;
import com.vst.vstsupport.mode.bean.ReceivableBean;
import com.vst.vstsupport.mode.bean.UserBean;
import com.vst.vstsupport.utils.webview.WebActivity;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.netstatus.NetUtils;

import java.util.ArrayList;

public class CommonMsgDetailActivity extends BaseAct implements PullToRefreshBase.OnRefreshListener2<ListView> {


    public int flag;
    private PullToRefreshListView common_msg_lv;
    private TextView time_tv;
    private LinearLayout layout_nodata;
    private CommonMsgSimpleAdapter expireAdapter;
    private IntoTotalInvAdapter intoTotalInvAdapter;
    private Into90InvAdapter into90InvAdapter;
    private MsgFollowAdapter followAdapter;
    private UserBean userBean;
    private boolean isFirst = true;
    private boolean isDownFresh;
    private int returnCount;
    private int totalPage;
    private Handler handler = new Handler();
    private String path;

    @Override
    protected void initData(Intent intent) {
        super.initData(intent);
        flag = getIntent().getIntExtra("flag", 0);
        returnCount = intent.getIntExtra("returnCount", 0);
        userBean = VstApplication.getInstance().getUserBean();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        TitleBarHelper titleBarHelper = new TitleBarHelper(this, R.string.common_empty, R.string.common_empty, R.string.common_empty);

        layout_nodata = (LinearLayout) findViewById(R.id.layout_nodata);
        common_msg_lv = (PullToRefreshListView) findViewById(R.id.common_msg_lv);
        common_msg_lv.setOnRefreshListener(this);
        ListView listView = common_msg_lv.getRefreshableView();
        View view = View.inflate(this, R.layout.text_msg_detail, null);
        time_tv = (TextView) view.findViewById(R.id.time_tv);
        listView.addHeaderView(view);
        common_msg_lv.setMode(PullToRefreshBase.Mode.BOTH);
        if (flag == 0) {
            titleBarHelper.setTitleMsg("即将到期");
            common_msg_lv.setMode(PullToRefreshBase.Mode.DISABLED);
            expireAdapter = new CommonMsgSimpleAdapter(this);
            common_msg_lv.setAdapter(expireAdapter);
            showLoadingView();
            receivableMessageListNet();
        } else if (flag == 1) {
            titleBarHelper.setTitleMsg("跟进提醒");
            followAdapter = new MsgFollowAdapter(this);
            common_msg_lv.setAdapter(followAdapter);
            showLoadingView();
            overdueMessageListNet();
        } else if (flag == 2) {
            titleBarHelper.setTitleMsg("总库存金额");
            titleBarHelper.setRightMsg("查看表格");
            titleBarHelper.setOnRightClickListener(this);
            path = "/ecsapp/appInventoryModel/intoTotalInvView?account=%s&token=1";
            path = String.format("%s%s", UrlConstants.host,path);
            intoTotalInvAdapter = new IntoTotalInvAdapter(this);
            common_msg_lv.setAdapter(intoTotalInvAdapter);
            showLoadingView();
            intoTotalInvNet();
        } else if (flag == 3) {
            titleBarHelper.setTitleMsg("90天以上库存");
            titleBarHelper.setRightMsg("查看表格");
            titleBarHelper.setOnRightClickListener(this);
            path = "/ecsapp/appInventoryModel/into90InvView?account=%s&token=1";
            path = String.format("%s%s", UrlConstants.host,path);
            into90InvAdapter = new Into90InvAdapter(this);
            common_msg_lv.setAdapter(into90InvAdapter);
            showLoadingView();
            into90InvNet();
        }
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId() == R.id.left) {
            finish();
        } else if (v.getId() == R.id.right) {
            Intent intent = new Intent(this, WebActivity.class);
            path = String.format(path,VstApplication.getInstance().getUserBean().getAccount());
            intent.putExtra("url",path);
            intent.putExtra("flag",flag+"");
            showActivity(this, intent);
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
        if (eventCenter.getEventCode() == 1014) {
            Integer position = (Integer) eventCenter.getData();
            followAdapter.getItem(position).setSolution("已跟进");
            followAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected View getContainerTargetView() {
        return common_msg_lv;
    }

    @Override
    protected void onNetworkConnected(NetUtils.NetType type) {

    }

    @Override
    protected void onNetworkDisConnected() {

    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_common_msg_detail);
    }

    //90天库存
    private void into90InvNet() {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("account", userBean.getAccount());
        ajaxParams.putCommonTypeParam("token", userBean.getToken());
        ajaxParams.putCommonTypeParam("currentPage", CURRENT_PAGE);
        ajaxParams.putCommonTypeParam("showCount", PAGE_SIZE);

        PostRequest<RequestResult<Into90InvBean>> request = new PostRequest<RequestResult<Into90InvBean>>(UrlConstants.INTO90INV, ajaxParams, new OnNetSuccuss<RequestResult<Into90InvBean>>() {
            @Override
            public void onSuccess(RequestResult<Into90InvBean> response) {
                if (response.success && isFirst) {
                    stopAllView();
                    isFirst = false;
                    totalPage = response.totalPage;
                    Into90InvBean into90InvBean = response.rs;
                    time_tv.setText(into90InvBean.getTimes());
                    ArrayList<Into90InvItemBean> into90InvItemBeens = into90InvBean.getNinetyInvData();
                    if (into90InvItemBeens != null && into90InvItemBeens.size() > 0) {
                        into90InvAdapter.setGroup(into90InvItemBeens);
                    } else {
                        showNoDataView();
                    }
                } else if (response.success) {
                    common_msg_lv.onRefreshComplete();
                    Into90InvBean into90InvBean = response.rs;
                    ArrayList<Into90InvItemBean> into90InvItemBeens = into90InvBean.getNinetyInvData();
                    if (into90InvItemBeens != null && into90InvItemBeens.size() > 0) {
                        if (isDownFresh) {
                            into90InvAdapter.setGroup(into90InvItemBeens);
                        } else {
                            into90InvAdapter.addItems(into90InvItemBeens);
                        }
                    }
                } else {
                    stopAllView();
                    showToastMsg(response.msg);
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                if (isFirst) {
                    stopAllView();
                    isFirst = false;
                }
                showNONetView(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingView();
                        into90InvNet();
                    }
                });
            }
        }, new TypeToken<RequestResult<Into90InvBean>>() {
        }.getType());
        executeRequest(request);
    }

    //总库存
    private void intoTotalInvNet() {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("account", userBean.getAccount());
        ajaxParams.putCommonTypeParam("token", userBean.getToken());
        ajaxParams.putCommonTypeParam("currentPage", CURRENT_PAGE);
        ajaxParams.putCommonTypeParam("showCount", PAGE_SIZE);

        PostRequest<RequestResult<IntoTotalInvBean>> request = new PostRequest<RequestResult<IntoTotalInvBean>>(UrlConstants.INTOTOTALINV, ajaxParams, new OnNetSuccuss<RequestResult<IntoTotalInvBean>>() {
            @Override
            public void onSuccess(RequestResult<IntoTotalInvBean> response) {
                if (response.success && isFirst) {
                    stopAllView();
                    isFirst = false;
                    totalPage = response.totalPage;
                    IntoTotalInvBean intoTotalInvBean = response.rs;
                    time_tv.setText(intoTotalInvBean.getTimes());
                    ArrayList<IntoTotalInvItemBean> intoTotalInvItemBeens = intoTotalInvBean.getTotalInv();
                    if (intoTotalInvItemBeens != null && intoTotalInvItemBeens.size() > 0) {
                        intoTotalInvAdapter.setGroup(intoTotalInvItemBeens);
                    } else {
                        showNoDataView();
                    }
                } else if (response.success) {
                    common_msg_lv.onRefreshComplete();
                    IntoTotalInvBean intoTotalInvBean = response.rs;
                    ArrayList<IntoTotalInvItemBean> intoTotalInvItemBeens = intoTotalInvBean.getTotalInv();
                    if (intoTotalInvItemBeens != null && intoTotalInvItemBeens.size() > 0) {
                        if (isDownFresh) {
                            intoTotalInvAdapter.setGroup(intoTotalInvItemBeens);
                        } else {
                            intoTotalInvAdapter.addItems(intoTotalInvItemBeens);
                        }
                    }
                } else {
                    stopAllView();
                    showToastMsg(response.msg);
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                if (isFirst) {
                    stopAllView();
                    isFirst = false;
                }
                showNONetView(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingView();
                        intoTotalInvNet();
                    }
                });
            }
        }, new TypeToken<RequestResult<IntoTotalInvBean>>() {
        }.getType());
        executeRequest(request);
    }

    //跟进提醒
    //{"success":true,"msg":"登陆成功","rs":{"symbol":"A5688","isvstuser":0,"position":0,"account":"zhang.yingming","token":"3bad6af0fa4b8b330d162e19938ee981"},"code":"10001","currentPage":0,"totalPage":0}
    private void overdueMessageListNet() {
        AjaxParams ajaxParams = new AjaxParams();
//        ajaxParams.putCommonTypeParam("account", "dan.wang");
//        ajaxParams.putCommonTypeParam("symbol", "A3801");
        ajaxParams.putCommonTypeParam("account", userBean.getAccount());
        ajaxParams.putCommonTypeParam("symbol", userBean.getSymbol());
        ajaxParams.putCommonTypeParam("isvstuser", userBean.getIsvstuser());
        ajaxParams.putCommonTypeParam("currentPage", CURRENT_PAGE);
        ajaxParams.putCommonTypeParam("showCount", PAGE_SIZE);

        PostRequest<RequestResult<OverdueBean>> request = new PostRequest<RequestResult<OverdueBean>>(UrlConstants.OVERDUEMESSAGELIST, ajaxParams, new OnNetSuccuss<RequestResult<OverdueBean>>() {
            @Override
            public void onSuccess(RequestResult<OverdueBean> response) {
                if (response.success && isFirst) {
                    stopAllView();
                    isFirst = false;
                    totalPage = response.totalPage;
                    OverdueBean overdueBean = response.rs;
                    time_tv.setText(overdueBean.getTimes());
                    followAdapter.setOperation(overdueBean.getOperation());
                    followAdapter.setNowTime(overdueBean.getNowTime());
                    ArrayList<OverdueListItemBean> overdueListItemBeens = overdueBean.getOverdueList();
                    if (overdueListItemBeens != null && overdueListItemBeens.size() > 0) {
                        followAdapter.setGroup(overdueListItemBeens);
                    } else {
                        showNoDataView();
                    }
                } else if (response.success) {
                    common_msg_lv.onRefreshComplete();
                    OverdueBean overdueBean = response.rs;
                    ArrayList<OverdueListItemBean> overdueListItemBeens = overdueBean.getOverdueList();
                    if (overdueListItemBeens != null && overdueListItemBeens.size() > 0) {
                        if (isDownFresh) {
                            followAdapter.setGroup(overdueListItemBeens);
                        } else {
                            followAdapter.addItems(overdueListItemBeens);
                        }
                    }
                } else {
                    stopAllView();
                    showToastMsg(response.msg);
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                if (isFirst) {
                    stopAllView();
                    isFirst = false;
                }
                showNONetView(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingView();
                        overdueMessageListNet();
                    }
                });
            }
        }, new TypeToken<RequestResult<OverdueBean>>() {
        }.getType());
        executeRequest(request);
    }

    //即将到期
    //{"success":true,"msg":"登陆成功","rs":{"symbol":"A2870","isvstuser":0,"position":0,"account":"xuhong.ma","token":"3bad6af0fa4b8b330d162e19938ee981"},"code":"10001","currentPage":0,"totalPage":0}
    private void receivableMessageListNet() {
        AjaxParams ajaxParams = new AjaxParams();
//        ajaxParams.putCommonTypeParam("account", "xuhong.ma");
//        ajaxParams.putCommonTypeParam("symbol", "A2870");
        ajaxParams.putCommonTypeParam("account", userBean.getAccount());
        ajaxParams.putCommonTypeParam("symbol", userBean.getSymbol());
        ajaxParams.putCommonTypeParam("isvstuser", userBean.getIsvstuser());

        PostRequest<RequestResult<ReceivableBean>> request = new PostRequest<RequestResult<ReceivableBean>>(UrlConstants.RECEIVABLEMESSAGELIST, ajaxParams, new OnNetSuccuss<RequestResult<ReceivableBean>>() {
            @Override
            public void onSuccess(RequestResult<ReceivableBean> response) {
                stopAllView();
                if (response.success) {
                    totalPage = response.totalPage;
                    ReceivableBean receivableBean = response.rs;
                    time_tv.setText(receivableBean.getTimes());
                    expireAdapter.addItems(receivableBean.getReceivableFiveDay());
                    expireAdapter.addItems(receivableBean.getReceivableList());
                } else {
                    showToastMsg(response.msg);
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                stopAllView();
                showNONetView(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingView();
                        receivableMessageListNet();
                    }
                });
            }
        }, new TypeToken<RequestResult<ReceivableBean>>() {
        }.getType());
        executeRequest(request);
    }

    private void showNoDataView() {
        common_msg_lv.setVisibility(View.GONE);
        time_tv.setVisibility(View.GONE);
        layout_nodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isDownFresh = true;
        CURRENT_PAGE = 1;
        if (flag == 0) {

        } else if (flag == 1) {
            overdueMessageListNet();
        } else if (flag == 2) {
            intoTotalInvNet();
        } else if (flag == 3) {
            into90InvNet();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isDownFresh = false;
        CURRENT_PAGE++;
        if (CURRENT_PAGE > totalPage) {
            showToastMsg("已经是最后一页");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    common_msg_lv.onRefreshComplete();
                }
            }, 300);
            return;
        }
        if (flag == 0) {//即将到期

        } else if (flag == 1) {//跟进提醒
            overdueMessageListNet();
        } else if (flag == 2) {//总库存
            intoTotalInvNet();
        } else if (flag == 3) {//90天库存
            into90InvNet();
        }
    }
}
