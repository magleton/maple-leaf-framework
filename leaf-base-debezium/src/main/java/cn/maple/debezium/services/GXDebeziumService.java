package cn.maple.debezium.services;

import cn.hutool.core.lang.Dict;

public interface GXDebeziumService {
    void processCaptureDataChange(Dict data);
}
