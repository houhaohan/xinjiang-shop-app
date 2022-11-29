package com.pinet.sms.enums;


import com.pinet.core.util.StringUtil;

/**
 * Created by acer on 2017/11/9.
 */
public enum SmsTemplate {
    REGISTER("regist", "3124350", 15),
    SOCIAL_REGISTER("social_regist", "3124350", 15),
    LOGIN("login",  "3124350", 15),
    FORGET("forget", "3124350", 15),
    EDITPWDPAY("editpwdpay",  "3124350", 15),
    EDITPWDLOGIN("editpwdlogin",  "3124350", 15),
    VERIFYPHONE("verifyphone", "3124350", 15),
    EDITPHONE("editphone", "3124350", 15),
    PWD_APPLY("pwdapply", "3124350", 15),
    BIND("bind", "3124350", 15),
    COUPON("coupon", "3124350", 15),
    UNICOM("unicom", "3124350", 15),
    REALNAME("realname","3124350",15),
    RIDDLE("riddle","3124350",15),
    UNBINDACCOUNT("unbindaccount","3124350",15),
    RECEIVECOMMISSION("receivecommission","3124350",15),
    ORDER_NOTICE_FOR_SHOP("order_notice_for_shop","598148",15),
    ORDER_NOTICE_FOR_USER("order_notice_for_user","598149",15),
    BINDING_HOUSE("binding_house","480758",15),
    REMOVE_CUSTOMER("remove_customer","480759",15)

    ;

    private String name;

    private String template;

    //在验证通过后的有效可操作时间，以分钟为单位
    private int activeTime;

    SmsTemplate(String name ,  String template, int activeTime){
        this.name = name;
        this.template = template;
        this.activeTime = activeTime;
    }

    public int getActiveTime(){ return this.activeTime;}

    public String getName(){
        return this.name;
    }

    public String getTemplate(){
        return this.template;
    }

    public static SmsTemplate getTemplateByName(String name){
        if(StringUtil.isEmpty(name)){
            return null;
        }
        SmsTemplate[] smsTemplates = values();
        for(SmsTemplate smsTemplate : smsTemplates){
            if(name.equals(smsTemplate.getName()) ){
                return smsTemplate;
            }
        }
        return null;
    }
}
