package com.vst.vstsupport.control.message.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.VstApplication;
import com.vst.vstsupport.mode.bean.TuiMessageBean;
import com.vst.vstsupport.utils.MoneyTool;
import com.vst.vstsupport.view.swipemenulistview.BaseSwipListAdapter;
import com.vstecs.android.funframework.utils.DensityUtils;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/18
 * class description:请输入类描述
 */
public class MessageAdapter extends BaseSwipListAdapter<TuiMessageBean> {

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_smlv_message, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        TuiMessageBean tuiMessageBean = getItem(position);
        vh.tv_title_ism.setText(tuiMessageBean.getTitle());

        vh.tv_time_ism.setText(tuiMessageBean.getCreateDateTime());
        vh.tv_num_ism.setText("");

        //消息数字显示
        if (tuiMessageBean.isShowNumTag()) {
            vh.tv_num_ism.setVisibility(View.VISIBLE);
        } else {
            vh.tv_num_ism.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams lp = vh.tv_num_ism.getLayoutParams();
        lp.width = DensityUtils.dip2px(mContext, 11);
        lp.height = DensityUtils.dip2px(mContext, 11);
        vh.tv_second_title_ism.setVisibility(View.GONE);
        switch (tuiMessageBean.getMessageId()) {
            case 1://即将到期
                vh.tv_content_ism.setText(String.format("您有%d条即将到期的应收账款", tuiMessageBean.getReturnCount()));
                vh.iv_ism.setImageResource(R.mipmap.ic_expire_msg);
                vh.tv_num_ism.setBackgroundResource(R.mipmap.msg_no_num_bg);
                vh.tv_num_ism.setLayoutParams(lp);
                break;
            case 2://超期跟进
                vh.tv_content_ism.setText(String.format("您有%d条没有跟进的超期", tuiMessageBean.getReturnCount()));
                vh.iv_ism.setImageResource(R.mipmap.ic_follow_msg);
                vh.tv_num_ism.setBackgroundResource(R.mipmap.msg_no_num_bg);
                vh.tv_num_ism.setLayoutParams(lp);
                break;
            case 3://总库存金额
                vh.tv_num_ism.setBackgroundResource(R.mipmap.msg_no_num_bg);
                vh.tv_num_ism.setLayoutParams(lp);
                vh.iv_ism.setImageResource(R.mipmap.ic_inventory_all);
                vh.tv_content_ism.setText(String.format("总库存金额为%s千元", TextUtils.isEmpty(tuiMessageBean.getTotalmoney()) ? "0" : MoneyTool.commaSsymbolFormat(tuiMessageBean.getTotalmoney())));
                break;
            case 4://90天以上库存
                vh.tv_content_ism.setText(String.format("您有%d条90天以上库存信息", tuiMessageBean.getReturnCount()));
                vh.tv_num_ism.setBackgroundResource(R.mipmap.msg_no_num_bg);
                vh.tv_num_ism.setLayoutParams(lp);
                vh.iv_ism.setImageResource(R.mipmap.ic_inventory_90);
                break;
            case 5://回复
                leadAsk(vh,tuiMessageBean);
                vh.tv_title_ism.setText(tuiMessageBean.getCustomerName());
                vh.tv_second_title_ism.setVisibility(View.VISIBLE);
                vh.tv_second_title_ism.setText(VstApplication.getInstance().getUserBean().getIsvstuser()==1?tuiMessageBean.getBrandName():tuiMessageBean.getSecendLevelLine());
                break;
            case 6:
                leadAsk(vh, tuiMessageBean);
                vh.tv_title_ism.setText(tuiMessageBean.getSecendLevelLine());
                vh.tv_second_title_ism.setVisibility(View.VISIBLE);
                vh.tv_second_title_ism.setText(tuiMessageBean.getProjectSerial());
                break;
            case 7:
                leadAsk(vh,tuiMessageBean);
                vh.tv_title_ism.setText(tuiMessageBean.getProjectSerial());
                vh.tv_second_title_ism.setVisibility(View.VISIBLE);
                vh.tv_second_title_ism.setText(tuiMessageBean.getSecendLevelLine());
                break;
        }


        if (position == getCount() - 1) {
            vh.line_ism.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void leadAsk(ViewHolder vh,TuiMessageBean tuiMessageBean){
        ViewGroup.LayoutParams lp = vh.tv_num_ism.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        vh.tv_num_ism.setBackgroundResource(R.mipmap.msg_num_bg);

        if (tuiMessageBean.isShowNumTag() && tuiMessageBean.getNoReaderCount() < 1) {
            vh.tv_num_ism.setVisibility(View.GONE);
        }else {
            vh.tv_num_ism.setText(String.valueOf(tuiMessageBean.getNoReaderCount()));
        }
        vh.tv_content_ism.setText(tuiMessageBean.getContent());
        if (tuiMessageBean.getIdentityType() == 0) {
            vh.iv_ism.setImageResource(R.mipmap.ic_sale_msg);
        } else if (tuiMessageBean.getIdentityType() == 1) {
            vh.iv_ism.setImageResource(R.mipmap.ic_pm_msg);
        } else {
            vh.iv_ism.setImageResource(R.mipmap.ic_leader_msg);
        }
    }


    class ViewHolder {
        public View rootView;
        public ImageView iv_ism;
        public TextView tv_title_ism;
        public TextView tv_content_ism;
        public TextView tv_time_ism;
        public TextView tv_num_ism;
        public View line_ism;
        public TextView tv_second_title_ism;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_ism = (ImageView) rootView.findViewById(R.id.iv_ism);
            this.tv_title_ism = (TextView) rootView.findViewById(R.id.tv_title_ism);
            this.tv_second_title_ism = (TextView) rootView.findViewById(R.id.tv_second_title_ism);
            this.tv_content_ism = (TextView) rootView.findViewById(R.id.tv_content_ism);
            tv_time_ism = (TextView) rootView.findViewById(R.id.tv_time_ism);
            tv_num_ism = (TextView) rootView.findViewById(R.id.tv_num_ism);
            line_ism = rootView.findViewById(R.id.line_ism);
        }
    }
}
