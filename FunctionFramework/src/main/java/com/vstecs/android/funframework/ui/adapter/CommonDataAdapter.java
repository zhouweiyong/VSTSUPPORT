package com.vstecs.android.funframework.ui.adapter;


import java.util.ArrayList;
import java.util.List;

/**
 * A adapter using View Holder to display the item of a list view;
 *
 * @param <ItemDataType>
 * @author http://www.liaohuqiu.net
 */
public class CommonDataAdapter<ItemDataType> extends ListViewDataAdapterBase<ItemDataType> {

    protected ArrayList<ItemDataType> mItemDataList = new ArrayList<ItemDataType>();

    public CommonDataAdapter() {

    }

    /**
     * @param viewHolderCreator The view holder creator will create a View Holder that extends {@link ViewHolderBase}
     */
    public CommonDataAdapter(ViewHolderCreator<ItemDataType> viewHolderCreator) {
        super(viewHolderCreator);
    }

    public ArrayList<ItemDataType> getDataList() {
        return mItemDataList;
    }

    @Override
    public int getCount() {
        return (mItemDataList == null) ? 0 : mItemDataList.size();
    }

    @Override
    public ItemDataType getItem(int position) {
        if (mItemDataList == null || mItemDataList.size() <= position || position < 0) {
            return null;
        }
        return mItemDataList.get(position);
    }

    public void removeItem(int position) {
        if (mItemDataList == null || position < 0 || position > mItemDataList.size())
            return;
        mItemDataList.remove(position);
        notifyDataSetChanged();
        return;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return (mItemDataList == null) ? true : mItemDataList.isEmpty();
    }

    public void setGroup(List<ItemDataType> g) {
        if (mItemDataList != null)
            mItemDataList.clear();
        mItemDataList.addAll(g);
        notifyDataSetChanged();
    }

    public void addItem(ItemDataType item) {
        mItemDataList.add(item);
        notifyDataSetChanged();
    }

    /**
     * 添加指定位置的item
     */
    public void addItem(int position, ItemDataType item) {
        mItemDataList.add(position, item);
        notifyDataSetChanged();
    }

    public void addItemNoNotify(ItemDataType item) {
        mItemDataList.add(item);
    }

    public void addItems(List<ItemDataType> items) {
        if (items != null) {
            mItemDataList.addAll(items);
            notifyDataSetChanged();
        }
    }

    public void clearGroup(boolean notify) {
        if (mItemDataList != null) {
            mItemDataList.clear();
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }
}

