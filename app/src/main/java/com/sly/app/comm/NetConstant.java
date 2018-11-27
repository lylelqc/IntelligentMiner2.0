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


    public static final String SET_SCAN_MACHINE_POOL = "SlyTwo.019";



    public static final String GET_YUNW_REPAIR_TREATED = "SlyTwo.024";

    public static final String GET_YUNW_REPAIR_TREATING = "SlyTwo.025";

    public static final String GET_YUNW_REPAIR_UNTREATED = "SlyTwo.026";

    public static final String GET_YUNW_REPAIR_DETAILS = "SlyTwo.028";

    public static final String GET_YUNW_REPAIR_SPARES = "SlyTwo.029";

    public static final String GET_YUNW_REPAIR_SUBMIT = "SlyTwo.030";

    public static final String GET_YUNW_REPAIR_DEALING_BTN = "SlyTwo.031";

    public static final String GET_YUNW_REPAIR_DEALED_BTN = "SlyTwo.032";

    public static final String SET_DETAILS_AND_MANAGE_POOL = "SlyTwo.041";



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


    }


    public static final class Request {
        public static final int PAGE_NUMBER = 12;
        public static final int MACHINE_LIST_PAGE_NUMBER = 20;
    }

}
