package com.sly.app.http;

/**
 * <p/>
 * 类用途：
 * 最后修改：
 */

public class NetWorkCons {
//    public static final String BASE_URL = "http://47.104.166.182:8081/KernelSvr.svc/Api";
//    public static final String BASE_URL = "http://39.104.17.63:8081/KernelSvr.svc/Api";

    public static final String BASE_URL = "http://118.89.220.130:8081/KernelSvr.svc/Api";
    public static final String IMG_URL = "http://118.89.220.130:8081";
//    public static final String IMG_URL2 = "http://118.89.220.130::8081";
    public static final String IMG_URL2 = "http://sys.slyhash.com";


    public static final String GET_MINER_REPAIR_TAKET = "Miner.009";//获取矿工维修单

    public static final String GET_RECOURD_LIST = "Member.006";//获取用户钱包流水

    public static final String GET_RECHARGE_LIST = "Miner.032";//获取用户钱包流水

    public static final String GET_REPAIR_TAKET_DETAIL = "Repair.001";//获取维修单详情

    public static final String GET_REPAIR_FEE_DETAIL = "Repair.002";//获取备件名称

    public static final String PAY_REPAIR_FEE = "Repair.003";//支付矿机维修费用

    public static final String NO_PAY_REPAIR_FEE = "Repair.004";//维修单维修拒单

    public static final String IS_REMINDER_STATUS = "Miner.027";//判断是否可以催单接口

    public static final String REMINDER_REPAIR_TAKET = "Miner.028";//催单接口

    public static final String GET_MY_MACHINE = "Miner.029";//获取矿工的设备列表的接口。

    public static final String GET_MY_MACHINE_DETAIL = "Miner.030";//获取单个设备详情的接口

    public static final String CALER_FIRST = "Calculater.003";//计算器首次计算

    public static final String GET_CALCULATER_DEFULT_NUM = "Calculater.002";//获取挖矿收益计算默认参数接口

    public static final String GET_CALCULATER = "Calculater.001";//挖矿收益计算接口

    public static final String GET_MINER_NOTICE_LIST = "Miner.033";//获取矿工消息列表

    public static final String GET_MINEMASTER_NOTICE_LIST = "MineMaster.019";//矿场主获取人员消息列表

    public static final String GET_NOTICE_DETAIL_MINER = "Miner.034";//获取矿工消息详情

    public static final String GET_NOTICE_DETAIL_MINERMASTER = "MineMaster.020";//矿场主获取人员消息详细

    public static final String SET_MASTER_NOTICE_STATUS = "MineMaster.021";//改变矿主阅读人员消息的状态

    public static final String SET_MINER_NOTICE_STATUS = "Miner.035";//改变矿工消息的阅读状态

    public static final String MSG_GET_ALL = "Member.015";//播报列表接口

    public static final String GET_HOME_MSG_DETAIL = "Member.016";//根据ID获取播报信息接口

    public static final String GET_MACHINE_ONLINE_LIST = "SmartOnline.003";//根据ID获取矿机上线列表

    public static final String GET_PLAN_AREA_LIST = "SmartOnline.004";//获取上线计划区域列表

    public static final String GET_SIMGLE_PLAN_DETAILS_LIST = "SmartOnline.005";//获取单个上线计划分组

    public static final String GET_ONLINE_MACHINE_LIST = "SmartOnline.006";//获取计划扫描出来的设备列表

    public static final String GET_SCAN_MACHINE_LIST = "SmartOnline.007";//获取属于某个矿工号的扫描出来的设备列表

    public static final String BIND_USER_VIP_CODE = "SmartOnline.008";//根据姓名获取名称

    public static final String GET_CHECK_MACHINE_LIST = "SmartOnline.009";//获取上机计划里某个区域的设备

    public static final String GET_USER_NAME = "SmartOnline.010";//根据姓名获取名称

    public static final String COMMIT_SCAN_LIST = "SmartOnline.011";//扫描详情提交

    public static final String GET_MANAGE_MACHINE_DETAILS_HEAR = "Sly.007";//运维矿机管理矿机详情（头部数据）

    public static final String GET_MANAGE_MACHINE_LIST = "Sly.008";//获取运维人员管理的设备列表

    public static final String GET_MANAGE_AREA = "Sly.010";//获取运维人员管理的区域

    public static final String GET_MANAGE_AREA_TYPE = "Sly.011";//获取运维人员管理的区域型号

    public static final String GET_MANAGE_MACHINE_STATUS = "Sly.012";//获取矿机状态

    public static final String GET_MANAGE_MACHINE_DOC = "Sly.013";//获取维修单据列表

    public static final String GET_REPAIR_RESULT_STATUS = "Sly.014";//获取维修单据列表

    public static final String GET_REPAIR_RESULT_DETAILS = "Sly.015";//获取获取维修单详情

    public static final String GET_REPAIR_GOODS = "Sly.016";//获取维修单据配件

    public static final String GET_REPAIR_GOODS_DEAL = "Sly.017";//维修单据配件处理

    public static final String GET_MANAGE_MACHINE_DETAILS_FOOT = "Sly.018";//获取矿机状态

    public static final String GET_MANAGE_MACHINE_DETAILS_PIC = "Sly.025";//获取矿机状态波动图

    public static final String GET_REPAIR_DEALED = "Sly.026";//已处理

    public static final String GET_MONITOR_RATE = "Sly.023";//根据人员角色获取矿场矿机概括

    public static final String GET_MONITOR_AREA_INFO = "Sly.024";//获取区域信息板列表接口

    public static final String GET_JPUSH_REGISTRATION_ID = "Sly.027";//检查更新JPUSH注册ID接口

    public static final String GET_MACHINE_LIST = "Sly.030";//获取矿主的设备列表的接口。

    public static final String GET_OPERATION_NOTICE_LIST = "Sly.037";//获取矿主的设备列表的接口。

    public static final String GET_BEGIN_WORK_STATUS = "Sly.039";//判断上班状态

    public static final String GET_END_WORK_STATUS = "Sly.040";//判断下班状态

    public static final String SET_WORKING = "Sly.041";//上班打卡

    public static final String SET_NO_WORK = "Sly.042";//下班打卡

    public static final String GET_WORK_TIME = "Sly.043";//获取上下班时间





    //    public static final String BASE_URL = "http://localhost:2856/KernelSvr.svc/Api";
    public static final class EventTags {

        public final static int WXLOGIN = -1;

        public final static int WXUSERINFO = -2;


        public final static int BEGIN_EVENT = 10;

        public final static int HOMEADV = BEGIN_EVENT + 20;  //广告接口

        public final static int EDITUSER = BEGIN_EVENT + 21;  //更改个人资料

        public static final int BIND_BANK_CARD = BEGIN_EVENT + 22;//绑定银行卡

        public static final int GET_RECOURD_LIST = BEGIN_EVENT + 23;//获取用户钱包流水

        public static final int GET_REPAIR_FEE_DETAIL = BEGIN_EVENT + 24;//获取备件名称

        public static final int PAY_REPAIR_FEE = BEGIN_EVENT + 25;//支付矿机维修费用

        public static final int CALER_FIRST = BEGIN_EVENT + 26;//计算器首次计算

        public static final int GET_CALCULATER_DEFULT_NUM = BEGIN_EVENT + 27;//获取挖矿收益计算默认参数接口

        public static final int GET_REPAIR_TAKET_DETAIL = BEGIN_EVENT + 28;//获取维修单详情

        public static final int IS_REMINDER_STATUS = BEGIN_EVENT + 29;

        public static final int REMINDER_REPAIR_TAKET = BEGIN_EVENT + 30;//催单接口

        public static final int GET_MY_MACHINE_DETAIL = BEGIN_EVENT + 31;//获取单个设备详情的接口

        public static final int NO_PAY_REPAIR_FEE = BEGIN_EVENT + 32;//维修单维修拒单

        public static final int GET_CALCULATER = BEGIN_EVENT + 33;//挖矿收益计算接口

        public static final int GET_MINER_NOTICE_LIST = BEGIN_EVENT + 34;//获取矿工消息列表

        public static final int GET_MINEMASTER_NOTICE_LIST = BEGIN_EVENT + 35;//矿场主获取人员消息列表

        public static final int GET_NOTICE_DETAIL_MINER = BEGIN_EVENT + 36;//获取矿工消息详情

        public static final int GET_NOTICE_DETAIL_MINERMASTER = BEGIN_EVENT + 37;//矿场主获取人员消息详细

        public static final int SET_MASTER_NOTICE_STATUS = BEGIN_EVENT + 38;//改变矿主阅读人员消息的状态

        public static final int SET_MINER_NOTICE_STATUS = BEGIN_EVENT + 39;//改变矿工消息的阅读状态

        public static final int GET_HOME_MSG_DETAIL = BEGIN_EVENT + 40;//根据ID获取播报信息接口

        public static final int GET_MACHINE_ONLINE_LIST = BEGIN_EVENT + 41;//获取矿机上线列表

        public static final int GET_PLAN_AREA_LIST = BEGIN_EVENT + 42;//获取上线计划区域列表

        public static final int GET_SIMGLE_PLAN_DETAILS_LIST = BEGIN_EVENT + 43;//获取单个上线计划分组

        public static final int GET_ONLINE_MACHINE_LIST = BEGIN_EVENT + 44;//获取单个上线计划分组

        public static final int GET_SCAN_MACHINE_LIST = BEGIN_EVENT + 45;//获取单个上线计划分组

        public static final int GET_CHECK_MACHINE_LIST = BEGIN_EVENT + 46;//获取单个上线计划分组

        public static final int COMMIT_SCAN_LIST = BEGIN_EVENT + 47;//扫描详情提交

        public static final int GET_USER_NAME = BEGIN_EVENT + 50;//获取用户姓名

        public static final int BIND_USER_VIP_CODE = BEGIN_EVENT + 51;//绑定会员号

        public static final int GET_MANAGE_MACHINE_DETAILS_HEAR = BEGIN_EVENT + 52;//运维矿机管理矿机详情（数据）

        public static final int GET_MANAGE_MACHINE_LIST = BEGIN_EVENT + 53;//获取运维人员管理的设备列表

        public static final int GET_MANAGE_AREA = BEGIN_EVENT + 54;//获取运维人员管理的区域

        public static final int GET_MANAGE_AREA_TYPE = BEGIN_EVENT + 55;//获取运维人员管理的区域型号

        public static final int GET_MANAGE_MACHINE_STATUS = BEGIN_EVENT + 56;//获取矿机状态

        public static final int GET_MANAGE_MACHINE_DETAILS_FOOT = BEGIN_EVENT + 57;//运维矿机管理矿机详情（数据）

        public static final int GET_MANAGE_MACHINE_DETAILS_PIC = BEGIN_EVENT + 58;//运维矿机管理矿机图表

        public static final int GET_MANAGE_MACHINE_DOC = BEGIN_EVENT + 59;//运维矿机管理矿机图表

        public static final int GET_REPAIR_RESULT_STATUS = BEGIN_EVENT + 60;//运维矿机维修结果状态

        public static final int GET_REPAIR_RESULT_DETAILS = BEGIN_EVENT + 61;//运维矿机维修详情

        public static final int GET_REPAIR_GOODS = BEGIN_EVENT + 62;//运维矿机维修配件

        public static final int GET_REPAIR_GOODS_DEAL = BEGIN_EVENT + 63;//运维矿机维修配件处理

        public static final int GET_REPAIR_DEALED = BEGIN_EVENT + 63;//运维矿机维修已处理

        public static final int GET_MONITOR_RATE = BEGIN_EVENT + 64;//根据人员角色获取矿场矿机概括

        public static final int GET_MONITOR_AREA_INFO = BEGIN_EVENT + 65;//获取区域信息板列表接口

        public static final int GET_JPUSH_REGISTRATION_ID = BEGIN_EVENT + 66;//检查更新JPUSH注册ID接口

        public static final int GET_MINER_REPAIR_TAKET = BEGIN_EVENT + 67;//获取矿工维修单

        public static final int GET_MACHINE_LIST = BEGIN_EVENT + 68;//获取矿主的设备列表的接口。

        public static final int GET_BEGIN_WORK_STATUS = BEGIN_EVENT + 69;//判断上班按钮是否出现。

        public static final int GET_END_WORK_STATUS = BEGIN_EVENT + 70;//判断下班按钮是否出现

        public static final int SET_WORKING = BEGIN_EVENT + 71;//上班打卡

        public static final int SET_NO_WORK = BEGIN_EVENT + 72;//下班打卡

        public static final int GET_WORK_TIME = BEGIN_EVENT + 74;//获取上下班时间



        public final static int REDEEMCODE = BEGIN_EVENT + 87; //兑换码

    }

    public static final class Integers {
        public static final int PAGE_LAZY_LOAD_DELAY_TIME_MS = 200;
    }

    public static final class Request {
        public static final int PAGE_NUMBER = 12;
        public static final int MACHINE_LIST_PAGE_NUMBER = 15;
    }

}