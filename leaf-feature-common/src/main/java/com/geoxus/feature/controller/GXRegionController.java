package com.geoxus.feature.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.feature.entities.GXRegionEntity;
import com.geoxus.feature.services.GXRegionService;
import com.geoxus.core.framework.util.GXResultUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/generate/region")
public class GXRegionController {
    @Resource
    private GXRegionService regionService;

    /**
     * 获取所有区域树
     *
     * @return GXResultUtils
     */
    @GetMapping("/get-region-tree")
    public GXResultUtil<List<GXRegionEntity>> getRegionTree() {
        List<GXRegionEntity> list = regionService.getRegionTree();
        return GXResultUtil.ok(list);
    }

    /**
     * 通过条件获取区域
     *
     * @param param
     * @return GXResultUtils
     */
    @PostMapping("/get-region")
    public GXResultUtil<List<GXRegionEntity>> getRegion(@RequestBody Dict param) {
        List<GXRegionEntity> list = regionService.getRegion(param);
        return GXResultUtil.ok(list);
    }
}