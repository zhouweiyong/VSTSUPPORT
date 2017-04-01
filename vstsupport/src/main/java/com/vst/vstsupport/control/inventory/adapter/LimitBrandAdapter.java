package com.vst.vstsupport.control.inventory.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vst.vstsupport.R;
import com.vst.vstsupport.control.base.BaseAda;
import com.vst.vstsupport.mode.bean.BrandBean;

/**
 * Author:  Chen.yuan
 * Email:   1650737154@qq.com
 * Date:    ${Date}
 * Description:库存积压情况下的事业部适配器
 */
public class LimitBrandAdapter extends BaseAda<BrandBean> {

    public LimitBrandAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.common_select_layout, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.select_tv.setText(getItem(position).brandName);

        return convertView;


    }


    public class ViewHolder {

        public TextView select_tv;


        public ViewHolder(View convertView) {

            select_tv= (TextView) convertView.findViewById(R.id.select_tv);
        }
    }
}
