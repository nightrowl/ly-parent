package com.ly.item.mapper;

import com.ly.item.SpecGroup;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.Mapper;

public interface SpecGroupMapper extends Mapper<SpecGroup>,IdListMapper<SpecGroup,Long>,InsertListMapper<SpecGroup> {
}
