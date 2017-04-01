package com.vst.vstsupport.utils;

import android.graphics.Color;

import com.vst.vstsupport.common.TitleBarHelper;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/12/2 16:08
 * Description:
 */
public class RightTextUtil {

    public static void setRightTextStyle(TitleBarHelper mTitleBarHelper) {
        mTitleBarHelper.setRightVisible();
        mTitleBarHelper.setRightTextColor(Color.parseColor("#999999"));
        mTitleBarHelper.setRightMsg("已跟进");
        mTitleBarHelper.setOnRightClickListener(null);
    }
}
