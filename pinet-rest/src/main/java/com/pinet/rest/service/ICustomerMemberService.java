package com.pinet.rest.service;

import com.pinet.core.page.PageRequest;
import com.pinet.rest.entity.CustomerMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.bo.RecommendTimeBo;
import com.pinet.rest.entity.dto.MemberRecommendProdDto;
import com.pinet.rest.entity.dto.PayDto;
import com.pinet.rest.entity.dto.RecommendListDto;
import com.pinet.rest.entity.vo.MemberRecommendProdVo;
import com.pinet.rest.entity.vo.MemberVo;
import com.pinet.rest.entity.vo.ProductListVo;
import com.pinet.rest.entity.vo.RecommendListVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
public interface ICustomerMemberService extends IService<CustomerMember> {

    /**
     * 店帮主充值
     * @param dto
     * @return
     */
    Object recharge(PayDto dto);

    /**
     * 根据用户id查询
     * @param customerId
     * @return
     */
    CustomerMember getByCustomerId(Long customerId);

    /**
     * 会员中心
     * @return
     */
    MemberVo member();

    /**
     * 推荐记录vo
     * @return
     */
    List<RecommendListVo> recommendList(RecommendListDto dto);

    /**
     * 获取用户会员等级
     * @param customerId
     * @return
     */
    Integer getMemberLevel(Long customerId);

    /**
     * 推荐商品
     * @param request
     * @return
     */
    List<MemberRecommendProdVo> memberRecommendProd(MemberRecommendProdDto dto);

    /**
     * 会员中心推荐记录
     * @return
     */
    List<RecommendTimeBo> recommendIndexList();

    List<ProductListVo> productList(Long shopId);
}
