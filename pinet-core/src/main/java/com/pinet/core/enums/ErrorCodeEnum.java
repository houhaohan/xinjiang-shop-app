package com.pinet.core.enums;


/**
 * Created by linlinyeyu on 2017/12/14.
 */
public enum ErrorCodeEnum {
    SUCCESS(0,"请求成功"),

    USER_TOKEN_ERROR(-98, "token不匹配"),

    USER_TOKEN_EXPIRE(-99, "token已过期"),

    IDENTITY_NOT_FOUND(-100, "无审核信息"),

    FAILED(-9000,"请求失败"),
    SIGN_ERROR(-9001,"签名错误"),
    PARAM_ERROR(-9002, "参数不匹配"),
    DELETE_ERROR(-9800, "删除失败"),
    NOT_FOUND(-9003, "找不到相关信息"),
    AUDITED(-9004, "此信息已被审核"),
    EXPIRED(-9005, "此信息已过期"),
    ID_EMPTY (-9900, "请传入ID"),
    JPUSH_TOKEN_EMPTY (-9901, "请传入JPushToken"),

    VERIFY_CODE_EXPIRE(-9902, "验证码已过期"),


    SMS_EMPTY(-10002, "验证码未传"),
    SMS_ERROR(-10003, "验证码错误"),
    SMS_EXPIRED(-10004,"验证码已过期"),
    OLD_PHONE_EXPIRED(-10005,"原手机验证码已失效，请重发"),
    PHONE_NOT_MATCH(-10004,"手机号格式不正确"),
    CUSTOMER_OR_PASSWD_ERROR(-10005, "账号或密码错误"),

    CUSTOMER_NOT_ACTIVE(-10006, "用户已被冻结，如有问题请联系客服"),
    CUSTOMER_PHONE_DUPLICATE(-10007, "此手机号已被使用"),
     CUSTOMER_NAME_EMPTY(-10008,"用户昵称未传"),
    CUSTOMER_NAME_ERROR(-10009,"昵称不能为11位纯数字且不能超过20位字符"),
    CUSTOMER_NAME_EXIST(-10010,"昵称已存在"),
    CUSTOMER_PASSWORD_EMPTY(-10011,"用户密码未传"),
    CUSTOMER_PASSWORD_ERROR(-10012,"用户密码格式不正确"),
    CUSTOMER_MESSAGE_EMPTY(-10013,"消息内容不能为空"),
    PAY_PASSWORD_EMPTY(-10011,"支付密码未传"),
    PAY_PASSWORD_ERROR(-10012,"支付密码格式不正确"),
    MESSAGE_TYPE_EMPTY(-10014,"消息类型不能为空"),
    ACCOUNT_CUSTOMER_NAME_EMPTY(-10015,"支付宝名称不能为空"),
    ACCOUNT_PAY_NAME_EMPTY(-10016,"支付宝账号不能为空"),
    CARD_NAME_EMPTY(-10017,"银行卡名称不能为空"),
    CARD_NUMBER_EMPTY(-10018,"银行卡账号不能为空"),
    BANK_NAME_EMPTY(-10019,"银行名称不能为空"),
    BANK_BRANCH_EMPTY(-10020,"银行支行名称不能为空"),
    ACCOUNT_TYPE_EMPTY(-10021,"账户类型不能为空"),
    CARDNUMBER_NOT_MATCH(-10022,"银行卡号格式不正确"),
    BALANCE_OUT(-10023,"账户余额不足"),
    ACCOUNT_NUMBER_ENOUGH(-10024,"账户数量已满"),
    CUSTOMER_PHONE_EMPTY(-10025,"手机号未传"),
    CUSTOMER_AVATAR_ERROR(-10026,"头像格式错误"),
    CUSTOMER_MESSAGE_TOLONG(-10027,"消息内容过长"),
    CUSTOMER_AVATAR_EMPTY(-10028,"用户头像未传"),
    PHONE_NOT_ACTIVE(-10029, "该手机号已被拉黑，如有问题请联系客服"),
    CODE_TYPE_EMPTY(-10030, "验证码类型未传"),
    CODE_TYPE_ERROR(-10031, "验证码类型未传"),
    CUSTOMER_EMPTY(-10032, "用户不存在"),
    PHONE_NOT_REGIST(-10033, "该手机号尚未注册，请先注册"),
    ACCOUNT_IS_DELETE(-10034, "该账号已被注销"),


    IDENTITY_NAME_EMPTY(-11001,"真实姓名未传"),
    IDENTITY_NAME_ERROR(-11002,"姓名只支持中文和英文"),
    IDENTITY_SEX_EMPTY(-11003,"性别未传"),
    IDENTITY_CARD_EMPTY(-11004,"身份证未传"),
    IDENTITY_FRONT_EMPTY(-11005,"正面照未传"),
    IDENTITY_BEHIND_EMPTY(-11006,"反面照未传"),
    IDENTITY_HAND_EMPTY(-11007,"手持照未传"),
    IDENTITY_DUPLICATE(-11008,"已申请，请耐心等待"),
    IDENTITY_DUPLICATE_PASSED(-11009,"申请已通过"),
    IDENTITY_NOT_MATCH(-11010,"身份证格式不正确"),

    TIMELINE_EMPTY(-14001, "请传入朋友圈信息"),
    TIMELINE_BODY_EMPTY(-14002, "请传入朋友圈内容"),
    TIMELINE_BODY_ERROR(-14003, "内容格式有误"),
    TIMELINE_USER_EMPTY(-14004, "请传入用户信息"),
    TIMELINE_MEDIA_EMPTY(-14005, "请传入朋友圈类型"),
    TIMELINE_PUBLISH_FAIL(-14006, "发布失败"),
    TIMELINE_RELATION_ERROR(-14007, "请传入朋友圈发布范围"),

    USER_PARAM_EMPTY(-12001,"请传入修改信息"),
    USER_PARAM_FEIFA(-12002,"、、、"),


    ADDRESS_NAME_EMPTY(-13001,"请传入名称"),
    ADDRESS_CANNOT_CREATE_PROVINCE(-13002, "无法创建省级地址"),
    ADDRESS_PARENT_EMPTY(-13003, "找不到父级地址"),
    ADDRESS_CITY_PINYIN_EMPTY(-13004, "请输入城市的拼音"),

    ADDRESS_PARAM_EMPTY(-13005, "参数不能为空"),
    ADDRESS_ID_EMPTY(-13006, "请传入修改地址的id"),
    ADDRESS_EMPTY(-13007, "指定地址不存在"),
    ADDRESS_PROVINCE_ERROR(-13008, "不允许修改省份信息"),
    ADDRESS_HAS_SON(-13009, "有下级子菜单，无法删除"),
    NAME_EMPTY(-13010,"收货人姓名未传"),
    ADDRESS_PHONE_EMPTY(-13011,"收货人手机未传"),
    ADDRESS_PROVINCE_EMPTY(-13012, "省份信息未传"),
    ADDRESS_CITY_EMPTY(-13013, "市份信息未传"),
    ADDRESS_DISTRICT_EMPTY(-13014, "区份信息未传"),
    ADDRESS_HOUSENUMBER_EMPTY(-13015, "门牌号信息未传"),
    ADDRESS_ADDRESSNAME_EMPTY(-13016, "地点名信息未传"),
    ADDRESS_LAT_EMPTY(-13017, "纬度信息未传"),
    ADDRESS_LNG_EMPTY(-13018, "经度信息未传"),
    LAT_LNG_EMPTY(-13019, "经纬度信息未传"),
    LAT_LNG_ERROR(-13020, "经纬度信息错误"),


    ADD_FRIEND_ERROR(-20001,"添加好友失败"),
    UPD_FRIEND_FAIL(-20002,"更新好友失败"),
    DEL_FRIEND_FAIL(-20003,"删除好友失败"),
    REJECT_FRIEND_FAIL(-20004,"拒绝好友失败"),
    UPD_RELATION_FAIL(-20005,"更新好友关系失败"),
    RELATION_TYPE_EMPTY(-20006, "请传入需要修改的关系"),
    RELATION_MODIFY_EMPTY(-20007, "请传入关系修改申请的id"),

    CREATE_GROUP_FAIL(-30001,"创建群聊失败"),
    ADD_GROUP_USER_FAIL(-30002,"添加群成员失败"),
    KICK_USER_FAIL(-30003,"踢除失败"),
    UPD_GROUP_FAIL(-30004,"更新群失败"),
    REMOVE_GROUP_FAIL(-30005,"解散群失败"),
    UPD_GROUP_LEADER_FAIL(-30006,"更换群主失败"),
    UPD_GROUP_ALIAS_FAIL(-30007,"更新群昵称失败"),
    LEADER_NOT_CHANGE(-30008,"请先移交群主"),
    LEAVE_GROUP_FAIL(-30009,"退出失败"),
    CREATE_TEMP_GROUP_FAIL(-30010,"拉取面对面群消息失败"),
    QUIT_AROUND_GROUP_FAIL(-30011,"退出面对面群失败"),

    MEMBER_EXCEED(-30013,"一次邀请成员不能超过200"),
    SET_TOP_FAIL(-30014,"设置置顶失败"),
    SET_MAIL_FAIL(-30015,"设置通讯录失败"),
    SET_MUTE_TEAM_FAIL(-30016,"设置免打扰失败"),
    SEND_FAIL(-30017,"发送失败"),

    GOODS_ID_EMPTY(-40001, "商品id未传"),
    SHOP_ID_EMPTY(-40001, "店铺id未传"),
    QUANTITY_EMPTY(-40003, "商品数量未传"),
    OPTION_ID_EMPTY(-40004, "商品规格id未传"),
    OPTION_NAME_EMPTY(-40005, "商品规格名称未传"),
    CART_IDS_EMPTY(-40006, "购物车ids未传"),
    QUANTITIES_EMPTY(-40007, "商品数量未传"),
    CART_EMPTY(-40008, "购物车信息不存在"),
    CART_IDS_NOT_MATCH(-40009, "购物车ids不合法"),
    REGIST_FAIL(-40010,"注册失败"),
    BUTTON_TYPE_ERROR(-40011,"按钮类型未传或不合法"),

    WEIXIN_ACCESSCODE_ERROR(-50001,"获取微信access_token错误"),
    WEIXIN_UPLOAD_ERROR(-50002,"微信上传图片错误"),
    WEIXIN_CONNECT_ERROR(-50003,"该微信号已经被绑定"),


    COMMUNITY_ID_EMPTY(-60001,"社区id未传"),
    ROOME_NAME_EMPTY(-60002,"房间名称未传"),
    PROPEIETOR_NAME_EMPTY(-60003,"业主名称未传"),
    PROPEIETOR_PHONE_EMPTY(-60004,"业主手机号未传"),
    PROPEIETOR_RELATION_EMPTY(-60005,"与业主关系未传"),
    NEAREST_COMMUNITY_EMPTY(-60006,"附近无小区"),
    COMMUNITY_IDENTITY_EXIST(-60008, "已有审核中的信息，请耐心等待"),
    CUSTOMER_IDENTITY_EMPTY(-60007,"实名认证通过后才能进行社区认证"),

    SOCIAL_ANONYMOUS_USER(-70001,"无具体用户信息"),
    SOCIAL_NOT_AUTHENTICATION(-70002,"用户信息未认证"),

    HOMEGOODSCATEGORY_EMPTY(-80001,"无该分类信息"),

    CATEGORYGOODS_EMPTY(-90001,"暂无信息");

    private String message;
    private int code;

    private ErrorCodeEnum(int code, String message){
        this.message = message;
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static String getValue(int key){
        for (ErrorCodeEnum typeEnum:ErrorCodeEnum.values()){
            if (typeEnum.getCode() == key){
                return typeEnum.getMessage();
            }
        }
        return null;
    }

    public static ErrorCodeEnum get(int key){
        for (ErrorCodeEnum typeEnum:ErrorCodeEnum.values()){
            if (typeEnum.getCode() == key){
                return typeEnum;
            }
        }
        return null;
    }
}
