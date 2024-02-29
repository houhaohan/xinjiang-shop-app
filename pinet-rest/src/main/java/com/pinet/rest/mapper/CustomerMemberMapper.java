package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.CustomerMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.MemberRecommendProdDto;
import com.pinet.rest.entity.vo.MemberRecommendProdVo;
import com.pinet.rest.entity.vo.OrderListVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
public interface CustomerMemberMapper extends BaseMapper<CustomerMember> {

    IPage<MemberRecommendProdVo> selectMemberRecommendProd(Page<OrderListVo> page, @Param("dto") MemberRecommendProdDto dto);

    CustomerMember selectByCustomerId(@Param("customerId") Long customerId);
}
