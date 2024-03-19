package com.pinet.core.constants;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 配送费常亮
 */
public class OrderPackageFeeConstant {

    public static BigDecimal packageFee(String dishType){
        if(Objects.equals("SINGLE",dishType)){
            return BigDecimal.ONE;
        }else if(Objects.equals("COMBO",dishType)){
            return new BigDecimal("2");
        }
        return BigDecimal.ZERO;

    }
}
