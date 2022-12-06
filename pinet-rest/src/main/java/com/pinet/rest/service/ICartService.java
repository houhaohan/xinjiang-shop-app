package com.pinet.rest.service;

import com.pinet.rest.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.vo.CartListVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface ICartService extends IService<Cart> {

    List<CartListVo> cartList(CartListDto dto);
}
