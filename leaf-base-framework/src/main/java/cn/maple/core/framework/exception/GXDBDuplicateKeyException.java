package cn.maple.core.framework.exception;

import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import org.yaml.snakeyaml.error.Mark;

/**
 * @author britton@126.com
 * @since 2021-10-19 15:00
 * <p>
 * 数据库主键重复异常
 */
public class GXDBDuplicateKeyException extends DuplicateKeyException {
    protected GXDBDuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
        super(contextMark, key, problemMark);
    }
}
