package com.vst.vstsupport.control.arrears.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.mode.bean.LimitDetailMsgBean;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:聊天消息适配器
 */
public class LimitIMDetailMsgAdapter extends BaseAda<LimitDetailMsgBean> {
    public LimitIMDetailMsgAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LimitDetailMsgBean bean = getItem(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message_comunicated, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (!bean.SEND_ACCOUNT.equals(VstApplication.getInstance().getUserBean().getAccount())) {//发送者!=登录者,别人发过来的信息
            vh.left_type_loy.setVisibility(View.VISIBLE);
            vh.right_type_loy.setVisibility(View.GONE);
            vh.message_iv_icon.setImageResource(R.mipmap.saleman_head_icon);
            vh.message_time_tv.setText(bean.CREATE_DATE);
            vh.message_time_tv.setVisibility(TextUtils.isEmpty(bean.CREATE_DATE) ? View.GONE : View.VISIBLE);
            vh.message_name_tv.setText(bean.SEND_ACCOUNT);
            vh.message_des_tv.setText(bean.CONTENT);
        } else {
            vh.message_iv_icon_right.setImageResource(R.mipmap.product_head_icon);
            vh.left_type_loy.setVisibility(View.GONE);
            vh.right_type_loy.setVisibility(View.VISIBLE);
            vh.message_time_tv_right.setText(bean.CREATE_DATE);
            vh.message_time_tv_right.setVisibility(TextUtils.isEmpty(bean.CREATE_DATE) ? View.GONE : View.VISIBLE);
            vh.message_des_tv_right.setText(bean.CONTENT);
        }
        return convertView;


    }




    public class ViewHolder {

        public TextView message_time_tv;
        public TextView message_name_tv;
        public ImageView message_iv_icon;

        public TextView message_des_tv;

        //            right type
        public ImageView message_iv_icon_right;
        public TextView message_des_tv_right;
        public TextView message_time_tv_right;
        public RelativeLayout left_type_loy;
        public RelativeLayout right_type_loy;


        public ViewHolder(View convertView) {

            message_time_tv = (TextView) convertView.findViewById(R.id.message_time_tv);
            message_name_tv = (TextView) convertView.findViewById(R.id.message_name_tv);
            message_iv_icon = (ImageView) convertView.findViewById(R.id.message_iv_icon);
            message_des_tv = (TextView) convertView.findViewById(R.id.message_des_tv);
            message_des_tv_right = (TextView) convertView.findViewById(R.id.message_des_tv_right);
            message_iv_icon_right = (ImageView) convertView.findViewById(R.id.message_iv_icon_right);
            message_time_tv_right = (TextView) convertView.findViewById(R.id.message_time_tv_right);
            left_type_loy = (RelativeLayout) convertView.findViewById(R.id.left_type_loy);
            right_type_loy = (RelativeLayout) convertView.findViewById(R.id.right_type_loy);


        }
    }
}
