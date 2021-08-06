package com.geoxus.commons.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.common.annotation.GXFieldCommentAnnotation;
import com.geoxus.core.common.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName("media_library")
@EqualsAndHashCode(callSuper = false)
public class GXMediaLibraryEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @GXFieldCommentAnnotation(zhDesc = "主键ID")
    private Integer id;

    @GXFieldCommentAnnotation(zhDesc = "模型类型")
    private String modelType;

    @GXFieldCommentAnnotation(zhDesc = "系统模型ID")
    private long coreModelId;

    @GXFieldCommentAnnotation(zhDesc = "目标记录ID(根据coreModelId进行确定)")
    private long targetId;

    @GXFieldCommentAnnotation(zhDesc = "集合名字")
    private String collectionName;

    @GXFieldCommentAnnotation(zhDesc = "文件名字")
    private String name;

    @GXFieldCommentAnnotation(zhDesc = "带后缀的文件名字")
    private String fileName;

    @GXFieldCommentAnnotation(zhDesc = "文件MIME")
    private String mimeType;

    @TableField(select = false)
    @GXFieldCommentAnnotation(zhDesc = "存储方式")
    private String disk;

    @GXFieldCommentAnnotation(zhDesc = "文件大小")
    private long size;

    @GXFieldCommentAnnotation(zhDesc = "维护者")
    private String manipulations = "[]";

    @GXFieldCommentAnnotation(zhDesc = "自定义属性")
    private String customProperties = "{}";

    @GXFieldCommentAnnotation(zhDesc = "响应式图片")
    private String responsiveImages = "{}";

    @GXFieldCommentAnnotation(zhDesc = "排序")
    private int orderColumn;

    @GXFieldCommentAnnotation(zhDesc = "资源类型")
    private String resourceType = "";

    @GXFieldCommentAnnotation(zhDesc = "OSS的URL地址")
    private String ossUrl;

    @TableField(exist = false)
    @GXFieldCommentAnnotation(zhDesc = "文件存放物理地址")
    private String filePath = "";
}
