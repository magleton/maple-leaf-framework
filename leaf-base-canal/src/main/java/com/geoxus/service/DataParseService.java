package com.geoxus.service;

import cn.hutool.core.lang.Dict;

public interface DataParseService {
    /**
     * 获取canal消息中的数据库名字
     *
     * @param data
     * @return
     */
    String dataBase(Dict data);
}
