package com.ly.item;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_spu_detail")
@Data
public class SpuDetail {
    /**
     * // 对应的SPU的id
     */
    @Id
    private Long spuId;
    /**
     * // 商品描述
     */
    private String description;
    /**
     * // 商品特殊规格的名称及可选值模板
     */
    private String specialSpec;
    /**
     * // 商品的全局规格属性
     */
    private String genericSpec;
    /**
     * // 包装清单
     */
    private String packingList;
    /**
     * // 售后服务
     */
    private String afterService;
}