package cn.maple.sse.dto;

import cn.maple.core.framework.dto.GXBaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GXMessageDto extends GXBaseDto {
    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 传输数据体(json)
     */
    private String message;
}