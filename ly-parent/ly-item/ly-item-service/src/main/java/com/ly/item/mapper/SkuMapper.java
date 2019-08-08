package com.ly.item.mapper;

import com.ly.item.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *
 * @author 70719
 */
public interface SkuMapper extends Mapper<Sku>,IdListMapper<Sku,Long>,InsertListMapper<Sku> {
}
