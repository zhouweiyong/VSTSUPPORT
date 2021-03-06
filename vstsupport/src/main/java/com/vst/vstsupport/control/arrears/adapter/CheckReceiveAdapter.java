package com.vst.vstsupport.control.arrears.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.arrears.activity.CheckReceiveActivity;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.mode.bean.InventoryLookUpBean;
import com.vst.vstsupport.utils.FormatUtil;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:
 */
public class CheckReceiveAdapter extends BaseAda<InventoryLookUpBean> {

    private CheckReceiveActivity activity;

    public CheckReceiveAdapter(Context context) {
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
        InventoryLookUpBean bean = getItem(position);
//        vh.time_tv.setVisibility(View.GONE);
        vh.name_tv.setText(bean.productName);
        vh.second_line_tv.setText(bean.secendLevelLine);


        //库存查看的情况
        if (!activity.flag.equals("limit")) {
            vh.count_total_des_tv.setText("库 存 金 额");
            vh.count_total_tv.setText(FormatUtil.inventoryFunFormat(bean.money));
            vh.count_expiring_des_tv.setText("产 品 数 量");
            vh.count_expiring_total_tv.setText(bean.productNum);
            vh.count_distance_des_day_tv.setText("入 库 时 间");
            vh.count_distance_day_tv.setText(bean.date);
            vh.in_db_des_day_loy.setVisibility(View.VISIBLE);
            vh.in_db_des_day_tv.setText("在 库 天 数");
            vh.in_db_day_tv.setText(bean.days + "天");
            vh.has_calculate_tv.setVisibility(View.GONE);

            //共用布局，库存查看的情况显示名称和二级线相反
            vh.name_tv.setText(bean.secendLevelLine);
            vh.second_line_tv.setText(bean.productName);
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
        public ImageView open_close_iv;


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
            open_close_iv = (ImageView) convertView.findViewById(R.id.open_close_iv);
            open_close_iv.setVisibility(View.GONE);
        }
    }
}
