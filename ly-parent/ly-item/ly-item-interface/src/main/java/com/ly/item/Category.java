package com.ly.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 分类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_category")
public class Category {

    /**
     * 类目主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id ;

    /**
     * 类目名称
     */
    private String name ;

    /**
     * 父类目id,顶级类目填0
     */
    private Long parentId;

    /**
     * 是否为父节点 0,1
     */
    private Integer isParent;

    /**
     * 排序id
     */
    private Integer sort;
}
