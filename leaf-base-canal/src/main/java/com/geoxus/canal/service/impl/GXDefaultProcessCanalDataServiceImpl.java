package com.geoxus.canal.service.impl;

import cn.hutool.core.lang.Dict;
import com.geoxus.canal.dto.GXCanalDataDto;
import com.geoxus.canal.service.GXProcessCanalDataService;
import org.springframework.stereotype.Service;

@Service("defaultProcessCanalDataService")
public class GXDefaultProcessCanalDataServiceImpl implements GXProcessCanalDataService {
    /**
     * 处理update操作
     *
     * @param canalData canal解析出来的数据
     * @param param     额外参数
     * @return Dict
     */
    @Override
    public Dict processUpdate(GXCanalDataDto canalData, Dict param) {
        return null;
    }

    /**
     * 处理insert操作
     *
     * @param canalData canal解析出来的数据
     * @param param     额外参数
     * @return Dict
     */
    @Override
    public Dict processInsert(GXCanalDataDto canalData, Dict param) {
        return null;
    }

    /**
     * 处理delete操作
     *
     * @param canalData canal解析出来的数据
     * @param param     额外参数
     * @return Dict
     */
    @Override
    public Dict processDelete(GXCanalDataDto canalData, Dict param) {
        return null;
    }
}
