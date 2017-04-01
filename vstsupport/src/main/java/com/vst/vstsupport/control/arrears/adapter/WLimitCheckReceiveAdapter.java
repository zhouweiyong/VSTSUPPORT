package com.vst.vstsupport.control.arrears.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.mode.bean.WLimitCheckReceiveBean;
import com.vst.vstsupport.utils.MoneyTool;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:
 */
public class WLimitCheckReceiveAdapter extends BaseAda<WLimitCheckReceiveBean> {


    public WLimitCheckReceiveAdapter(Context context) {
        super(context);
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
        WLimitCheckReceiveBean bean = getItem(position);
        vh.name_tv.setText(bean.customerName);
        vh.second_line_tv.setText(bean.brandName);
        vh.count_total_tv.setText("￥" + MoneyTool.commaSsymbolFormat(bean.money));

        if (bean.flag.equals("1")){
            vh.iv_expire_3days.setVisibility(View.VISIBLE);
            vh.count_total_des_tv.setText("应 收 余 额");
        }else{
            vh.iv_expire_3days.setVisibility(View.GONE);
            vh.count_total_des_tv.setText("超 期 余 额");
        }
        return convertView;


    }


    public class ViewHolder {

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
        public RelativeLayout expire_date_layout;
        public RelativeLayout count_distance_des_day_loy;
        public TextView in_db_des_day_tv;
        public TextView in_db_day_tv;
        public ImageView iv_expire_3days;


        public ViewHolder(View convertView) {

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
            expire_date_layout = (RelativeLayout) convertView.findViewById(R.id.expire_date_layout);
            count_distance_des_day_loy = (RelativeLayout) convertView.findViewById(R.id.count_distance_des_day_loy);
            in_db_des_day_tv = (TextView) convertView.findViewById(R.id.in_db_des_day_tv);
            in_db_day_tv = (TextView) convertView.findViewById(R.id.in_db_day_tv);
            iv_expire_3days = (ImageView) convertView.findViewById(R.id.iv_expire_3days);
            expire_date_layout.setVisibility(View.GONE);
            count_distance_des_day_loy.setVisibility(View.GONE);
        }
    }
}
