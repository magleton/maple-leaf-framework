package cn.maple.redisson.module.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.maple.core.framework.ddd.repository.GXBaseSERepository;
import cn.maple.core.framework.dto.inner.GXBaseSEParamInnerDto;
import cn.maple.core.framework.util.GXCommonUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GXRediSearchRepository implements GXBaseSERepository {
    @Override
    public <R> List<R> search(GXBaseSEParamInnerDto dataIndexesParamInnerDto, Class<R> targetClass) {
        List<Dict> l = l();
        return GXCommonUtils.convertSourceListToTargetList(l, targetClass, dataIndexesParamInnerDto.getConvertMethodName(), dataIndexesParamInnerDto.getCopyOptions());
    }

    private List<Dict> l() {
        return CollUtil.newArrayList(Dict.create().set("AAAA", "AAAA"));
    }
}
