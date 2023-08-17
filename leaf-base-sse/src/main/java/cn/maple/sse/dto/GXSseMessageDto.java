package cn.maple.sse.dto;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.maple.core.framework.dto.GXBaseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class GXSseMessageDto extends GXBaseDto {
    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 消息ID
     */
    private String msgId = IdUtil.fastUUID();

    /**
     * 传输数据
     */
    private Dict data;

    /**
     * 事件名字
     */
    private String eventName;

    /**
     * 从新连接时间
     * 默认 2秒
     */
    private long reconnectTimeMillis = 2_000L;

    /**
     * 注释
     */
    private String comment = "";
}