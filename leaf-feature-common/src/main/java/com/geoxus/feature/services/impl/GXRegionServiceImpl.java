package com.geoxus.feature.services.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.geoxus.feature.dao.GXRegionDao;
import com.geoxus.feature.entities.GXRegionEntity;
import com.geoxus.feature.mappers.GXRegionMapper;
import com.geoxus.feature.services.GXRegionService;
import com.geoxus.core.framework.util.GXChineseToPinYinUtils;
import com.geoxus.core.service.impl.GXDBBaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GXRegionServiceImpl extends GXDBBaseServiceImpl<GXRegionEntity, GXRegionMapper, GXRegionDao> implements GXRegionService {
    private static final String NAME_TAG = "name";

    private static final String PARENT_TAG = "parent_id";

    private static final String TYPE_TAG = "type";

    @Override
    public List<GXRegionEntity> getRegionTree() {
        List<GXRegionEntity> list = baseDao.list(new QueryWrapper<>());
        //把根分类区分出来
        List<GXRegionEntity> rootList = list.stream().filter(root -> root.getParentId() == 100000).collect(Collectors.toList());
        //把非根分类区分出来
        List<GXRegionEntity> subList = list.stream().filter(sub -> sub.getParentId() != 100000).collect(Collectors.toList());
        //递归构建结构化的分类信息
        rootList.forEach(root -> buildSubs(root, subList));
        return rootList;
    }

    /**
     * 递归构建
     *
     * @param parent 父级ID
     * @param subs   子集数据
     */
    private void buildSubs(GXRegionEntity parent, List<GXRegionEntity> subs) {
        List<GXRegionEntity> children = subs.stream().filter(sub -> sub.getParentId() == parent.getId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(children)) {
            parent.setChildren(children);
            //有子分类的情况 再次递归构建
            children.forEach(child -> buildSubs(child, subs));
        } else {
            parent.setChildren(null);
        }
    }

    @Override
    public List<GXRegionEntity> getRegion(Dict param) {
        QueryWrapper<GXRegionEntity> queryWrapper = new QueryWrapper<>();
        final String name = param.getStr(NAME_TAG);
        final Integer parentId = param.getInt(PARENT_TAG);
        final Short type = param.getShort(TYPE_TAG);
        if (CharSequenceUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        } else {
            if (null != parentId) {
                queryWrapper.eq("parent_id", parentId);
            }
            queryWrapper.eq("type", type == null ? 1 : type);
        }
        return baseDao.list(queryWrapper);
    }

    @Override
    public boolean convertNameToPinYin() {
        final List<GXRegionEntity> list = baseDao.list(new QueryWrapper<>());
        for (GXRegionEntity entity : list) {
            final String firstLetter = GXChineseToPinYinUtils.getFirstLetter(entity.getName());
            final String fullLetter = GXChineseToPinYinUtils.getFullLetter(entity.getName());
            entity.setFirstLetter(firstLetter);
            entity.setPinyin(fullLetter);
            baseDao.updateById(entity);
        }
        return false;
    }
}