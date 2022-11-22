package com.pinet.rest.service.impl;

import com.pinet.rest.entity.Activity;
import com.pinet.rest.mapper.ActivityMapper;
import com.pinet.rest.service.IActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 推广活动（已弃用） 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-11-21
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements IActivityService {

}
