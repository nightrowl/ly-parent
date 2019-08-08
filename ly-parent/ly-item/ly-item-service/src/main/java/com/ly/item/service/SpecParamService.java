package com.ly.item.service;


import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.item.SpecParam;
import com.ly.item.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecParamService {


    @Autowired
    SpecParamMapper specParamMapper;


    public List<SpecParam> findSpecParams(Long gId ,Long cId , Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gId);
        specParam.setCid(cId);
        specParam.setSearching(searching);
        List<SpecParam> specParamList = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(specParamList)){
            throw new LyException(LyExceptionEnum.SPEC_PARAMS_NOT_FOUND);
        }
        return specParamList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSpecParams(SpecParam specParam) throws Exception{
        specParam.setId(null);
        int count = specParamMapper.insert(specParam);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPEC_PARAMS_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSpecParams(SpecParam specParam) throws Exception{
        int count = specParamMapper.updateByPrimaryKey(specParam);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPEC_GROUP_UPDATE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSpecParams(Long id) throws Exception{
        SpecParam specParam = new SpecParam();
        specParam.setId(id);
        int count = specParamMapper.delete(specParam);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPEC_PARAMS_DELETE_ERROR);
        }
    }
}
