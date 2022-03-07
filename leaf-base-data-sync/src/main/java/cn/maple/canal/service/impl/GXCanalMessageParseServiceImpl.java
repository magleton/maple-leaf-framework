package cn.maple.canal.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.canal.dto.GXCanalDataDto;
import cn.maple.canal.service.GXCanalMessageParseService;
import cn.maple.canal.service.GXProcessCanalDataService;
import cn.maple.core.framework.util.GXSpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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
        if (!JSONUtil.isTypeJSON(message)) {
            log.error("请传递JSON字符串");
            return dict;
        }
        final GXCanalDataDto canalDataDto = JSONUtil.toBean(message, GXCanalDataDto.class);
        final String serviceName = CharSequenceUtil.toCamelCase(CharSequenceUtil.format("{}_{}_Service", canalDataDto.getDatabase(), canalDataDto.getTable()));
        Object bean = GXSpringContextUtils.getBean(serviceName);
        if (Objects.isNull(bean)) {
            bean = GXSpringContextUtils.getBean("defaultProcessCanalDataService");
            if (Objects.isNull(bean)) {
                log.debug("{}不存在,请提供实现了{}接口的类型", serviceName, GXProcessCanalDataService.class.getSimpleName());
                return dict;
            }
        }
        if (!(bean instanceof GXProcessCanalDataService)) {
            log.debug("{}必须是{}的子类", serviceName, GXProcessCanalDataService.class.getSimpleName());
            return dict;
        }
        String type = Optional.ofNullable(canalDataDto.getType()).orElse("");
        switch (type) {
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
                log.debug("RabbitMQ监听的canal没有对应的操作");
                break;
        }
        return dict;
    }
}
