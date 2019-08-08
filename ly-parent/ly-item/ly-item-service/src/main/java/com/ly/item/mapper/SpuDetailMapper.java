package com.ly.item.mapper;

import com.ly.item.SpuDetail;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 *
 * @author 70719
 */
public interface SpuDetailMapper extends Mapper<SpuDetail>,IdListMapper<SpuDetail,Long>,InsertListMapper<SpuDetail> {
}
