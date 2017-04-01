package com.vst.vstsupport.utils;

import java.text.DecimalFormat;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/8/9 14:59
 * Description:金额的格式化工具
 */
public class MoneyTool {
    private static DecimalFormat decimalFormat = new DecimalFormat("#,##0");

    /**
     * 整数部分每三位一个逗号隔开，小数部分原生不动
     * @param str 没有逗号的字符串
     * @return 每三位一个逗号隔开
     */
    public static String commaSsymbolFormat(String str){

        if(str.contains(".")){
            String []aString=str.split("\\.");
            if (aString[1].length()>=2){
                return decimalFormat.format(Long.parseLong(aString[0]))+"."+aString[1].substring(0,2);
            }else{
                return decimalFormat.format(Long.parseLong(aString[0]))+"."+aString[1]+"0";
            }

        }else{
//            return decimalFormat.format(Long.parseLong(str))+".00";
            return decimalFormat.format(Long.parseLong(str));
        }

//        String fianlStr="";
//        if(!str.contains(".")){
////            fianlStr=Math.abs(Long.parseLong(str)*1000)+"";
//            fianlStr=Long.parseLong(str)*1000+"";
//        }else{
////            String temp=Math.abs(Double.parseDouble(str)*1000)+"";
//            String temp=Double.parseDouble(str)*1000+"";
//            BigDecimal bigDecimal=new BigDecimal(temp);
//            fianlStr= bigDecimal.toPlainString();
//        }
//        if(fianlStr.contains(".")){
//            String []aString=fianlStr.split("\\.");
//            if (aString[1].length()>=2){
//                return decimalFormat.format(Long.parseLong(aString[0]))+"."+aString[1].substring(0,2);
//            }else{
//                return decimalFormat.format(Long.parseLong(aString[0]))+"."+aString[1]+"0";
//            }
//
//        }else{
//            return decimalFormat.format(Long.parseLong(fianlStr))+".00";
//        }

    }

//    /**
//     * 整数部分每三位一个逗号隔开，小数部分原生不动
//     * @param str 没有逗号的字符串
//     * @return 每三位一个逗号隔开
//     */
//    public static String commaSsymbolFormat(String str, boolean isNeedMulti){
//        String fianlStr="";
//        if (isNeedMulti){
//            if(!str.contains(".")){
////                fianlStr=Math.abs(Long.parseLong(str)*1000)+"";
//                fianlStr=Long.parseLong(str)*1000+"";
//            }else{
////                String temp=Math.abs(Double.parseDouble(str)*1000)+"";
//                String temp=Double.parseDouble(str)*1000+"";
//                BigDecimal bigDecimal=new BigDecimal(temp);
//                fianlStr= bigDecimal.toPlainString();
//            }
//        }else{
//            if(!str.contains(".")){
//                fianlStr=Math.abs(Long.parseLong(str))+"";
//            }else{
//                fianlStr= Math.abs(Double.parseDouble(str))+"";
//            }
//        }
//
//
//        if(fianlStr.contains(".")){
//            String []aString=fianlStr.split("\\.");
//            if (aString[1].length()>=2){
//                return decimalFormat.format(Long.parseLong(aString[0]))+"."+aString[1].substring(0,2);
//            }else{
//                return decimalFormat.format(Long.parseLong(aString[0]))+"."+aString[1]+"0";
//            }
//
//        }else{
//            return decimalFormat.format(Long.parseLong(fianlStr))+".00";
//        }
//
//    }

    /**
     * 整数部分每三位一个逗号隔开
     * @param str 没有逗号的字符串
     * @return 每三位一个逗号隔开
     */
    public static String commaSsymbolFormatNoFloat(String str) {

        if (str.contains(".")) {
            String[] aString = str.split("\\.");

            return decimalFormat.format(Long.parseLong(aString[0]));
        }
        else{
            return decimalFormat.format(Long.parseLong(str));
        }
    }
}
