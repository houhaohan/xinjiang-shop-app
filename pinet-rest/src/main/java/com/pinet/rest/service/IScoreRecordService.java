package com.pinet.rest.service;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.ScoreRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.enums.ScoreRecordTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 积分明细 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-12-22
 */
public interface IScoreRecordService extends IService<ScoreRecord> {
    /**
     * 添加积分流水
     * @param shopId 店铺id
     * @param scoreTitle 积分流水标题
     * @param score 积分(扣减传负数)
     * @param fkId 外键id
     * @param scoreRecordTypeEnum 积分类型枚举
     * @param customerId 用户id
     */
    void addScoreRecord(Long shopId, String scoreTitle, Double score, Long fkId, ScoreRecordTypeEnum scoreRecordTypeEnum,Long customerId);

    /**
     * 积分明细列表
     * @param pageRequest
     * @return
     */
    List<ScoreRecord> pageList(PageRequest pageRequest);
}
