package com.ly.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum  LyExceptionEnum {

    PRICE_CAN_NOT_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类不存在"),
    BRAND_NOT_FOUND(404,"商品品牌不存在"),
    BRAND_SAVE_ERROR(500,"品牌新增失败"),
    BRAND_UPDATE_ERROR(500,"品牌修改失败"),
    INVALID_FILE_TYPE(500,"图片类型不支持"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    BRAND_DELETE_ERROR(500,"品牌删除失败"),
    CATEGORY_SAVE_ERROR(500,"商品分类新增失败"),
    CATEGORY_UPDATE_ERROR(500,"商品分类修改失败"),
    SPEC_GROUP_NOT_FOUND(404,"该分类下没有规格"),
    SPEC_GROUP_SAVE_ERROR(500,"添加规格参数失败"),
    SPEC_GROUP_UPDATE_ERROR(500,"添加规格参数失败"),
    SPEC_GROUP_DELETE_ERROR(500,"添加规格参数失败"),
    SPEC_PARAMS_NOT_FOUND(404,"该规格下没有规格参数"),
    SPEC_PARAMS_SAVE_ERROR(500,"规则参数新增失败"),
    SPEC_PARAMS_UPDATE_ERROR(500,"规则参数修改失败"),
    SPEC_PARAMS_DELETE_ERROR(500,"规则参数删除失败"),
    SPU_SAVE_ERROR(500,"商品公共spu新增失败"),
    GOODS_DETAIL_NOT_FOUND(404,"spu详情不存在"),
    GOODS_SKU_NOT_FOUND(404,"sku不存在" ),
    GOODS_STOCK_NOT_FOUND(404,"商品库存不存在" ),
    SPU_UPDATE_ERROR(500,"商品公共spu修改失败"),
    GOODS_DELETE_ERROR(500,"商品删除失败"),
    GOODS_FLAG_UPDATE_ERROR(500,"商品状态修改失败" );
    private int code ;
    private String message;

}
