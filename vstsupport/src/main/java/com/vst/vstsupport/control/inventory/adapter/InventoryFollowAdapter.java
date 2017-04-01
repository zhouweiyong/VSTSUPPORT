package com.vst.vstsupport.control.inventory.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.arrears.activity.FollowUpActivity;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.control.inventory.activity.InventoryDetailActivity;
import com.vst.vstsupport.control.inventory.activity.InventoryFollowActivity;
import com.vst.vstsupport.mode.bean.InventoryBean;
import com.vst.vstsupport.utils.DateTimeTool;
import com.vst.vstsupport.utils.FormatUtil;

import java.util.Date;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:库存跟进适配器
 */
public class InventoryFollowAdapter extends BaseAda<InventoryBean> {
    private boolean isOpen;
    InventoryFollowActivity activity;
    public InventoryFollowAdapter(Context context) {
        super(context);
        activity= (InventoryFollowActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.common_item_detail_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        InventoryBean bean = getItem(position);

//        if (bean.type.equals("流量")){
//
//        }else {
//            vh.name_tv.setText(bean.projectName);
//        }
        vh.name_tv.setText(bean.projectSerial);
        vh.second_line_tv.setText(bean.secendLevelLine);
        vh.count_total_tv.setText(FormatUtil.inventoryFunFormat(bean.totalMoney));
        vh.last_long_time_human_tv.setText(Html.fromHtml("<font color='#999999'>最长在库天数：</font>" + bean.totalday + "天<font color='#999999'> &nbsp&nbsp | &nbsp&nbsp 产品经理：</font>" + bean.pmName));
        vh.count_total_des_tv.setText("在库金额：");
        vh.dept_line_des.setVisibility(View.GONE);

//        vh.des_day01to15_tv.setText("0-60天");
//        vh.des_day16to30_tv.setText("61-90天");
        vh.des_day31to60_tv.setText("60-120天");
        vh.des_day60up_tv.setText("120天以上");

//        vh.day60up_tv.setText(TextUtils.isEmpty(bean.day0to60) ? "----" :"￥"+MoneyTool.commaSsymbolFormat( bean.day0to60));
//        vh.day60up_tv.setText(TextUtils.isEmpty(bean.day61to90) ? "----" :"￥"+ MoneyTool.commaSsymbolFormat(bean.day61to90));
        vh.day31to60_tv.setText(FormatUtil.inventoryFunFormat(bean.day60to120));
        vh.day60up_tv.setText(FormatUtil.inventoryFunFormat(bean.day120up));
        String currentDay= DateTimeTool.getWeekOfDate(new Date(activity.serverTime));
        if (VstApplication.getInstance().getUserBean().getPosition()==1) {//产品经理进入
            String weeks="星期五,星期六,星期日";
            if (weeks.contains(currentDay)) {//积压库存跟进填写只能周五到周日，其他时间需要隐藏跟进按钮，产品经理不能再填写，不允许非填写时间内再上线填写修改跟进情况，填写时间内不管产品经理是否填写，周一出报告后，按钮都变成“已跟进”
                if (bean.followFlag.equals("0")){//1表示跟进 0表示已跟进
                    setSaleFollowStyle(vh);
                }else{
                    vh.has_calculate_tv.setText("跟进");
                    vh.has_calculate_tv.setClickable(true);
                    vh.has_calculate_tv.setTextColor(Color.parseColor("#007fbe"));
                    vh.has_calculate_tv.setBackgroundResource(R.drawable.selector_limit_btn_bg);
                    vh.has_calculate_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, FollowUpActivity.class);
                            intent.putExtra("flag", "inventroy");
                            intent.putExtra("inventoryBean", getItem(position));
                            mContext.startActivity(intent);
                        }
                    });
                }
            }else{
                setSaleFollowStyle(vh);
            }
        } else {
            vh.has_calculate_tv.setText("问询");
            String weeks = "星期二,星期三,星期四，星期一";
            if (weeks.contains(currentDay)) {//积压库存的问询时间是周一至周四
                vh.has_calculate_tv.setText("问询");
                vh.has_calculate_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, InventoryDetailActivity.class);
                        intent.putExtra("InventoryBean", getItem(position));
                        mContext.startActivity(intent);
                    }
                });
            }else{
                vh.has_calculate_tv.setVisibility(View.GONE);
            }

        }


        final ViewHolder finalVh = vh;
        vh.open_close_loy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    finalVh.common_item_open_close_layout.setVisibility(View.GONE);
                    finalVh.open_close_iv.setImageResource(R.mipmap.common_down_icon);
                    isOpen = false;
                } else {
                    finalVh.common_item_open_close_layout.setVisibility(View.VISIBLE);
                    finalVh.open_close_iv.setImageResource(R.mipmap.common_up_icon);
                    isOpen = true;
                }
            }
        });
        return convertView;


    }

    private void setSaleFollowStyle(ViewHolder vh){
        vh.has_calculate_tv.setText("已跟进");
        vh.has_calculate_tv.setTextColor(Color.parseColor("#999999"));
        vh.has_calculate_tv.setBackgroundResource(R.drawable.selector_limit_btn_bg_unselect);
        if(vh.has_calculate_tv.isClickable()){
            vh.has_calculate_tv.setClickable(false);
        }
    }


    public class ViewHolder {

        public TextView name_tv;
        public TextView has_calculate_tv;
        public TextView second_line_tv;
        public TextView count_total_tv;
        public TextView count_total_des_tv;
        public TextView last_long_time_human_tv;
        public TextView company_dept_tv;
        public TextView dept_tv;
        public TextView dept_line_des;
        public TextView deal_tv;
        public TextView clear_time_tv;
        public ImageView open_close_iv;
        public LinearLayout common_item_open_close_layout;


        public TextView des_day01to15_tv;
        public TextView des_day16to30_tv;
        public TextView des_day31to60_tv;
        public TextView des_day60up_tv;

        public TextView day01to15_tv;
        public TextView day16to30_tv;
        public TextView day31to60_tv;
        public TextView day60up_tv;
        public LinearLayout genloy;
        public RelativeLayout open_close_loy;


        public ViewHolder(View convertView) {
            name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            has_calculate_tv = (TextView) convertView.findViewById(R.id.has_calculate_tv);
            second_line_tv = (TextView) convertView.findViewById(R.id.second_line_tv);
            count_total_tv = (TextView) convertView.findViewById(R.id.count_total_tv);
            count_total_des_tv = (TextView) convertView.findViewById(R.id.count_total_des_tv);
            last_long_time_human_tv = (TextView) convertView.findViewById(R.id.last_long_time_human_tv);
            company_dept_tv = (TextView) convertView.findViewById(R.id.company_dept_tv);
            dept_tv = (TextView) convertView.findViewById(R.id.dept_tv);
            deal_tv = (TextView) convertView.findViewById(R.id.deal_tv);
            dept_line_des = (TextView) convertView.findViewById(R.id.dept_line_des);
            clear_time_tv = (TextView) convertView.findViewById(R.id.clear_time_tv);
            open_close_iv = (ImageView) convertView.findViewById(R.id.open_close_iv);
            common_item_open_close_layout = (LinearLayout) convertView.findViewById(R.id.common_item_open_close_layout);

            des_day01to15_tv = (TextView) convertView.findViewById(R.id.des_day0to60_tv);
            des_day16to30_tv = (TextView) convertView.findViewById(R.id.des_day61to90_tv);
            des_day31to60_tv = (TextView) convertView.findViewById(R.id.des_day91to120_tv);
            des_day60up_tv = (TextView) convertView.findViewById(R.id.des_day120up_tv);

            day01to15_tv = (TextView) convertView.findViewById(R.id.day0to60_tv);
            day16to30_tv = (TextView) convertView.findViewById(R.id.day61to90_tv);
            day31to60_tv = (TextView) convertView.findViewById(R.id.day91to120_tv);
            day60up_tv = (TextView) convertView.findViewById(R.id.day120up_tv);
            genloy = (LinearLayout) convertView.findViewById(R.id.genloy);
            open_close_loy = (RelativeLayout) convertView.findViewById(R.id.open_close_loy);

            company_dept_tv.setVisibility(View.GONE);
            dept_tv.setVisibility(View.GONE);
            clear_time_tv.setVisibility(View.GONE);
            deal_tv.setVisibility(View.GONE);

        }
    }
}
