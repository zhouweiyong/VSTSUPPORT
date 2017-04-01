package com.vst.vstsupport.control.message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.control.message.activity.CommonMsgDetailActivity;
import com.vst.vstsupport.mode.bean.Into90InvItemBean;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:即将到期适配器
 */
public class Into90InvAdapter extends BaseAda<Into90InvItemBean> {

    private CommonMsgDetailActivity activity;

    public Into90InvAdapter(Context context) {
        super(context);
        activity = (CommonMsgDetailActivity) context;
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
        Into90InvItemBean bean = getItem(position);

        vh.has_calculate_tv.setVisibility(View.GONE);
        vh.count_total_des_tv.setText("库 存 数 量  ");
        vh.count_expiring_des_tv.setText("库 存 分 布  ");
        vh.count_distance_des_day_loy.setVisibility(View.GONE);

        vh.name_tv.setText(bean.getSecendLevelLine());
        vh.second_line_tv.setText(bean.getProjectName());
        vh.count_total_tv.setText(String.valueOf(bean.getNum()));
        vh.count_expiring_total_tv.setText(bean.getArea());
        return convertView;
    }


    public class ViewHolder {

        public TextView time_tv;
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


        public ViewHolder(View convertView) {

            time_tv = (TextView) convertView.findViewById(R.id.time_tv);
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
        }
    }
}
