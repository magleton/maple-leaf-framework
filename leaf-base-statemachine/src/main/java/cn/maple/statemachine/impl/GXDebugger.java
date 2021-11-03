package cn.maple.statemachine.impl;

import lombok.extern.slf4j.Slf4j;

/**
 * GXDebugger, This is used to decouple Logging framework dependency
 */
@Slf4j
public class GXDebugger {
    private static boolean isDebugOn = false;

    private GXDebugger() {
    }

    public static void debug(String message) {
        if (isDebugOn) {
            log.info(message);
        }
    }

    public static void enableDebug() {
        isDebugOn = true;
    }
}
