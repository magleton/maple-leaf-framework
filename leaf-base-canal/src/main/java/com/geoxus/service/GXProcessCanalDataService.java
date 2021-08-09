package com.geoxus.service;

import cn.hutool.core.lang.Dict;
import com.geoxus.dto.GXCanalDataDto;

public interface GXProcessCanalDataService {
    /**
     * 处理update操作
     *
     * @param canalData canal解析出来的数据
     * @param param     额外参数
     * @return Dict
     */
    Dict processUpdate(GXCanalDataDto canalData, Dict param);

    /**
     * 处理insert操作
     *
     * @param canalData canal解析出来的数据
     * @param param     额外参数
     * @return Dict
     */
    Dict processInsert(GXCanalDataDto canalData, Dict param);

    /**
     * 处理delete操作
     *
     * @param canalData canal解析出来的数据
     * @param param     额外参数
     * @return Dict
     */
    Dict processDelete(GXCanalDataDto canalData, Dict param);
}
