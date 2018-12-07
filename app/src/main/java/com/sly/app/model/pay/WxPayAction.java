package com.sly.app.model.pay;

/**
 * 作者 by K
 * 时间：on 2017/9/28 16:14
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class WxPayAction {
    /**支付用途 shopping,recharge**/
    private static String payUseing;
    /**支付商城类型 zg,jm,ev**/
    private static String WwwType;
    /**支付调回订单详情页的回退action**/
    private static String backAction;
    public static String getOrderNo() {
        return OrderNo;
    }

    public static void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    private static String OrderNo;
    public static String getBackAction() {
        return backAction;
    }

    public static void setBackAction(String backAction) {
        WxPayAction.backAction = backAction;
    }

    public WxPayAction() {
    }

    public WxPayAction(String payUseing, String wwwType) {
        WxPayAction.payUseing = payUseing;
        WwwType = wwwType;
    }

    public  static String getPayUseing() {
        return payUseing;
    }

    public void setPayUseing(String payUseing) {
        WxPayAction.payUseing = payUseing;
    }

    public static String getWwwType() {
        return WwwType;
    }

    public void setWwwType(String wwwType) {
        WwwType = wwwType;
    }

    @Override
    public String toString() {
        return "WxPayAction{" +
                "payUseing='" + payUseing + '\'' +
                ", WwwType='" + WwwType + '\'' +
                '}';
    }
}
