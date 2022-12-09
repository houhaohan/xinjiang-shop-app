package com.pinet.rest.mapper;

import com.pinet.rest.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.AddressIdVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 地址表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-09
 */
public interface AddressMapper extends BaseMapper<Address> {


    /**
     * 根据省市区名称查找Id
     * @param province
     * @param city
     * @param district
     * @return
     */
    AddressIdVo selectIdByName(@Param("province") String province, @Param("city") String city, @Param("district") String district);
}
