package com.ly.item.service;

import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.item.SpecGroup;
import com.ly.item.mapper.SpecGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *
 * @author 70719
 */
@Service
public class SpecGroupService {


    @Autowired
    SpecGroupMapper specGroupMapper;

    /**
     * 根据分类id查询
     * @param cid
     * @return
     */
    public List<SpecGroup> findByCId(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        if(CollectionUtils.isEmpty(specGroupList)){
            throw new LyException(LyExceptionEnum.SPEC_GROUP_NOT_FOUND);
        }

        return specGroupList;
    }

    /**
     * 添加规格参数
     * @param specGroup
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveSpecGroup(SpecGroup specGroup) throws Exception{
        specGroup.setId(null);
        int count = specGroupMapper.insert(specGroup);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPEC_GROUP_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSpecGroup(SpecGroup group) throws Exception {
        int count = specGroupMapper.updateByPrimaryKey(group);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPEC_GROUP_UPDATE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSpecGroup(Long id) throws Exception{
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        int count = specGroupMapper.delete(specGroup);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPEC_GROUP_DELETE_ERROR);
        }
    }
}
