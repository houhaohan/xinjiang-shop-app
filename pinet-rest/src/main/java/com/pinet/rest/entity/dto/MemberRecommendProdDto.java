package com.pinet.rest.entity.dto;

import com.pinet.core.page.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @program: xinjiang-shop-app
 * @description: 推荐商品dto
 * @author: hhh
 * @create: 2024-02-29 16:38
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberRecommendProdDto extends PageRequest {
    private Long shopId;
}
