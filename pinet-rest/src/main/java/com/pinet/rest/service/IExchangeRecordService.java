package com.pinet.rest.service;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.ExchangeRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.ExchangeRecordListVo;

import java.util.List;

/**
 * <p>
 * 兑换记录 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
public interface IExchangeRecordService extends IService<ExchangeRecord> {

    /**
     * 添加兑换记录
     * @param customerId 用户id
     * @param exchangeProductId 兑换商品id
     * @param score 积分
     * @param prodType 商品类型
     * @param prodName 商品名称
     * @param exchangeNum 兑换数量
     * @param shopId 店铺id
     * @param shopName 店铺名称
     * @return 兑换记录id
     */
    Long addExchangeRecord(Long customerId,Long exchangeProductId,Integer score,Integer prodType,String prodName,
                           Integer exchangeNum,Long shopId,String shopName);

    List<ExchangeRecordListVo> exchangeRecordList(PageRequest request);
}
