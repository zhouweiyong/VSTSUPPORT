package com.vst.vstsupport.control.message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.control.message.activity.CommonMsgDetailActivity;
import com.vst.vstsupport.mode.bean.ReceivableItemBean;
import com.vst.vstsupport.utils.NumUtils;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:即将到期适配器
 */
public class CommonMsgSimpleAdapter extends BaseAda<ReceivableItemBean> {

    private CommonMsgDetailActivity activity;

    public CommonMsgSimpleAdapter(Context context) {
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
        ReceivableItemBean bean = getItem(position);

        vh.name_tv.setText(bean.getCustomerName());
        vh.second_line_tv.setText(bean.getSecendLevelLine());
        vh.count_total_tv.setText(String.format("¥ %s", NumUtils.formatNum(bean.getMoney()) ));
        vh.count_expiring_total_tv.setText(bean.getExpireDay());

        if (VstApplication.getInstance().getUserBean().getIsvstuser()==1){
            vh.count_distance_des_day_loy.setVisibility(View.GONE);
            vh.expire_date_layout.setVisibility(View.GONE);
            vh.iv_expire_3days.setVisibility(View.VISIBLE);
        }else {
            int day = Math.abs(Integer.parseInt(bean.getDays()));
            vh.count_distance_day_tv.setText(String.format("%d 天", day));
            if (day <= 5) {
                vh.has_calculate_tv.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }


    public class ViewHolder {

        public TextView name_tv;
        public TextView has_calculate_tv;
        public ImageView iv_expire_3days;
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
        public RelativeLayout expire_date_layout;


        public ViewHolder(View convertView) {

            name_tv = (TextView) convertView.findViewById(R.id.name_tv);
            has_calculate_tv = (TextView) convertView.findViewById(R.id.has_calculate_tv);
            iv_expire_3days = (ImageView) convertView.findViewById(R.id.iv_expire_3days);

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
            expire_date_layout = (RelativeLayout) convertView.findViewById(R.id.expire_date_layout);

        }
    }
}
