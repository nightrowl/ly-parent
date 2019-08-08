package com.ly.item.mapper;

import com.ly.item.Stock;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *
 * @author 70719
 */
public interface StockMapper extends Mapper<Stock>,IdListMapper<Stock,Long>,InsertListMapper<Stock> {
}
