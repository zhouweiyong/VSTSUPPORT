package com.vst.vstsupport.utils;

import com.vst.vstsupport.mode.bean.test.TuiMessageBean;

import java.util.ArrayList;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/18
 * class description:请输入类描述
 */
public class TestUitils {
    private static TestUitils instance;

    public static TestUitils getInstance(){
        if (instance==null){
            instance = new TestUitils();
        }
        return instance;
    }

    public  ArrayList<TuiMessageBean> getTuiMessage(){
        ArrayList<TuiMessageBean> tuiMessageBeens = new ArrayList<>();
        for (int i = 0;i <10 ;i ++){
            TuiMessageBean tuiMessageBean = new TuiMessageBean();
            tuiMessageBean.setType(i);
            tuiMessageBean.setMessageTitle("跟进提醒"+i);
            tuiMessageBean.setMessageContent("9月10号前你能陆续回款吗？");
            tuiMessageBeens.add(tuiMessageBean);
        }
        return tuiMessageBeens;
    }

}
