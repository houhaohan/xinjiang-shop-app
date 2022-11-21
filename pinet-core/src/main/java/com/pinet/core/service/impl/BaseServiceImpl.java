package com.pinet.core.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.service.BaseService;

public class BaseServiceImpl<M extends BaseMapper<T>,T extends BaseEntity>  extends ServiceImpl<M,T> implements BaseService<T> {

}
