package com.geoxus.feature.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.geoxus.core.framework.annotation.GXFieldComment;
import com.geoxus.core.datasource.entity.GXBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName("media_library")
@EqualsAndHashCode(callSuper = false)
public class GXMediaLibraryEntity extends GXBaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @GXFieldComment(zhDesc = "主键ID")
    private Integer id;

    @GXFieldComment(zhDesc = "模型类型")
    private String modelType;

    @GXFieldComment(zhDesc = "系统模型ID")
    private long coreModelId;

    @GXFieldComment(zhDesc = "目标记录ID(根据coreModelId进行确定)")
    private long targetId;

    @GXFieldComment(zhDesc = "集合名字")
    private String collectionName;

    @GXFieldComment(zhDesc = "文件名字")
    private String name;

    @GXFieldComment(zhDesc = "带后缀的文件名字")
    private String fileName;

    @GXFieldComment(zhDesc = "文件MIME")
    private String mimeType;

    @TableField(select = false)
    @GXFieldComment(zhDesc = "存储方式")
    private String disk;

    @GXFieldComment(zhDesc = "文件大小")
    private long size;

    @GXFieldComment(zhDesc = "维护者")
    private String manipulations = "[]";

    @GXFieldComment(zhDesc = "自定义属性")
    private String customProperties = "{}";

    @GXFieldComment(zhDesc = "响应式图片")
    private String responsiveImages = "{}";

    @GXFieldComment(zhDesc = "排序")
    private int orderColumn;

    @GXFieldComment(zhDesc = "资源类型")
    private String resourceType = "";

    @GXFieldComment(zhDesc = "OSS的URL地址")
    private String ossUrl;

    @TableField(exist = false)
    @GXFieldComment(zhDesc = "文件存放物理地址")
    private String filePath = "";
}
