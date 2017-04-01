package com.vst.vstsupport.control.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAda<T> extends BaseAdapter {
    protected List<T> group = new ArrayList<T>();
    protected Context mContext;
    protected LayoutInflater mInflater;
//	protected DisplayImageOptions options;

    public BaseAda(Context context) {
        super();
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return (group == null) ? 0 : group.size();
    }

    @Override
    public T getItem(int position) {
        /**防止外部调用时越界*/
        if (group == null || position < 0 || position > group.size()-1)
            return null;
        return group.get(position);
    }

    public void removeItem(int position) {
        if (group == null || position < 0 || position > group.size())
            return;
        group.remove(position);
        notifyDataSetChanged();
        return;
    }

//	protected void displayImg(String url,ImageView imageView){
//		ImageLoader.getInstance().displayImage(url, imageView,options);
//	}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return (group == null) ? true : group.isEmpty();
    }

    public void setGroup(List<T> g) {
        if (group != null)
            group.clear();
        group.addAll(g);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        group.add(item);
        notifyDataSetChanged();
    }

    /**
     * 添加指定位置的item
     */
    public void addItem(int position, T item) {
        group.add(position, item);
        notifyDataSetChanged();
    }

    public void addItemNoNotify(T item) {
        group.add(item);
    }

    public void addItems(List<T> items) {
        if (items != null) {
            group.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clearGroup(boolean notify) {
        if (group != null) {
            group.clear();
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }

}
