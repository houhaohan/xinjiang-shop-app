package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.entity.ScoreRecord;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.enums.ScoreRecordTypeEnum;
import com.pinet.rest.mapper.ScoreRecordMapper;
import com.pinet.rest.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 积分明细 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-12-22
 */
@Service
@RequiredArgsConstructor
public class ScoreRecordServiceImpl extends ServiceImpl<ScoreRecordMapper, ScoreRecord> implements IScoreRecordService {

    private final IShopService shopService;
    private final IVipUserService vipUserService;
    private final ICustomerBalanceService customerBalanceService;

    @Override
    public void addScoreRecord(Long shopId, String scoreTitle, Double score, Long fkId, ScoreRecordTypeEnum scoreRecordTypeEnum,Long customerId) {
        Shop shop = shopService.getById(shopId);
        ScoreRecord scoreRecord = new ScoreRecord();
        scoreRecord.setCustomerId(customerId);
        scoreRecord.setShopId(shopId);
        scoreRecord.setShopName(shop.getShopName());
        scoreRecord.setScoreTitle(scoreTitle);
        scoreRecord.setScore(score);
        scoreRecord.setFkId(fkId);
        scoreRecord.setScoreType(scoreRecordTypeEnum.getCode());
        scoreRecord.setCustomerMember(vipUserService.getLevelByCustomerId(customerId));
        CustomerBalance customerBalance = customerBalanceService.getByCustomerId(customerId);
        scoreRecord.setCustomerScore(customerBalance.getScore() + score);
        save(scoreRecord);
    }

    @Override
    public List<ScoreRecord> pageList(PageRequest pageRequest) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<ScoreRecord> page = new Page<>(pageRequest.getPageNum(),pageRequest.getPageSize());
        Page<ScoreRecord> pageList = baseMapper.selectPageList(page,customerId);
        return pageList.getRecords();
    }
}
