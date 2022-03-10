package cn.maple.redisson.module.service.impl;

import cn.hutool.core.lang.Dict;
import cn.maple.redisson.module.dto.req.GXRediSearchQueryParamReqDto;
import cn.maple.redisson.module.repository.GXRediSearchRepository;
import cn.maple.redisson.module.service.GXRediSearchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GXRediSearchServiceImpl implements GXRediSearchService {
    @Resource
    private GXRediSearchRepository rediSearchRepository;

    /**
     * 按照条件进行搜索
     *
     * @param paramInnerDto 搜索条件
     * @return List
     */
    @Override
    public List<Dict> search(GXRediSearchQueryParamReqDto paramInnerDto) {
        return rediSearchRepository.search(paramInnerDto, Dict.class);
    }
}
