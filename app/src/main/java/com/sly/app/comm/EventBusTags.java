package com.sly.app.comm;

/**
 * Created by liucanwen on 16/3/3.
 */
public class EventBusTags {

    public static final String test = "test";//刷新个人列表

    public static final String REFRESH="refresh";//刷新

    public static final String LOGIN="login";//登录成功

    public static final String LOGOUT="logout";//登录退出

    public static final String LOGIN_SUCCESS = "login_success"; //登录成功

    public static final String UPDATE_HOSTING_DATA = "update_hosting_data"; //更新托管数据

    public static final String UPDATE_BANK_LIST = "update_bank_list";//更新银行列表

    public static final String UPDATE_HOSTING_MINER_DATA = "update_hosting_miner_data";//更新矿工托管

    public static final String UPDATE_HOSTING_MASTER_DATA = "update_hosting_master_data";//更新矿主托管

    public static final String UPDATE_HOSTING_OPERATION_DATA = "update_hosting_operation_data";//更新运维托管

    /*
    * version 2.0
    * */
    public static final String JUMP_REPAIR_BILL_TREATING = "jump_repair_bill_treating"; //跳转处理中

    public static final String JUMP_REPAIR_BILL_TREATED = "jump_repair_bill_treated"; //跳转已处理

    public static final String CHOOSE_AUTH_MINE_AREA = "choose_auth_mine_area"; //选择授权矿场

}
