package com.pinet.core.constants;

public class CommonConstant {

    public static final String SMS_CODE_LOGIN = "sms_code:login:";

    public static final String SMS_CODE_FORGET_PAY_PASSWORD = "sms_code:forget:pay_password:";

    //最大订单数
    public static final Integer MAX_ORDER_NUM = 100;

    //默认纬度
    public static final String DEFAULT_LAT = "30.182024";//保利店经纬度

    //默认经度
    public static final String DEFAULT_LNG = "120.197379";

    //图片域名前缀
    public static final String IMAGE_DOMAIN = "http://image.ypxlbz.com/";


    /**
     * 否
     */
    public static final Integer NO = 0;

    /**
     * 是
     */
    public static final Integer YES = 1;

    /**
     * 是否删除，0-否，1-是
     */
    public static final int DEL_FLAG_N = 0;

    public static final int DEL_FLAG_Y = 1;

    /**
     * 启用/禁用，0-禁用，1-启用
     */
    public static final int ENABLE = 1;
    public static final int DISABLE = 0;

    /**
     * 成功/失败
     */

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    /**
     * 未支付
     */
    public static final String UNPAY = "UNPAY";

    /**
     * 消息类型1:通知公告2:系统消息
     */
    public static final String MSG_CATEGORY_1 = "1";
    public static final String MSG_CATEGORY_2 = "2";



}
