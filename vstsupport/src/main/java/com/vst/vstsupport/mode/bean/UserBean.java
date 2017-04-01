package com.vst.vstsupport.mode.bean;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2016/7/26
 * class description:请输入类描述
 */
public class UserBean {
    /**
     * symbol : A6117
     * isvstuser : 0
     * tokoen : 3bad6af0fa4b8b330d162e19938ee981
     * position : 2
     * account : junjie.cai
     */

    private String symbol;//工号
    private int isvstuser;//是否是vst员工 1是 0 不是
    private String token;//token信息，其他接口都要传该参数，每次登陆会话有效期1小时
    private int position;//0销售员 1产品经理 2领导 position的值主要用来判断积压库存功能能否使用，以及当积压库存跟进功能 是跟进还是问询
    private String account;
    private String teamIntroductionUrl;

    private boolean isHideInventory;

    public boolean getIsHideInventory() {
        if (isvstuser==1){
            isHideInventory = true;
        }else {
            isHideInventory = position==0?true:false;
        }
        return isHideInventory;
    }

    public String getTeamIntroductionUrl() {
        return teamIntroductionUrl;
    }

    public void setTeamIntroductionUrl(String teamIntroductionUrl) {
        this.teamIntroductionUrl = teamIntroductionUrl;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getIsvstuser() {
        return isvstuser;
    }

    public void setIsvstuser(int isvstuser) {
        this.isvstuser = isvstuser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
