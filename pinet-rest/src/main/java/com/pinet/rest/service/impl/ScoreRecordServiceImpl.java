package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.entity.ScoreRecord;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.enums.ScoreRecordTypeEnum;
import com.pinet.rest.mapper.ScoreRecordMapper;
import com.pinet.rest.service.ICustomerBalanceService;
import com.pinet.rest.service.ICustomerMemberService;
import com.pinet.rest.service.IScoreRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IShopService;
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
public class ScoreRecordServiceImpl extends ServiceImpl<ScoreRecordMapper, ScoreRecord> implements IScoreRecordService {
    @Resource
    private IShopService shopService;

    @Resource
    private ICustomerMemberService customerMemberService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Override
    public void addScoreRecord(Long shopId, String scoreTitle, Integer score, Long fkId, ScoreRecordTypeEnum scoreRecordTypeEnum) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Shop shop = shopService.getById(shopId);
        ScoreRecord scoreRecord = new ScoreRecord();
        scoreRecord.setCustomerId(customerId);
        scoreRecord.setShopId(shopId);
        scoreRecord.setShopName(shop.getShopName());
        scoreRecord.setScoreTitle(scoreTitle);
        scoreRecord.setScore(score);
        scoreRecord.setFkId(fkId);
        scoreRecord.setScoreType(scoreRecordTypeEnum.getCode());
        scoreRecord.setCustomerMember(customerMemberService.getMemberLevel(customerId));
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
