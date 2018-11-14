package com.sly.app.http;

/**
 * 作者 by K
 * 时间：on 2017/9/4 09:47
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class NetWorkConstant {
    public static final String DQC_BASE_URL = "http://117.48.196.62:9000";
    public static final String HOME_BANNER_TOP = DQC_BASE_URL + "/MemberSvr.svc/reqBannerPic";
    // TODO: 2017/9/19 ---------------会员------------------
    /**
     * 注册
     */
    public static String register = DQC_BASE_URL + "/MemberSvr.svc/RegisterS";
    /**
     * 登录
     */
    public static String USER_LOGIN = DQC_BASE_URL + "/MemberSvr.svc/LoginS";
    /**
     * 获取个人信息
     */
    public static String USER_GET_INFO = DQC_BASE_URL + "/MemberSvr.svc/GetMemberMessage";
    /**
     * 注册发送验证码接口
     */
    public static String regSendCode = DQC_BASE_URL + "/MemberSvr.svc/SendMobileCodeForRegister";
    /**
     * 会员发送验证码接口
     */
    public static String USER_GET_CODE = DQC_BASE_URL + "/MemberSvr.svc/SendMoblieCodeForMember";
    /**
     * 获取手机号注册的账号
     */
    public static String USER_GET_MEMBER = DQC_BASE_URL + "/MemberSvr.svc/GetMCOfMobile";
    /**
     * 钱包支付宝充值
     */
    public static String WALLET_PAY_ALI = DQC_BASE_URL + "/PurseSvr.svc/AliCharge";
    /**
     * 修改会员头像
     */
    public static String USER_GET_IMG = DQC_BASE_URL + "/MemberSvr.svc/editUserInfoS";
    /**
     * 绑定地球村会员
     */
    public static String USER_RELATION = DQC_BASE_URL + "/MemberSvr.svc/BindAccount";
    /**
     * 是否绑定地球村会员
     */
    public static String USER_IS_RELATION = DQC_BASE_URL + "/MemberSvr.svc/DQCBindAccount";
    /**
     * 退出登陆
     */
    public static String USER_LOGIN_OUT = DQC_BASE_URL + "/MemberSvr.svc/LogoutS";
    /**
     * 分销
     */
    public static String USER_JOIN_FX = DQC_BASE_URL + "/MemberSvr.svc/JoinFxOrder";
    /**
     * 我的团队
     */
    public static String USER_GROUP_TEAM = DQC_BASE_URL + "/MemberSvr.svc/NodeRelation";
    /**
     * 佣金
     */
    public static String USER_GROUP_NEXUS_1 = DQC_BASE_URL + "/MemberSvr.svc/GetYeJi";
    /**
     * 业绩
     */
    public static String USER_GROUP_NEXUS_2 = DQC_BASE_URL + "/MemberSvr.svc/GetYongJin";
    /**
     * 邀请的好友
     */
    public static String USER_INVITE = DQC_BASE_URL + "/MemberSvr.svc/GeteRecommendMeesage";
    /**
     * 获取识别码
     */
    public static String USER_CODE = DQC_BASE_URL + "/MemberSvr.svc/GetMemberRecomand";
    /**
     * 获取识别码
     */
    public static String USER_GET_MEMBERINFO = DQC_BASE_URL + "/MemberSvr.svc/GetOrderMemberMessage";
    /**
     * 修改登录密码
     */
    public static String GetChaneLoginWD = DQC_BASE_URL + "/MemberSvr.svc/ChangePassword";
    /**
     * 修改支付密码
     */
    public static String GetChanePayWD = DQC_BASE_URL + "/PurseSvr.svc/IsSetorUdapterPayPassword";
    /**
     * 流水
     */
    public static String getBusiness = DQC_BASE_URL + "/PurseSvr.svc/GetPurseFlow";
    /**
     * 修改昵称
     */
    public static String ChangeNikeName = DQC_BASE_URL + "/MemberSvr.svc/editUserInfoNickNme";
    /**
     * 修改性别
     */
    public static String UdapaterSex = DQC_BASE_URL + "/MemberSvr.svc/UdapaterSex";
    /**
     * 修改身份证号
     */
    public static String UdapaterParperNo = DQC_BASE_URL + "/MemberSvr.svc/UdapaterParperNo";
    /**
     * 修改生日
     */
    public static String UdapaterBrithDay = DQC_BASE_URL + "/MemberSvr.svc/UdapaterBrithDay";

    // TODO: 2017/9/20 钱包-------------------------
    /**
     * 获取钱包数据
     */
    public static String PURSER_DATE = DQC_BASE_URL + "/PurseSvr.svc/GetPurseBalance";
    /**
     * 转账
     */
    public static String USER_WALLET_ZZ = DQC_BASE_URL + "/PurseSvr.svc/ToMoney";
    /**
     * 是否有支付密码/设置过支付密码
     */
    public static String PURSE_IS_NULL = DQC_BASE_URL + "/PurseSvr.svc/IsSetPayPassword";
    /**
     * 获取是否设置支付密码接口
     */
    public static String IsSetPayPassword = DQC_BASE_URL + "/PurseSvr.svc/IsSetPayPassword";
    /**
     * 获取绑定银行卡接口
     */
    public static String GetBankCard = DQC_BASE_URL + "/PurseSvr.svc/GetBankCard";
    /**
     * 绑定银行账户接口
     */
    public static String BindPurseBankCard = DQC_BASE_URL + "/PurseSvr.svc/BindPurseBankCard ";
    /**
     * 取消提现
     */
    public static String CancelCash = DQC_BASE_URL + "/PurseSvr.svc/CancelCashs";
    /**
     * 是否有申请提现
     */
    public static String IsApply = DQC_BASE_URL + "/PurseSvr.svc/IsApply";
    /**
     * 更新银行卡信息
     */
    public static String UpdateAccount = DQC_BASE_URL + "/PurseSvr.svc/UpdateAccount";
    /**
     * 提现
     */
    public static String Withdraw = DQC_BASE_URL + "/PurseSvr.svc/Withdraw";

    /**
     * 钱包微信充值
     */
    public static String WALLET_PAY_WX = DQC_BASE_URL + "/PurseSvr.svc/WxInsertOrder";
    /**
     * 库存额度/产品券微信充值
     */
    public static String WALLET_TYPE_PAY_WX = DQC_BASE_URL + "/PurseSvr.svc/CreateChargeOrder";
    // TODO: 2017/9/19 地址-------------------------------
    /**
     * 获取全部地址
     */
    public static String ADDRESS_GET_LIST = DQC_BASE_URL + "/SysSvr.svc/GetAllAddress";

    /**
     * 设置默认地址
     */
    public static String ADDRESS_DEFAULT = DQC_BASE_URL + "/SysSvr.svc/UpdateDefaultAddress";

    /**
     * 删除地址
     */
    public static String ADDRESS_DEL = DQC_BASE_URL + "/SysSvr.svc/DelAddress";
    /**
     * 修改地址
     */
    public static String ADDRESS_UPDATA = DQC_BASE_URL + "/SysSvr.svc/UpdateDefaultAddress";
    /**
     * 修改地址
     */
    public static String ADDRESS_ADD = DQC_BASE_URL + "/SysSvr.svc/InsertAddress";

    // TODO: 2017/9/19  商品-------------------------------
    /**
     * 商品列表
     */
    public static String COM_GET_LIST = DQC_BASE_URL + "/ComSvr.svc/CommodityList";
    /**
     * 热卖商品列表
     */
    public static String COM_GET_HOT_LIST = DQC_BASE_URL + "/ComSvr.svc/GetvCommodityListHostData";
    /**
     * 分类商品列表
     */
    public static String COM_GET_TYPE_LIST = DQC_BASE_URL + "/ComSvr.svc/GetCommodity";
    /**
     * 礼包商品列表
     */
    public static String COM_GET_LB_LIST = DQC_BASE_URL + "/ComSvr.svc/CommodityLists";
    /**
     * 商品属性
     */
    public static String COM_ATTR = DQC_BASE_URL + "/ComSvr.svc/getProductPropertyDetail";
    /**
     * 商品详情
     */
    public static String COM_DETAIL = DQC_BASE_URL + "/ComSvr.svc/CommodityDetail";

    /**
     * 商品详情页
     **/
    public static final String COM_DETAIL_VIEW = DQC_BASE_URL+"/AppCommon/AppDetail.aspx?comid=";
    /**
     * 收藏
     */
    public static String COM_COLLECTION = DQC_BASE_URL + "/ComSvr.svc/addToFavouriteList";
    /**
     * 取消收藏
     */
    public static String COM_DEL_COLLECTION = DQC_BASE_URL + "/ComSvr.svc/removeFromFavouriteList";
    /**
     * 获取收藏列表
     */
    public static String FAVOURITE_GET_ALL = DQC_BASE_URL + "/ComSvr.svc/getFavouriteList";

    /**
     * 获取商品运费
     */
    public static String COM_GET_POSTAGE = DQC_BASE_URL + "/ComSvr.svc/GetOrderPostage";
    // TODO: 2017/9/19 购物车--------------------------
    /**
     * 添加购物车列表
     */
    public static String CART_COM_LIST = DQC_BASE_URL + "/ComSvr.svc/CartList";
    /**
     * 添加购物车
     */
    public static String CART_ADD = DQC_BASE_URL + "/ComSvr.svc/addToNewShopCart";
    /**
     * 全部取消选中
     */
    public static String CART_All_SELECT_S = DQC_BASE_URL + "/ComSvr.svc/AllTheSelected";
    /**
     * 选中
     */
    public static String CART_SELECT = DQC_BASE_URL + "/ComSvr.svc/selectUnselCartProduct";
    /**
     * 删除
     */
    public static String CART_DElET_ALL = DQC_BASE_URL + "/ComSvr.svc/delProductFromCart";
    // TODO: 2017/12/19  订单
    /**
     * 创建订单
     */
    public static String WS_ORDER_ADD = DQC_BASE_URL + "/MemberSvr.svc/CreateOrder";
    /**
     * 订单支付
     */
    public static String ORDER_PAY = DQC_BASE_URL + "/PurseSvr.svc/PayOrder";
    /**
     * 订单微信支付
     */
    public static String ORDER_PAY_WX = DQC_BASE_URL + "/PurseSvr.svc/WxPayInsertOrder";
    /**
     * 支付宝支付
     */
    public static String ORDER_PAY_ALI = DQC_BASE_URL + "/PurseSvr.svc/AliPay";
    /**
     * 创建加盟订单
     */
    public static String USER_JOIN_ORDER = DQC_BASE_URL + "/MemberSvr.svc/CreateJoinOrder";
    /**
     * 获取订单详情
     */
    public static String ORDER_GET_DETAILS = DQC_BASE_URL + "/MemberSvr.svc/GetOneOrder";
    /**
     * 获取订单
     */
    public static String ORDER_GET_ALL = DQC_BASE_URL + "/MemberSvr.svc/GetOrder";
    /**
     * 取消订单
     */
    public static String ORDER_CANCEL = DQC_BASE_URL + "/MemberSvr.svc/CancelOrder";
    /**
     * 确认订单
     */
    public static String ORDER_AFFIRM_R = DQC_BASE_URL + "/MemberSvr.svc/SureOrder";
    /**
     * 删除订单
     */
    public static String ORDER_DELETE = DQC_BASE_URL + "/MemberSvr.svc/DeleteOrderStatus";
    // TODO: 2017/12/19    消息 通知
    /**
     * 获取平台信息通知列表
     */
    public static String UserAllMessage = DQC_BASE_URL + "/MemberSvr.svc/UserAllMessage";
    /**
     * 删除消息
     */
    public static String UpdateUserAllMessage = DQC_BASE_URL + "/MemberSvr.svc/UpdateUserAllMessage";
    /**
     * 清除消息提提醒
     */
    public static String CleanMessageWarn = DQC_BASE_URL + "/MemberSvr.svc/UpdateMessage";
    /**
     * 是否有消息提醒
     */
    public static String GetMessage = DQC_BASE_URL + "/MemberSvr.svc/GetMessage";
    // TODO: 2017/9/27 零元购
    /**
     * 获取零元购买商品
     */
    public static String ZERO_GET_COM = DQC_BASE_URL + "/ZeroBuySvr.svc/GetvCommodityListData";
    /**
     * 判断零元购是否本季度有购物
     */
    public static String ZERO_USER_BUY = DQC_BASE_URL + "/ZeroBuySvr.svc/IsFreeShopBug";
    /**
     * 零元购生成订单
     */
    public static String ZERO_CREAT_ORDER = DQC_BASE_URL + "/ZeroBuySvr.svc/CreatePointOrder2";
    // TODO: 20 17/10/11  积分商城
    /**
     * 积分兑换
     */
    public static String POINT_CREAT_ORDER = DQC_BASE_URL + "/MemberSvr.svc/ExchangeIntegralCom";

    /**
     * 列表
     */
    // TODO: 2017/12/25   资讯 公告
    public static String MSG_GET_ALL = DQC_BASE_URL + "/SysSvr.svc/GetArticleInfo";
    /**
     * 详情
     */
    public static String MSG_INFO = DQC_BASE_URL+"/AppCommon/Articel.aspx?code=";

}