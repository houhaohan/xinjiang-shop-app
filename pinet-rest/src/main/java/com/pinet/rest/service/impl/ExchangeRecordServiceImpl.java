package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.constants.DB;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.ExchangeRecord;
import com.pinet.rest.entity.vo.ExchangeRecordListVo;
import com.pinet.rest.mapper.ExchangeRecordMapper;
import com.pinet.rest.service.IExchangeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 兑换记录 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@Service
@DS(DB.MASTER)
public class ExchangeRecordServiceImpl extends ServiceImpl<ExchangeRecordMapper, ExchangeRecord> implements IExchangeRecordService {

    @Override
    public Long addExchangeRecord(Long customerId, Long exchangeProductId, Integer score, Integer prodType, String prodName, Integer exchangeNum, Long shopId, String shopName) {
        ExchangeRecord exchangeRecord = new ExchangeRecord();
        exchangeRecord.setCustomerId(customerId);
        exchangeRecord.setExchangeProductId(exchangeProductId);
        exchangeRecord.setScore(score);
        exchangeRecord.setProdType(prodType);
        exchangeRecord.setProdName(prodName);
        exchangeRecord.setExchangeNum(exchangeNum);
        exchangeRecord.setShopId(shopId);
        exchangeRecord.setShopName(shopName);
        save(exchangeRecord);
        return exchangeRecord.getId();
    }

    @Override
    public List<ExchangeRecordListVo> exchangeRecordList(PageRequest request) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<ExchangeRecordListVo> page = new Page<>(request.getPageNum(),request.getPageSize());
        return baseMapper.selectExchangeRecordList(page,customerId);
    }
}
