package com.vst.vstsupport.control.message.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.config.UrlConstants;
import com.vst.vstsupport.control.base.BaseFra;
import com.vst.vstsupport.control.message.activity.CommonMsgDetailActivity;
import com.vst.vstsupport.control.message.activity.InventoryMsgIMDetailActivity;
import com.vst.vstsupport.control.message.activity.LimitMsgIMDetailActivity;
import com.vst.vstsupport.control.message.activity.WLimitMsgIMDetailActivity;
import com.vst.vstsupport.control.message.adapter.MessageAdapter;
import com.vst.vstsupport.mode.bean.InventoryBean;
import com.vst.vstsupport.mode.bean.LimitBean;
import com.vst.vstsupport.mode.bean.TuiMessageBean;
import com.vst.vstsupport.mode.bean.WLimitBean;
import com.vst.vstsupport.mode.bean.test.NullBean;
import com.vst.vstsupport.view.swipemenulistview.PullToSwipeMenuListView;
import com.vst.vstsupport.view.swipemenulistview.SwipeMenu;
import com.vst.vstsupport.view.swipemenulistview.SwipeMenuCreator;
import com.vst.vstsupport.view.swipemenulistview.SwipeMenuItem;
import com.vst.vstsupport.view.swipemenulistview.SwipeMenuListView;
import com.vstecs.android.funframework.eventbus.EventCenter;
import com.vstecs.android.funframework.net.xokhttp.OkhttpError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetError;
import com.vstecs.android.funframework.net.xokhttp.callback.OnNetSuccuss;
import com.vstecs.android.funframework.net.xokhttp.https.RequestResult;
import com.vstecs.android.funframework.net.xokhttp.params.AjaxParams;
import com.vstecs.android.funframework.net.xokhttp.request.PostRequest;
import com.vstecs.android.funframework.utils.DensityUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/13
 * class description:请输入类描述
 */
public class MessageFragment extends BaseFra implements SwipeMenuListView.OnMenuItemClickListener, PullToRefreshBase.OnRefreshListener2<SwipeMenuListView>, AdapterView.OnItemClickListener {
    private PullToSwipeMenuListView ptrSMListView;
    private MessageAdapter messageAdapter;
    private SwipeMenuListView swipeMenuListView;
    private int messageTotalNum;


    private boolean isPullRefresh = false;

    @Override
    protected View getContainerTargetView() {
        return swipeMenuListView;
    }

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == 1013) {
            ptrSMListView.post(new Runnable() {
                @Override
                public void run() {
                    ptrSMListView.setRefreshing(true);
                }
            });
        }
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    protected void initWidget(View parentView) {
        TextView title = (TextView) parentView.findViewById(R.id.title_fm);
        title.setText(R.string.title_message);
        ptrSMListView = (PullToSwipeMenuListView) parentView.findViewById(R.id.ptrsmlv_fm);
        ptrSMListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ptrSMListView.setOnRefreshListener(this);
        swipeMenuListView = ptrSMListView.getRefreshableView();
        // Drawable drawable=activity.getResources().getDrawable(R.drawable.shape_divider_lv);
//        swipeMenuListView.setDivider(drawable);
//        swipeMenuListView.setDividerHeight(1);
        messageAdapter = new MessageAdapter(activity);
        ptrSMListView.setAdapter(messageAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        VstApplication.getInstance());
                // set item background
                deleteItem.setBackground(R.color.color_e94415);
                // set item width
                deleteItem.setWidth(DensityUtils.dip2px(VstApplication.getInstance(), 81));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(this);
        swipeMenuListView.setOnItemClickListener(this);

//        showLoadingView();
//        messageNet();
    }

    @Override
    public void onResume() {
        super.onResume();
        ptrSMListView.post(new Runnable() {
            @Override
            public void run() {
                ptrSMListView.setRefreshing(true);
            }
        });
    }

    @Override
    public void onMenuItemClick(int position, SwipeMenu menu, int index) {
        TuiMessageBean tuiMessageBean = messageAdapter.getItem(position);
        if (tuiMessageBean.getMessageId() > 4) {
            deleteCommentsNet(tuiMessageBean,position);
        }else {
            messageAdapter.removeItem(position);
        }
    }

    private void messageNet() {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("account", VstApplication.getInstance().getUserBean().getAccount());
        ajaxParams.putCommonTypeParam("symbol", VstApplication.getInstance().getUserBean().getSymbol());
        ajaxParams.putCommonTypeParam("isvstuser", VstApplication.getInstance().getUserBean().getIsvstuser());

        PostRequest<RequestResult<ArrayList<TuiMessageBean>>> request = new PostRequest<RequestResult<ArrayList<TuiMessageBean>>>(UrlConstants.GETMESSAGELIST, ajaxParams, new OnNetSuccuss<RequestResult<ArrayList<TuiMessageBean>>>() {
            @Override
            public void onSuccess(RequestResult<ArrayList<TuiMessageBean>> response) {
                stopAllView();
                if (response.success && response.rs != null && response.rs.size() > 0) {
                    ArrayList<TuiMessageBean> tuiMessageBeens = response.rs;
                    if (isPullRefresh) {
                        isPullRefresh = false;
                        ptrSMListView.onRefreshComplete();
                        messageAdapter.setGroup(tuiMessageBeens);
                    } else {
                        messageAdapter.setGroup(tuiMessageBeens);
                    }
                    messageTotalNum = 0;
                    for (int i = 0; i < tuiMessageBeens.size(); i++) {
                        if (tuiMessageBeens.get(i).getMessageId() > 4) {
                            messageTotalNum += tuiMessageBeens.get(i).getNoReaderCount();
                        }
                    }
                    if (messageTotalNum > 0) {
                        EventBus.getDefault().post(new EventCenter<Integer>(1012, messageTotalNum));
                    }
                } else {
                    toggleShowEmpty(true, "暂无消息", null, R.mipmap.no_data_msg);
                    if (isPullRefresh) {
                        isPullRefresh = false;
                        ptrSMListView.onRefreshComplete();
                    }
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                stopAllView();
                if (isPullRefresh) {
                    isPullRefresh = false;
                    ptrSMListView.onRefreshComplete();
                }
                showNONetView(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showLoadingView();
                        messageNet();
                    }
                });
            }
        }, new TypeToken<RequestResult<ArrayList<TuiMessageBean>>>() {
        }.getType());
        executeRequest(request);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
        isPullRefresh = true;
        messageNet();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int realPos = position - ((ListView) parent).getHeaderViewsCount();
        TuiMessageBean tuiMessageBean = messageAdapter.getItem(realPos);
        Intent intent = new Intent();
//        intent.setClass(activity, CommonMsgDetailActivity.class);
//        intent.putExtra("flag", 3);

        int messageNum = tuiMessageBean.getNoReaderCount();
        if (tuiMessageBean.isShowNumTag()) {
            tuiMessageBean.setShowNumTag(false);
            messageAdapter.removeItem(realPos);
            messageAdapter.addItem(realPos, tuiMessageBean);
            if (tuiMessageBean.getMessageId() > 4){
                readerCommentsNet(tuiMessageBean,position);
                messageTotalNum -= messageNum;
                EventBus.getDefault().post(new EventCenter<Integer>(1012, messageTotalNum));
            }
        }
        switch (tuiMessageBean.getMessageId()) {
            case 1://即将到期
                intent.setClass(activity, CommonMsgDetailActivity.class);
                intent.putExtra("flag", 0);
                intent.putExtra("returnCount", tuiMessageBean.getReturnCount());
                break;
            case 2://超期跟进
                intent.setClass(activity, CommonMsgDetailActivity.class);
                intent.putExtra("flag", 1);
                intent.putExtra("returnCount", tuiMessageBean.getReturnCount());
                break;
            case 3://总库存金额
                intent.setClass(activity, CommonMsgDetailActivity.class);
                intent.putExtra("flag", 2);
                intent.putExtra("returnCount", tuiMessageBean.getReturnCount());
                break;
            case 4://90天以上库存
                intent.setClass(activity, CommonMsgDetailActivity.class);
                intent.putExtra("flag", 3);
                intent.putExtra("returnCount", tuiMessageBean.getReturnCount());
                break;
            case 5://应收
                if (VstApplication.getInstance().getUserBean().getIsvstuser()==1){
                    intent.setClass(activity, WLimitMsgIMDetailActivity.class);
                    WLimitBean wLimitBean=new WLimitBean();
                    wLimitBean.personId=tuiMessageBean.getPersonId();
                    wLimitBean.customerId=tuiMessageBean.getCustomerId();
                    wLimitBean.brandId=tuiMessageBean.getBrandId();
                    intent.putExtra("WLimitBean", wLimitBean);
                }else{
                    intent.setClass(activity, LimitMsgIMDetailActivity.class);
                    LimitBean limitBean = new LimitBean();
                    limitBean.customerName = tuiMessageBean.getCustomerName();
                    limitBean.secendLevelLine = tuiMessageBean.getSecendLevelLine();
                    limitBean.personId = tuiMessageBean.getPersonId();
                    intent.putExtra("LimitBean", limitBean);
                }

                intent.putExtra("title", tuiMessageBean.getTitle());
                if (tuiMessageBean.getIdentityType() == 0) {
                    intent.putExtra("flag", "5");
                } else {
                    intent.putExtra("flag", "4");
                }
                break;
            case 6://库存 项目
                intent.setClass(activity, InventoryMsgIMDetailActivity.class);
                InventoryBean inventoryBean = new InventoryBean();
                inventoryBean.projectSerial = tuiMessageBean.getProjectSerial();
                inventoryBean.secendLevelLine = tuiMessageBean.getSecendLevelLine();
//                inventoryBean.projectName = tuiMessageBean.getProjectName();
                inventoryBean.type = "项目";
                inventoryBean.businessDepartment = tuiMessageBean.getBusinessDepartment();
                intent.putExtra("InventoryBean", inventoryBean);
                intent.putExtra("title", tuiMessageBean.getTitle());
                break;
            case 7://库存 流量
                intent.setClass(activity, InventoryMsgIMDetailActivity.class);
                InventoryBean inventoryBean2 = new InventoryBean();
                inventoryBean2.projectSerial = tuiMessageBean.getProjectSerial();
                inventoryBean2.secendLevelLine = tuiMessageBean.getSecendLevelLine();
//                inventoryBean2.projectName = tuiMessageBean.getProjectName();
                inventoryBean2.type = "流量";
                inventoryBean2.businessDepartment = tuiMessageBean.getBusinessDepartment();
                intent.putExtra("InventoryBean", inventoryBean2);
                intent.putExtra("title", tuiMessageBean.getTitle());
                break;
        }
        showActivity(activity, intent);
    }


    private void deleteCommentsNet(final TuiMessageBean tuiMessageBean,final int position) {
        showLoadingDialog("正在删除");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("account", VstApplication.getInstance().getUserBean().getAccount());
        ajaxParams.putCommonTypeParam("sendAccount",tuiMessageBean.getTitle() );
        ajaxParams.putCommonTypeParam("objectId",tuiMessageBean.getObjectId() );

        PostRequest<RequestResult<NullBean>> request = new PostRequest<RequestResult<NullBean>>(UrlConstants.READERCOMMENTS, ajaxParams, new OnNetSuccuss<RequestResult<NullBean>>() {
            @Override
            public void onSuccess(RequestResult<NullBean> response) {
                dismissLoadingDialog();
                messageTotalNum -= tuiMessageBean.getNoReaderCount();
                EventBus.getDefault().post(new EventCenter<Integer>(1012, messageTotalNum));
                messageAdapter.removeItem(position);
                if (messageAdapter.getCount()<1){
                    toggleShowEmpty(true, "暂无消息", null, R.mipmap.no_data_msg);
                }
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                dismissLoadingDialog();
                showToastMsg(okhttpError.errMsg);
            }
        }, new TypeToken<RequestResult<NullBean>>() {
        }.getType());
        executeRequest(request);
    }

    private void readerCommentsNet(final TuiMessageBean tuiMessageBean,final int position) {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.putCommonTypeParam("account", VstApplication.getInstance().getUserBean().getAccount());
        ajaxParams.putCommonTypeParam("sendAccount",tuiMessageBean.getTitle() );
        ajaxParams.putCommonTypeParam("objectId",tuiMessageBean.getObjectId() );

        PostRequest<RequestResult<NullBean>> request = new PostRequest<RequestResult<NullBean>>(UrlConstants.READERCOMMENTS, ajaxParams, new OnNetSuccuss<RequestResult<NullBean>>() {
            @Override
            public void onSuccess(RequestResult<NullBean> response) {
            }
        }, new OnNetError() {
            @Override
            public void onError(OkhttpError okhttpError) {
                showToastMsg(okhttpError.errMsg);
            }
        }, new TypeToken<RequestResult<NullBean>>() {
        }.getType());
        executeRequest(request);
    }
}
