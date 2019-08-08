package com.ly.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 品牌表，一个品牌下有多个商品（spu），一对多关系
 * @author 70719
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_brand")
public class Brand {

    /**
     * 主键id
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id ;

    /**
     *品牌名称
     */
    private String name;

    /**
     * 品牌图片地址
     */
    private String image;

    /**
     * 品牌首字母
     */
    private String letter;
}
