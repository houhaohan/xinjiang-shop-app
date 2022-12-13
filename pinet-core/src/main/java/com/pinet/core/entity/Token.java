package com.pinet.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;

public class Token {

    /**
     * 用户id
     */
    private Long customerId;

    private String token;
    /**
     * 终端
     */
    private Integer terminal;
    /**
     * 是否已加入黑名单，注意，就算加入了黑名单，但3分钟内仍可以使用此token进行请求
     */
    private Integer isBlackmail;
    private Long expireTime;
    private Long createTime;
    /**
     * token过期后仍可访问的时间，防止出线一过期就不可使用此token
     */
    @TableField("grace_time")
    private Long graceTime;



    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public Integer getIsBlackmail() {
        return isBlackmail;
    }

    public void setIsBlackmail(Integer isBlackmail) {
        this.isBlackmail = isBlackmail;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getGraceTime() {
        return graceTime;
    }

    public void setGraceTime(Long graceTime) {
        this.graceTime = graceTime;
    }



    @Override
    public String toString() {
        return "CustomerToken{" +
                ", customerId=" + customerId +
                ", token=" + token +
                ", terminal=" + terminal +
                ", isBlackmail=" + isBlackmail +
                ", expireTime=" + expireTime +
                ", createTime=" + createTime +
                ", graceTime=" + graceTime +
                "}";
    }
}
