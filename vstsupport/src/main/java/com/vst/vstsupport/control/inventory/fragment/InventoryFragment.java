package com.vst.vstsupport.control.inventory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.arrears.activity.CheckReceiveActivity;
import com.vst.vstsupport.control.base.BaseFra;
import com.vst.vstsupport.control.inventory.activity.InventoryFollowActivity;
import com.vstecs.android.funframework.eventbus.EventCenter;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/13
 * class description:库存积压
 */
public class InventoryFragment extends BaseFra {

    private TextView inventory_follow_up_tv;
    private TextView inventory_look_tv;
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
        return inflater.inflate(R.layout.frg_inventory_layout,container,false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        inventory_follow_up_tv= (TextView) parentView.findViewById(R.id.inventory_follow_up_tv);
        inventory_look_tv= (TextView) parentView.findViewById(R.id.inventory_look_tv);
        inventory_follow_up_tv.setOnClickListener(this);
        inventory_look_tv.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        super.widgetClick(v);
        if (v.getId()==R.id.inventory_look_tv){
            Intent intent=new Intent(activity,CheckReceiveActivity.class);
            intent.putExtra("flag","inventory");
            showActivity(activity, intent);
        }else if(v.getId()==R.id.inventory_follow_up_tv){
            showActivity(activity, InventoryFollowActivity.class);
        }
    }
}
