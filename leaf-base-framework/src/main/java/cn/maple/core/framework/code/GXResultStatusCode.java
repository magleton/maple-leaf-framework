package cn.maple.core.framework.code;

import cn.hutool.json.JSONObject;

public interface GXResultStatusCode {
    /**
     * 获取code码
     *
     * @return int
     */
    int getCode();

    /**
     * 获取提示信息
     *
     * @return 提示信息
     */
    String getMsg();

    /**
     * 获取额外信息
     *
     * @return 额外信息
     */
    default JSONObject getExtraData() {
        return null;
    }
}
