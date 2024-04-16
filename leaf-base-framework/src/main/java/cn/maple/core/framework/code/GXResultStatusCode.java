package cn.maple.core.framework.code;

import cn.hutool.core.lang.Dict;

@SuppressWarnings("all")
public interface GXResultStatusCode {
    /**
     * 获取code码
     *
     * @return int
     */
    default int getCode() {
        return this.getCode();
    }

    /**
     * 获取提示信息
     *
     * @return 提示信息
     */
    default String getMsg() {
        return this.getMsg();
    }

    /**
     * 获取额外信息
     *
     * @return 额外信息
     */
    default Dict getExtraData() {
        return Dict.create();
    }
}
