package com.vst.vstsupport.control.arrears.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.arrears.activity.CheckReceiveActivity;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.mode.bean.LimitCheckReceiveBean;
import com.vst.vstsupport.utils.FormatUtil;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:应收查看适配器
 */
public class LimitCheckReceiveAdapter extends BaseAda<LimitCheckReceiveBean> {
    private boolean isOpen;
    private CheckReceiveActivity activity;

    public LimitCheckReceiveAdapter(Context context) {
        super(context);
        activity = (CheckReceiveActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.common_item_msg_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        LimitCheckReceiveBean bean = getItem(position);
//        vh.time_tv.setVisibility(View.GONE);
        vh.name_tv.setText(bean.customerName);
        vh.second_line_tv.setText(bean.secendLevelLine);
        vh.count_total_tv.setText(FormatUtil.limitFunFormat(bean.money));
        vh.count_expiring_total_tv.setText(bean.expireDay);
        vh.count_distance_day_tv.setText(Math.abs(bean.days)+"天");


        vh.dept_tv.setText(bean.businessDepartment);
        vh.aere_tv.setText(bean.area);
        vh.sale_name_tv.setText(bean.saleName);

        if (bean.days<=0){
            if (bean.days<=-1&&bean.days>-6){
                vh.count_distance_des_day_tv.setText("距离到期日");
                vh.has_calculate_tv.setVisibility(View.VISIBLE);
            }else{
                vh.count_distance_des_day_tv.setText("距离到期日");
                vh.has_calculate_tv.setVisibility(View.GONE);
            }

        }else {
            vh.count_distance_des_day_tv.setText("逾 期 天 数 ");
            vh.has_calculate_tv.setVisibility(View.GONE);
        }


        final ViewHolder finalVh = vh;
        if (activity.operation.equals("ask")) {//超期跟进领导的情况
//            finalVh.common_item_open_close_layout.setVisibility(View.VISIBLE);
            finalVh.open_close_iv.setVisibility(View.VISIBLE);

            vh.count_distance_des_day_loy.setOnClickListener(new View.OnClickListener() {
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

        }else{
            finalVh.common_item_open_close_layout.setVisibility(View.GONE);
            finalVh.open_close_iv.setVisibility(View.GONE);
        }




        return convertView;


    }


    public class ViewHolder {

//        public TextView time_tv;
        public TextView name_tv;
        public TextView has_calculate_tv;
        public TextView second_line_tv;
        public TextView count_total_tv;
        public TextView count_expiring_total_tv;
        public TextView count_distance_day_tv;
        public TextView count_distance_des_day_tv;
        public TextView count_total_des_tv;
        public TextView count_expiring_des_tv;
        public RelativeLayout in_db_des_day_loy;
        public TextView in_db_des_day_tv;
        public TextView in_db_day_tv;
        public RelativeLayout count_distance_des_day_loy;
        public LinearLayout common_item_open_close_layout;
        public ImageView open_close_iv;

        public TextView des_area_tv;
        public TextView aere_tv;
        public TextView des_dept_tv;
        public TextView dept_tv;
        public TextView sale_man_tv;
        public TextView sale_name_tv;



        public ViewHolder(View convertView) {

//            time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            has_calculate_tv = (TextView) convertView.findViewById(R.id.has_calculate_tv);
            second_line_tv = (TextView) convertView.findViewById(R.id.second_line_tv);
            count_total_tv = (TextView) convertView.findViewById(R.id.count_total_tv);
            count_expiring_total_tv = (TextView) convertView.findViewById(R.id.count_expiring_total_tv);
            count_distance_day_tv = (TextView) convertView.findViewById(R.id.count_distance_day_tv);
            count_distance_des_day_tv = (TextView) convertView.findViewById(R.id.count_distance_des_day_tv);
            count_total_des_tv = (TextView) convertView.findViewById(R.id.count_total_des_tv);
            count_expiring_des_tv = (TextView) convertView.findViewById(R.id.count_expiring_des_tv);
            in_db_des_day_loy = (RelativeLayout) convertView.findViewById(R.id.in_db_des_day_loy);
            in_db_des_day_tv = (TextView) convertView.findViewById(R.id.in_db_des_day_tv);
            in_db_day_tv = (TextView) convertView.findViewById(R.id.in_db_day_tv);
            count_distance_des_day_loy = (RelativeLayout) convertView.findViewById(R.id.count_distance_des_day_loy);
            common_item_open_close_layout = (LinearLayout) convertView.findViewById(R.id.common_item_open_close_layout);
            open_close_iv = (ImageView) convertView.findViewById(R.id.open_close_iv);


            des_area_tv = (TextView) convertView.findViewById(R.id.des_area_tv);
            aere_tv = (TextView) convertView.findViewById(R.id.aere_tv);
            des_dept_tv = (TextView) convertView.findViewById(R.id.des_dept_tv);
            dept_tv = (TextView) convertView.findViewById(R.id.dept_tv);
            sale_man_tv = (TextView) convertView.findViewById(R.id.sale_man_tv);
            sale_name_tv = (TextView) convertView.findViewById(R.id.sale_name_tv);
        }
    }
}
