package com.geoxus.feature.controller;

import cn.hutool.core.lang.Dict;
import com.geoxus.common.dto.protocol.res.GXPaginationProtocol;
import com.geoxus.common.util.GXResultUtil;
import com.geoxus.feature.services.GXDataDictService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/generate/dict")
public class GXDataDictController {
    @Resource
    private GXDataDictService dataDictService;

    /**
     * 获取所有区域树
     *
     * @return GXResultUtils
     */
    @PostMapping("/list-or-search")
    public GXResultUtil<GXPaginationProtocol<Dict>> listOrSearchPage(@RequestBody Dict condition) {
        final GXPaginationProtocol<Dict> pagination = dataDictService.listOrSearchPage(condition);
        return GXResultUtil.ok(pagination);
    }
}