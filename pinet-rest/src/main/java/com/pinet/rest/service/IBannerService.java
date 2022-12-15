package com.pinet.rest.service;

import com.pinet.rest.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * banner表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-14
 */
public interface IBannerService extends IService<Banner> {

    List<Banner> bannerList();
}
