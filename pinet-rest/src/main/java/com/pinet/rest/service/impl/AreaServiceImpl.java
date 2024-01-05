package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.Area;
import com.pinet.rest.entity.vo.AreaTree;
import com.pinet.rest.mapper.AreaMapper;
import com.pinet.rest.service.IAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 地址区域表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-04
 */
@Service
@DS(DB.SLAVE)
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {

    @Override
    public List<AreaTree> treeList(List<Integer> levels) {
        List<AreaTree> list = baseMapper.treeList(levels);
        return listToTree(list);
    }
    /**
     * 递归查找所有下级
     * @param list
     * @return
     */
    public static List<AreaTree> listToTree(List<AreaTree> list) {
        //用递归找子。
        List<AreaTree> treeList = new ArrayList<>();
        for (AreaTree tree : list) {
            //根目录的parentId为0
            if (tree.getParentId() == 0) {
                treeList.add(findChildren(tree, list));
            }
        }
        return treeList;
    }

    private static AreaTree findChildren(AreaTree tree, List<AreaTree> list) {
        for (AreaTree node : list) {
            if (node.getParentId().equals(tree.getId())) {
                if (tree.getChildren() == null) {
                    tree.setChildren(new ArrayList<>());
                }
                tree.getChildren().add(findChildren(node, list));
            }
        }
        return tree;
    }
}
