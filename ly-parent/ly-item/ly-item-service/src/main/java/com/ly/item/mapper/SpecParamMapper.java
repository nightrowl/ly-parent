package com.ly.item.mapper;

import com.ly.item.SpecParam;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author 70719
 */
public interface SpecParamMapper extends Mapper<SpecParam>,IdListMapper<SpecParam,Long>,InsertListMapper<SpecParam> {
}
