package com.pinet.rest.service;

import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.VipUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.VipRechargeDTO;
import com.pinet.rest.entity.vo.VipUserVO;

/**
 * <p>
 * VIP用户 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-06-04
 */
public interface IVipUserService extends IService<VipUser> {
    /**
     * 创建会员
     */
    void create(Customer customer,Long shopId);

    /**
     * 会员充值
     * @param dto
     */
    void recharge(VipRechargeDTO dto);

    /**
     * 会员信息
     * @param customerId
     * @return
     */
    VipUserVO info(Long customerId);

    /**
     * 根据用户ID查询
     * @param customerId
     * @return
     */
    VipUser getByCustomerId(Long customerId);
}
