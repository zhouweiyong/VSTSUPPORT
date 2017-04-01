package com.vst.vstsupport.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/8/8
 * class description:请输入类描述
 */
public class NumUtils {
    private static NumberFormat nf1 = NumberFormat.getInstance(Locale.CHINA);
    private static DecimalFormat df1 = new DecimalFormat("###,###,###,##0.#########");

    public static String  formatNum(double num){
        String str = df1.format(num);
//        String pbStr = str.substring(str.indexOf(".")+1);
//        if (pbStr.length()>0&&pbStr.length()<2){
//            str = String.format("%s0",str);
//        }
        return str;
    }


}
