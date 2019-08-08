package com.ly.item.mapper;

import com.ly.item.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * 分类mapper
 */
public interface CategoryMapper extends Mapper<Category>,IdListMapper<Category,Long>,InsertListMapper<Category> {
}
