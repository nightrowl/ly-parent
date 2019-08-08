package com.ly.item.mapper;

import com.ly.item.Spu;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *
 * @author 70719
 */
public interface GoodsMapper extends Mapper<Spu>,IdListMapper<Spu,Long>,InsertListMapper<Spu> {
}
