package com.vst.vstsupport.mode.bean.test;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/18
 * class description:推送消息实体
 */
public class TuiMessageBean {
    private int type;//消息类型
    private String messageTitle;//消息标题
    private String messageContent;//消息内容

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
