package com.geoxus.ueditor.support.qiniu;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启七牛云上传
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GXQiNiuUploader.class)
public @interface GXEnableQiNiuUploaderAnnotation {
}
