package com.sly.app.comm;

public class NetConstant {

    //    public static final String BASE_URL = "http://39.104.17.63:8081/KernelSvr.svc/Api";

    public static final String BASE_URL = "http://118.89.220.130:8081/KernelSvr.svc/Api";
    public static final String IMG_URL = "http://118.89.220.130:8081";
    public static final String IMG_URL2 = "http://sys.slyhash.com";

    public static final String GET_YUNW_MACHINE_NUM_RATE = "SlyTwo.001";

    public static final String GET_MINE_AREA_INFO = "SlyTwo.002";

    public static final String GET_NEW_REPAIR_NUM = "SlyTwo.003";

    public static final String GET_YUNW_ALL_NUM = "SlyTwo.004";

    public static final String GET_MACHINE_LIST = "SlyTwo.005";

    public static final String GET_MACHINE_STATUS = "SlyTwo.006";

    public static final String GET_YUNW_MANAGE_AREA = "SlyTwo.007";

    public static final String GET_MACHINE_TYPE = "SlyTwo.008";

    public static final String GET_MACHINE_DEATAILS_HEADER = "SlyTwo.009";

    public static final String GET_MACHINE_DEATAILS_MINE_POOL = "SlyTwo.010";

    public static final String GET_YUNW_REPAIR_STOP_MACHINE = "SlyTwo.011";

    public static final String GET_YUNW_REPAIR_START_MACHINE = "SlyTwo.012";

    public static final String GET_YUNW_REPAIR_CANCEL_MACHINE = "SlyTwo.013";

    public static final String GET_MACHINE_DETAILS_ALL_SUANLI = "SlyTwo.014";

    public static final String GET_MACHINE_DETAILS_24_SUANLI = "SlyTwo.015";

    public static final String GET_MACHINE_DETAILS_30_SUANLI = "SlyTwo.016";

    public static final String GET_GOLINE_PLAN_LIST = "SlyTwo.017";

    public static final String GET_PLAN_MACHINE_LIST = "SlyTwo.018";

    public static final String SET_SCAN_MACHINE_POOL = "SlyTwo.019";

    public static final String BIND_VIP_CODE = "SlyTwo.020";

    public static final String COMMIT_PLAN_MACHINE_LIST = "SlyTwo.021";

    public static final String DELETE_GOLINE_PLAN = "SlyTwo.022";

    public static final String GET_PLAN_ALL_MACHINE = "SlyTwo.023";

    public static final String GET_YUNW_REPAIR_TREATED = "SlyTwo.024";

    public static final String GET_YUNW_REPAIR_TREATING = "SlyTwo.025";

    public static final String GET_YUNW_REPAIR_UNTREATED = "SlyTwo.026";

    public static final String GET_YUNW_REPAIR_DETAILS = "SlyTwo.028";

    public static final String GET_YUNW_REPAIR_SPARES = "SlyTwo.029";

    public static final String GET_YUNW_REPAIR_SUBMIT = "SlyTwo.030";

    public static final String GET_YUNW_REPAIR_DEALING_BTN = "SlyTwo.031";

    public static final String GET_YUNW_REPAIR_DEALED_BTN = "SlyTwo.032";

    public static final String GET_YUNW_BEGIN_WORK_STATUS = "SlyTwo.033";

    public static final String GET_YUNW_END_WORK_STATUS = "SlyTwo.034";

    public static final String GET_YUNW_SET_WORK_STATUS = "SlyTwo.035";

    public static final String GET_YUNW_SET_NO_WORK_STATUS = "SlyTwo.036";

    public static final String GET_YUNW_WORK_TIME = "SlyTwo.037";

    public static final String GET_NEW_NOTICE_LIST = "SlyTwo.038";

    public static final String GET_NEW_NOTICE_DETAILS = "SlyTwo.039";


    public static final String SET_DETAILS_AND_MANAGE_POOL = "SlyTwo.041";


    public static final String GET_PLAN_MACHINE_AREA = "SlyTwo.043";

    public static final String GET_NAME_BY_VIP_CODE = "SlyTwo.044";

    public static final String GET_MACHINE_GOLINE_AREA = "SlyTwo.045";

    public static final String GOLINE_COMMIT_PLAN = "SlyTwo.046";

    public static final String CAHNGE_NEWS_STATUS = "SlyTwo.047";

    public static final String GET_YUNW_NEWS_COUNT = "SlyTwo.048";

    public static final String GET_YUNW_MACHINE_LIST_STATUS = "SlyTwo.049";


    /*矿场主2.0*/
    public static final String GET_MASTER_MINE_LIST = "SlyTwo.101";

    public static final String GET_MASTER_ALL_DATA = "SlyTwo.102";

    public static final String GET_MASTER_NUM_RATE = "SlyTwo.103";

    public static final String GET_MASTER_MOBILE = "SlyTwo.104";


    public static final String GET_MASTER_MACHINE_LIST = "SlyTwo.108";

    public static final String GET_MASTER_SPARE_LIST = "SlyTwo.109";

    public static final String GET_MASTER_PERSON_MANAGE_LIST = "SlyTwo.110";

    public static final String SET_MASTER_PERSON_MANAGER = "SlyTwo.111";

    public static final String DELETE_MASTER_PERSON = "SlyTwo.112";

    public static final String GET_MANAGER_YUNWE_PERSON_LIST = "SlyTwo.113";

    public static final String ADD_YUNW_TO_MANAGER = "SlyTwo.114";

    public static final String DELETE_EVERY_PERSON = "SlyTwo.115";


    public static final String GET_CHILD_ACCOUNTEXEC = "SlyTwo.119";

    public static final String ADD_ACCOUNT_EXEC = "SlyTwo.120";

    public static final String SET_PERMISSION_FOR_ACCOUNT = "SlyTwo.121";

    public static final String GET_AUTH_ACCOUNT_MINE = "SlyTwo.122";

    public static final String GET_AUTH_ACCOUNT_PERMISSION = "SlyTwo.123";

    public static final String GET_MASTER_MONTH_FREE = "SlyTwo.124";

    public static final String GET_MASTER_MONTH_POWER = "SlyTwo.125";

    public static final String GET_MASTER_MACHINE_TYPE = "SlyTwo.126";

    public static final String GET_MASTER_MACHINE_STATUS = "SlyTwo.126";



    public static final String GET_COMMON_NOTICE = "SlyTwo.201";




    public static final class EventTags {

        public final static int BEGIN_EVENT = 20;

        public static final int GET_MINE_AREA_INFO = BEGIN_EVENT + 30; //获取区域信息板列表

        public static final int GET_NEW_REPAIR_NUM = BEGIN_EVENT + 31; //获取未处理维修单数量

        public static final int GET_YUNW_ALL_NUM = BEGIN_EVENT + 32; //获取未处理维修单数量

        public static final int GET_YUNW_REPAIR_UNTREATED = BEGIN_EVENT + 33; //获取未处理维修单

        public static final int GET_YUNW_REPAIR_TREATING = BEGIN_EVENT + 34; //获取已处理维修单

        public static final int GET_YUNW_REPAIR_TREATED = BEGIN_EVENT + 35; //获取处理中维修单

        public static final int GET_YUNW_REPAIR_DETAILS = BEGIN_EVENT + 36; //获取维修单详情

        public static final int GET_YUNW_REPAIR_SPARES = BEGIN_EVENT + 37; //获取维修备件

        public static final int GET_YUNW_REPAIR_SUBMIT = BEGIN_EVENT + 38; // 提交维修报单

        public static final int GET_YUNW_REPAIR_DEALING_BTN = BEGIN_EVENT + 39; //处理中待付款（已解决）

        public static final int GET_YUNW_REPAIR_DEALED_BTN = BEGIN_EVENT + 40; //维修结束（已解决）

        public static final int GET_YUNW_REPAIR_STOP_MACHINE = BEGIN_EVENT + 41; //设备停用

        public static final int GET_YUNW_REPAIR_START_MACHINE = BEGIN_EVENT + 42; //设备启用

        public static final int GET_YUNW_MACHINE_NUM_RATE = BEGIN_EVENT + 43; //获取状态设备数量及比例

        public static final int GET_MACHINE_LIST = BEGIN_EVENT + 44; //获取设备列表

        public static final int GET_MACHINE_TYPE = BEGIN_EVENT + 45; //获取设备类型

        public static final int GET_MACHINE_DEATAILS_HEADER = BEGIN_EVENT + 46; //获取设备详情头部信息

        public static final int GET_MACHINE_DEATAILS_MINE_POOL = BEGIN_EVENT + 47; //获取设备矿池

        public static final int GET_MACHINE_DETAILS_ALL_SUANLI = BEGIN_EVENT + 48; //获取设备所有总算力

        public static final int GET_MACHINE_DETAILS_24_SUANLI = BEGIN_EVENT + 49; //获取设备24小时算力

        public static final int GET_MACHINE_DETAILS_30_SUANLI = BEGIN_EVENT + 50; //获取设备30天算力

        public static final int SET_SCAN_MACHINE_POOL = BEGIN_EVENT + 51; //设置扫描出来的矿机的矿池

        public static final int SET_DETAILS_AND_MANAGE_POOL = BEGIN_EVENT + 52; //设置单台或多台矿机的矿池

        public static final int GET_YUNW_REPAIR_CANCEL_MACHINE = BEGIN_EVENT + 53; //设备注销

        public static final int GET_YUNW_MANAGE_AREA = BEGIN_EVENT + 54; //获取运维所负责的区域列表

        public static final int GET_GOLINE_PLAN_LIST = BEGIN_EVENT + 55; //获取上机计划列表

        public static final int GET_MACHINE_GOLINE_AREA = BEGIN_EVENT + 56; //获取矿机上线的区域列表

        public static final int GOLINE_COMMIT_PLAN = BEGIN_EVENT + 57; // 提交上机计划

        public static final int DELETE_GOLINE_PLAN = BEGIN_EVENT + 58; // 删除上机计划

        public static final int GET_PLAN_MACHINE_LIST = BEGIN_EVENT + 59; // 获取计划扫描出来的设备列表

        public static final int GET_PLAN_MACHINE_AREA = BEGIN_EVENT + 60; // 获取上机计划参数的区域列表

        public static final int COMMIT_PLAN_MACHINE_LIST = BEGIN_EVENT + 61; // 将计划扫描出来的矿机注册进系统

        public static final int GET_NAME_BY_VIP_CODE = BEGIN_EVENT + 62; // 根据会员号获取姓名

        public static final int BIND_VIP_CODE = BEGIN_EVENT + 63; // 为扫描出来的设备绑定会员号

        public static final int GET_PLAN_ALL_MACHINE = BEGIN_EVENT + 64; // 从扫描结果中根据区域和矿工号筛选出设备列表

        public static final int GET_YUNW_BEGIN_WORK_STATUS = BEGIN_EVENT + 65; // 判断上班按钮是否出现

        public static final int GET_YUNW_END_WORK_STATUS = BEGIN_EVENT + 66; // 判断下班按钮是否出现

        public static final int GET_YUNW_SET_WORK_STATUS = BEGIN_EVENT + 67; // 上班打卡

        public static final int GET_YUNW_SET_NO_WORK_STATUS = BEGIN_EVENT + 68; // 下班打卡

        public static final int GET_YUNW_WORK_TIME = BEGIN_EVENT + 69; // 上班、下班时间

        public static final int GET_NEW_NOTICE_LIST = BEGIN_EVENT + 70; // 获取人员未读消息列表

        public static final int GET_NEW_NOTICE_DETAILS = BEGIN_EVENT + 71; // 消息详情

        public static final int GET_COMMON_NOTICE = BEGIN_EVENT + 72; // 获取公告

        public static final int CAHNGE_NEWS_STATUS = BEGIN_EVENT + 73; // 修改人员消息为已读接口

        public static final int GET_YUNW_NEWS_COUNT = BEGIN_EVENT + 74; // 查看运维未读消息数量

        public static final int GET_YUNW_MACHINE_LIST_STATUS = BEGIN_EVENT + 75; // 获取运维负责的设备状态


        public static final int GET_MASTER_MINE_LIST = BEGIN_EVENT + 100; // 获取矿场

        public static final int GET_MASTER_ALL_DATA = BEGIN_EVENT + 101; // 数据汇总

        public static final int GET_MASTER_NUM_RATE = BEGIN_EVENT + 102; // 获取状态设备数量及比例

        public static final int GET_MASTER_MOBILE = BEGIN_EVENT + 103; // 获取手机号

        public static final int GET_MASTER_MACHINE_LIST = BEGIN_EVENT + 104; // 获取矿场主设备列表

        public static final int GET_MASTER_SPARE_LIST = BEGIN_EVENT + 105; // 获取配件列表

        public static final int GET_MASTER_PERSON_MANAGE_LIST = BEGIN_EVENT + 106; // 获取人员管理列表

        public static final int SET_MASTER_PERSON_MANAGER = BEGIN_EVENT + 107; // 设置班组长

        public static final int DELETE_MASTER_PERSON = BEGIN_EVENT + 108; // 删除人员

        public static final int GET_MANAGER_YUNWE_PERSON_LIST = BEGIN_EVENT + 109; // 人员管理-添加人员列表

        public static final int ADD_YUNW_TO_MANAGER = BEGIN_EVENT + 110; // 给班组长分配运维人员

        public static final int DELETE_EVERY_PERSON = BEGIN_EVENT + 111; // 人员架构-删除

        public static final int GET_MASTER_MONTH_FREE = BEGIN_EVENT + 112; // 矿场主月费用图表

        public static final int GET_MASTER_MONTH_POWER = BEGIN_EVENT + 113; // 矿场主月耗电量图表

        public static final int GET_MACHINE_STATUS = BEGIN_EVENT + 114; // 矿机状态

        public static final int ADD_ACCOUNT_EXEC = BEGIN_EVENT + 115; // 添加授权账号

        public static final int GET_CHILD_ACCOUNTEXEC = BEGIN_EVENT + 116; // 显示授权账号

        public static final int GET_AUTH_ACCOUNT_PERMISSION = BEGIN_EVENT + 117; // 获取授权账号的权限

        public static final int SET_PERMISSION_FOR_ACCOUNT = BEGIN_EVENT + 118; // 给授权账号分配权限(0为授权，1为未授权)

        public static final int GET_MASTER_MACHINE_TYPE = BEGIN_EVENT + 119; // 获取型号

        public static final int GET_MASTER_MACHINE_STATUS = BEGIN_EVENT + 120; // 获取矿场的设备状态

        public static final int GET_AUTH_ACCOUNT_MINE = BEGIN_EVENT + 121; // 获取授权账号的矿场


    }


    public static final class Request {
        public static final int PAGE_NUMBER = 12;
        public static final int MACHINE_LIST_PAGE_NUMBER = 20;
    }

}
