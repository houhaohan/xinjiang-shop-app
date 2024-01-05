package com.pinet.rest.service;

import com.pinet.rest.entity.Area;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.AreaTree;
import com.pinet.rest.entity.vo.AreaPyGroupVo;

import java.util.List;

/**
 * <p>
 * 地址区域表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-04
 */
public interface IAreaService extends IService<Area> {

    List<AreaTree> treeList(List<Integer> levels);

    List<AreaPyGroupVo>  cityPyGroupList(String name);
}
