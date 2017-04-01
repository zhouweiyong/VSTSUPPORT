package com.vst.vstsupport.mode.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/14
 * class description:请输入类描述
 */
public class MainPageEvent implements Parcelable {
    /**
     * 0消息
     * 1应收账款
     * 2库存
     * 3设置
     */
    private int pageIndex;

    public MainPageEvent(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pageIndex);
    }

    protected MainPageEvent(Parcel in) {
        this.pageIndex = in.readInt();
    }

    public static final Parcelable.Creator<MainPageEvent> CREATOR = new Parcelable.Creator<MainPageEvent>() {
        @Override
        public MainPageEvent createFromParcel(Parcel source) {
            return new MainPageEvent(source);
        }

        @Override
        public MainPageEvent[] newArray(int size) {
            return new MainPageEvent[size];
        }
    };
}
