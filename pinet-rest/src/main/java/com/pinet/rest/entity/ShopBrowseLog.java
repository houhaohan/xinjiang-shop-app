package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wlbz
 * @since 2023-08-16
 */
@Getter
@Setter
@TableName("shop_browse_log")
@ApiModel(value = "ShopBrowseLog对象", description = "")
public class ShopBrowseLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long shopId;

    private Long customerId;

    private String ip;


}
