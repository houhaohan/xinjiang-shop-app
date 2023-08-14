package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DishListVO {

    @ApiModelProperty("数据总数")
    private Integer total;

    @ApiModelProperty("具体数据")
    private List<DishList> dataList;

    @Data
    private static class DishList{
        @ApiModelProperty("菜品ID")
        private String dishId;

        @ApiModelProperty("菜品名称")
        private String dishName;

        @ApiModelProperty("菜品描述")
        private String dishDesc;

        @ApiModelProperty("分类ID")
        private String categoryId;

        @ApiModelProperty("排序值")
        private Integer sort;

        @ApiModelProperty("助记码")
        private String helpCode;

        @ApiModelProperty("菜品类型， SINGLE：单菜 ，COMBO：套餐， SIDE：配料")
        private String dishType;

        @ApiModelProperty("菜品状态")
        private String state;

        @ApiModelProperty("称重菜标识, Y:是，N:否")
        private String weighFlag;
    }


}
