package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.CouponGrantRecord;
import com.pinet.rest.mapper.CouponGrantRecordMapper;
import com.pinet.rest.service.ICouponGrantRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券发放记录 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-14
 */
@Service
@DS(DB.MASTER)
public class CouponGrantRecordServiceImpl extends ServiceImpl<CouponGrantRecordMapper, CouponGrantRecord> implements ICouponGrantRecordService {

}
