package cn.maple.core.datasource.model;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.maple.core.framework.model.GXBaseModel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GXMyBatisModel extends GXBaseModel {
    @TableField(fill = FieldFill.INSERT)
    private Integer createdAt;

    @TableField(fill = FieldFill.UPDATE)
    private Integer updatedAt;

    @TableField(fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(fill = FieldFill.UPDATE)
    private String updatedBy;

    /**
     * 扩展字段
     */
    private Object ext;

    /**
     * 获取扩展字符串
     *
     * @return JSON
     */
    public String getExt() {
        return Convert.toStr(ext);
    }

    /**
     * 将传入的扩展数据转换成JSON字符串
     *
     * @param ext 扩展数据
     */
    public void setExt(Object ext) {
        if (ObjectUtil.isNull(ext)) {
            ext = "{}";
        }
        if (String.class.isAssignableFrom(ext.getClass()) && JSONUtil.isTypeJSON(ext.toString())) {
            this.ext = ext;
        } else {
            this.ext = JSONUtil.toJsonStr(ext);
        }
    }
}
