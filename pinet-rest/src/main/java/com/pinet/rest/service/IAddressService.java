package com.pinet.rest.service;

import com.pinet.rest.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.AddressIdVo;

/**
 * <p>
 * 地址表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-09
 */
public interface IAddressService extends IService<Address> {

    /**
     * 根据省市区名称查找ID
     * @param province
     * @param city
     * @param district
     * @return
     */
    AddressIdVo selectIdByName(String province, String city, String district);

}
