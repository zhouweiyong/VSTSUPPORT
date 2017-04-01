package com.vst.vstsupport.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/12/15 11:08
 * Description:
 */
public class FormatUtil {

    public static CharSequence  inventoryFunFormat(CharSequence str){
        SpannableString ss=new SpannableString(MoneyTool.commaSsymbolFormatNoFloat(str.toString())+" 千元");
        ss.setSpan(new RelativeSizeSpan(0.8f),ss.length()-2,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return TextUtils.isEmpty(str) ||str.equals("0")? "----" :ss;
    }


    public static String  limitFunFormat(String str){
        return TextUtils.isEmpty(str) ||str.equals("0.00")? "----" :"￥"+MoneyTool.commaSsymbolFormat( str );
    }
}
