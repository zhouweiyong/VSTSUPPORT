package com.vst.vstsupport.control.arrears.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.arrears.activity.CheckReceiveActivity;
import com.vst.vstsupport.control.arrears.activity.LimitTimeFollowActivity;
import com.vst.vstsupport.control.arrears.activity.WCheckReceiveActivity;
import com.vst.vstsupport.control.arrears.activity.WLimitTimeFollowActivity;
import com.vst.vstsupport.control.base.BaseFra;
import com.vstecs.android.funframework.eventbus.EventCenter;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/13
 * class description:应收账款
 */
public class ArrearsFragment extends BaseFra {

    private TextView limit_time_expire_tv;
    private TextView should_look_tv;

    @Override
    protected View getContainerTargetView() {
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
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.frg_arreats_layout,container,false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        limit_time_expire_tv= (TextView) parentView.findViewById(R.id.limit_time_expire_tv);
        should_look_tv= (TextView) parentView.findViewById(R.id.should_look_tv);
        limit_time_expire_tv.setOnClickListener(this);
        should_look_tv.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        super.widgetClick(v);
        Intent intent=null;
        if (v.getId()==R.id.should_look_tv){
            if (VstApplication.getInstance().getUserBean().getIsvstuser()==1){
                intent=new Intent(activity, WCheckReceiveActivity.class);
            }else{
                intent=new Intent(activity, CheckReceiveActivity.class);
                intent.putExtra("flag","limit");
            }
            showActivity(activity, intent);
        }else if(v.getId()==R.id.limit_time_expire_tv){
            if (VstApplication.getInstance().getUserBean().getIsvstuser()==1){
                showActivity(activity, WLimitTimeFollowActivity.class);
            }else{
                showActivity(activity, LimitTimeFollowActivity.class);
            }

        }
    }
}
