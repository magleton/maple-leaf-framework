package com.geoxus.commons.services.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geoxus.commons.entities.GXDataDictEntity;
import com.geoxus.commons.mappers.GXDataDictMapper;
import com.geoxus.commons.services.GXDataDictService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GXDataDictServiceImpl extends ServiceImpl<GXDataDictMapper, GXDataDictEntity> implements GXDataDictService {
}
