package com.ly.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "tb_spu")
@Data
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    /**
     * // 1级类目
     */
    private Long cid1;
    /**
     * // 2级类目
     */
    private Long cid2;
    /**
     * // 3级类目
     */
    private Long cid3;
    /**
     * // 标题
     */
    private String title;
    /**
     * // 子标题
     */
    private String subTitle;
    /**
     * // 是否上架
     */
    private Boolean saleable;
    /**
     * // 是否有效，逻辑删除用
     */
    @JsonIgnore
    private Boolean valid;
    /**
     * // 创建时间
     */
    private Date createTime;
    /**
     * // 最后修改时间
     */
    @JsonIgnore
    private Date lastUpdateTime;
    /**
     * 忽略字段 数据库不存在 接受页面字段
     */
    @Transient
    private String cname;
    @Transient
    private String bname;
    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;

}