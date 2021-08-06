package com.geoxus.commons.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.commons.entities.GXRegionEntity;
import com.geoxus.commons.services.GXRegionService;
import com.geoxus.core.common.util.GXResultUtils;
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
    public GXResultUtils<List<GXRegionEntity>> getRegionTree() {
        List<GXRegionEntity> list = regionService.getRegionTree();
        return GXResultUtils.ok(list);
    }

    /**
     * 通过条件获取区域
     *
     * @param param
     * @return GXResultUtils
     */
    @PostMapping("/get-region")
    public GXResultUtils<List<GXRegionEntity>> getRegion(@RequestBody Dict param) {
        List<GXRegionEntity> list = regionService.getRegion(param);
        return GXResultUtils.ok(list);
    }
}