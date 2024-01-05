package com.pinet.rest.mapper;

import com.pinet.rest.entity.Area;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.AreaTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 地址区域表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-01-04
 */
public interface AreaMapper extends BaseMapper<Area> {

    /**
     * 根据层级查找地址
     * @param levels
     * @return
     */
    List<AreaTree> treeList(@Param("levels") List<Integer> levels);

}
