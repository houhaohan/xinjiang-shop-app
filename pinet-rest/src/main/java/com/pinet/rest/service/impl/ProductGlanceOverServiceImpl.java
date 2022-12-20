package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.ProductGlanceOver;
import com.pinet.rest.mapper.ProductGlanceOverMapper;
import com.pinet.rest.service.IProductGlanceOverService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * <p>
 * 商品浏览表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
@Service
public class ProductGlanceOverServiceImpl extends ServiceImpl<ProductGlanceOverMapper, ProductGlanceOver> implements IProductGlanceOverService {

    @Override
    public void updateGlanceOverTimes(Long prodId) {
        ThreadLocalUtil.UserLogin userLogin = ThreadLocalUtil.getUserLogin();
        if(userLogin == null || userLogin.getUserId() == null || userLogin.getUserId() == 0){
            return;
        }
        QueryWrapper<ProductGlanceOver> wrapper = new QueryWrapper<>();
        wrapper.eq("prod_id",prodId);
        wrapper.eq("customer_id",userLogin.getUserId());
        ProductGlanceOver productGlanceOver = getOne(wrapper);
        if(productGlanceOver == null){
            ProductGlanceOver entity = new ProductGlanceOver();
            entity.setProdId(prodId);
            entity.setCustomerId(userLogin.getUserId());
            entity.setCreateTime(new Date());
            entity.setTimes(1);
            save(entity);
        }else {
            productGlanceOver.setTimes(productGlanceOver.getTimes() + 1);
            updateById(productGlanceOver);
        }
    }
}
