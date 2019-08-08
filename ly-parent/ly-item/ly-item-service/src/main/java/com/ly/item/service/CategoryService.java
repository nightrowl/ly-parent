package com.ly.item.service;

import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.item.Category;
import com.ly.item.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 分类业务层
 */
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    public ResponseEntity<List<Category>> queryListByPid(Long pid)  throws Exception{
        Category t = new Category();
        t.setParentId(pid);
        List<Category> categoryList= categoryMapper.select(t);
        if(CollectionUtils.isEmpty(categoryList)){
            throw new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return ResponseEntity.ok(categoryList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveCategory(Category category) throws Exception{
        System.out.println(category);
        category.setId(null);
        int count = categoryMapper.insert(category);
        if(count != 1){
            throw new LyException(LyExceptionEnum.CATEGORY_SAVE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(Category category) throws Exception{
        System.out.println(category);
        //根据id查询出结果
        Category category1 = categoryMapper.selectByPrimaryKey(category);
        if(category1 == null){
            throw  new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        if(!StringUtils.isEmpty(category.getName())){
            category1.setName(category.getName());
        }
        int count = categoryMapper.updateByPrimaryKey(category1);
        if(count != 1){
            throw new LyException(LyExceptionEnum.CATEGORY_UPDATE_ERROR);
        }
    }

    public List<Category> queryByIds(List<Long> ids) {
        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(categoryList)){
            throw  new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return categoryList;
    }

    public List<Category> queryCategoryByIds(List<Long> ids) {
        List<Category> categoryList = categoryMapper.selectByIdList(ids);
        return categoryList;
    }
}
