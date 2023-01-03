package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pinet.rest.entity.Banner;
import com.pinet.rest.mapper.BannerMapper;
import com.pinet.rest.service.IBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <p>
 * banner表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-14
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Override
    public List<Banner> bannerList() {
        List<Banner> bannerList = this.list(Wrappers.lambdaQuery(new Banner())
                .eq(Banner::getStatus, 0)
        );
        return bannerList;
    }
}
