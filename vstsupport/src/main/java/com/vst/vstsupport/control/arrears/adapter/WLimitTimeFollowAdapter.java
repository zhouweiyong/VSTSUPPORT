package com.vst.vstsupport.control.arrears.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.arrears.activity.FollowUpActivity;
import com.vst.vstsupport.control.arrears.activity.WLimitDetailActivity;
import com.vst.vstsupport.control.arrears.activity.WLimitTimeFollowActivity;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.mode.bean.WLimitBean;
import com.vst.vstsupport.utils.DateTimeTool;
import com.vst.vstsupport.utils.FormatUtil;

import java.util.Date;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:
 */
public class WLimitTimeFollowAdapter extends BaseAda<WLimitBean> {
    WLimitTimeFollowActivity activity;
    private boolean isOpen;

    public WLimitTimeFollowAdapter(Context context) {
        super(context);
        activity = (WLimitTimeFollowActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.limit_item_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        WLimitBean bean = getItem(position);

        vh.name_tv.setText(bean.customerName);
        vh.second_line_tv.setText(bean.brandName);
        vh.count_total_tv.setText(FormatUtil.limitFunFormat(bean.totalOverdueMoney));
        vh.last_long_time_human_tv.setText(Html.fromHtml("<font color='#999999'>业务员：</font>" + bean.saleName));

        vh.des_day01to15_tv.setText("1-7天");
        vh.des_day16to30_tv.setText("8-15天");
        vh.des_day31to60_tv.setText("15天以上");
        vh.des_day60up_tv.setText("30天以上");

        vh.day01to15_tv.setText(FormatUtil.limitFunFormat(bean.day1to7));
        vh.day16to30_tv.setText(FormatUtil.limitFunFormat(bean.day8to15));
        vh.day31to60_tv.setText(FormatUtil.limitFunFormat(bean.day15up));
        vh.day60up_tv.setText(FormatUtil.limitFunFormat(bean.day30up));


        vh.company_dept_tv.setVisibility(View.GONE);
        vh.dept_tv.setVisibility(View.GONE);
        vh.dept_line_des.setVisibility(View.GONE);
        String currentDay = DateTimeTool.getWeekOfDate(new Date(activity.serverTime));
        if (activity.operation.equals("follow")) {//销售进来
            vh.deal_tv.setVisibility(View.GONE);
            vh.deal_des_tv.setVisibility(View.GONE);

            vh.clear_time_tv.setVisibility(View.GONE);

            String weeks = "星期一,星期六,星期日";
            if (weeks.contains(currentDay)) {//销售于周六、周日、周一可在线点“跟进”，填写 “解决方案”和“清理时间”。其他时间隐藏按钮
                if (!TextUtils.isEmpty(bean.solution)){
                    setSaleFollowStyle(vh);
                }else{
                    vh.has_calculate_tv.setText("跟进");
                    vh.has_calculate_tv.setTextColor(Color.parseColor("#007fbe"));
                    vh.has_calculate_tv.setBackgroundResource(R.drawable.selector_limit_btn_bg);
                    vh.has_calculate_tv.setClickable(true);
                    vh.has_calculate_tv.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, FollowUpActivity.class);
                            intent.putExtra("mWLimitBean", getItem(position));
                            intent.putExtra("flag", "limit_look_w");
                            mContext.startActivity(intent);
                        }
                    });
                }
            }else{
//                vh.has_calculate_tv.setVisibility(View.GONE);
                setSaleFollowStyle(vh);
            }

        } else {
            setView(vh, bean);
            String weeks = "星期二,星期三,星期四，星期五";
            if (weeks.contains(currentDay)) {//超期问询时间：周二至周五
                vh.has_calculate_tv.setText("问询");
                vh.has_calculate_tv.setTextColor(Color.parseColor("#007fbe"));
                vh.has_calculate_tv.setBackgroundResource(R.drawable.selector_limit_btn_bg);
                vh.has_calculate_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, WLimitDetailActivity.class);
                        intent.putExtra("WLimitBean", getItem(position));
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
                    isOpen = true;
                    finalVh.open_close_iv.setImageResource(R.mipmap.common_up_icon);
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

    private void setView(ViewHolder vh, final WLimitBean bean) {

        vh.deal_tv.setVisibility(View.VISIBLE);
        vh.deal_des_tv.setVisibility(View.VISIBLE);
        vh.clear_time_tv.setVisibility(View.VISIBLE);
        vh.has_calculate_tv.setVisibility(View.VISIBLE);

//        vh.company_dept_tv.setText(Html.fromHtml("<font color='#999999'>分 &nbsp&nbsp 公  &nbsp&nbsp&nbsp  司&nbsp&nbsp  ：</font>" + bean.branchCompany));
//        vh.dept_tv.setText(Html.fromHtml("<font color='#999999'>事   &nbsp&nbsp       业  &nbsp&nbsp&nbsp 部&nbsp&nbsp ：</font>" + bean.businessDepartment));

        vh.deal_tv.setText(Html.fromHtml("" + (TextUtils.isEmpty(bean.solution) ? "暂未填写" : bean.solution)));

        vh.clear_time_tv.setText(Html.fromHtml("<font color='#999999'>清理时间 ：&nbsp&nbsp</font>" + (TextUtils.isEmpty(bean.dealTime) ? "暂未填写" : bean.dealTime)));
        vh.has_calculate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WLimitDetailActivity.class);
                intent.putExtra("WLimitBean", bean);
                mContext.startActivity(intent);

            }
        });
    }


    public class ViewHolder {

        public TextView name_tv;
        public TextView has_calculate_tv;
        public TextView second_line_tv;
        public TextView count_total_tv;
        public TextView last_long_time_human_tv;
        public TextView company_dept_tv;
        public TextView deal_tv;
        public TextView deal_des_tv;
        public TextView dept_tv;
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
        public TextView dept_line_des;
        public RelativeLayout open_close_loy;


        public ViewHolder(View convertView) {
            name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            has_calculate_tv = (TextView) convertView.findViewById(R.id.has_calculate_tv);
            second_line_tv = (TextView) convertView.findViewById(R.id.second_line_tv);
            count_total_tv = (TextView) convertView.findViewById(R.id.count_total_tv);
            last_long_time_human_tv = (TextView) convertView.findViewById(R.id.last_long_time_human_tv);
            company_dept_tv = (TextView) convertView.findViewById(R.id.company_dept_tv);
            deal_tv = (TextView) convertView.findViewById(R.id.deal_tv);
            deal_des_tv = (TextView) convertView.findViewById(R.id.deal_des_tv);
            dept_tv = (TextView) convertView.findViewById(R.id.dept_tv);
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
            dept_line_des = (TextView) convertView.findViewById(R.id.devide_line);
            open_close_loy = (RelativeLayout) convertView.findViewById(R.id.open_close_loy);

        }
    }
}
