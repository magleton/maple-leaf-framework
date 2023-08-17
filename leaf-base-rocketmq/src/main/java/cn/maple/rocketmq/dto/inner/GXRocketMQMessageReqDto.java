package cn.maple.rocketmq.dto.inner;

import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.dto.req.GXBaseReqDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class GXRocketMQMessageReqDto extends GXBaseReqDto {
    /**
     * <p> 消息主题, 最长不超过255个字符; 由a-z, A-Z, 0-9, 以及中划线"-"和下划线"_"构成. </p>
     *
     * <p> <strong>一条合法消息本成员变量不能为空</strong> </p>
     */
    private String topic;

    /**
     * 消息队列tag
     */
    private String tag = "";

    /**
     * 内容
     */
    private String body;

    /**
     * 延时消息发送相对时间 秒值 列如:3600秒
     */
    private long deliverTime;

    /**
     * 消息的KEY
     */
    private String messageKey;

    public GXRocketMQMessageReqDto() {

    }

    public GXRocketMQMessageReqDto(Object body) {
        this.body = JSONUtil.toJsonStr(body);
    }

    public GXRocketMQMessageReqDto(String tag, Object body) {
        this.tag = tag;
        this.body = JSONUtil.toJsonStr(body);
    }

    public GXRocketMQMessageReqDto(String topic, String tag, Object body, int deliverTime, String messageKey) {
        this.topic = topic;
        this.tag = tag;
        this.deliverTime = deliverTime;
        this.messageKey = messageKey;
        this.body = JSONUtil.toJsonStr(body);
    }
}