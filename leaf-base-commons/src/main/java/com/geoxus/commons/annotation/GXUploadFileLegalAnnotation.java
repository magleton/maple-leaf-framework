package com.geoxus.commons.annotation;

import java.lang.annotation.*;

/**
 * 检测上传文件的用户的合法性
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GXUploadFileLegalAnnotation {
}
