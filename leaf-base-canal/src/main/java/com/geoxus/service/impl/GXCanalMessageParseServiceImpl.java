package com.geoxus.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.geoxus.core.framework.util.GXSpringContextUtil;
import com.geoxus.core.framework.dto.GXCanalDataDto;
import com.geoxus.service.GXCanalMessageParseService;
import com.geoxus.service.GXProcessCanalDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class GXCanalMessageParseServiceImpl implements GXCanalMessageParseService {
    /**
     * 解析RabbitMQ中canal的消息信息
     *
     * @param message 消息
     * @return GXCanalDataDto
     */
    @Override
    public Dict parseMessage(String message) {
        String operator = "operator";
        Dict dict = Dict.create();
        if (!JSONUtil.isJson(message)) {
            log.error("请传递JSON字符串");
            return dict;
        }
        final GXCanalDataDto canalDataDto = JSONUtil.toBean(message, GXCanalDataDto.class);
        final String serviceName = CharSequenceUtil.toCamelCase(CharSequenceUtil.format("{}_{}_Service", canalDataDto.getDatabase(), canalDataDto.getTable()));
        Object bean = GXSpringContextUtil.getBean(serviceName);
        if (Objects.isNull(bean)) {
            bean = GXSpringContextUtil.getBean("defaultProcessCanalDataService");
            if (Objects.isNull(bean)) {
                log.info("{}不存在,请提供实现了{}接口的类型", serviceName, GXProcessCanalDataService.class.getSimpleName());
                return dict;
            }
        }
        if (!(bean instanceof GXProcessCanalDataService)) {
            log.info("{}必须是{}的子类", serviceName, GXProcessCanalDataService.class.getSimpleName());
            return dict;
        }
        switch (canalDataDto.getType()) {
            case "UPDATE":
                dict = ((GXProcessCanalDataService) bean).processUpdate(canalDataDto, Dict.create().set(operator, "update"));
                break;
            case "INSERT":
                ((GXProcessCanalDataService) bean).processInsert(canalDataDto, Dict.create().set(operator, "insert"));
                break;
            case "DELETE":
                ((GXProcessCanalDataService) bean).processDelete(canalDataDto, Dict.create().set(operator, "delete"));
                break;
            default:
                log.info("RabbitMQ监听的canal没有对应的操作");
                break;
        }
        return dict;
    }
}
