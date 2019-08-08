package com.ly.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.common.vo.PageResult;
import com.ly.item.Brand;
import com.ly.item.mapper.BrandMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 70719
 */
@Service
public class BrandService {

    @Autowired
    BrandMapper brandMapper;

    public PageResult<Brand> queryByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //分页
        PageHelper.startPage(page, rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //过滤条件
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(LyExceptionEnum.BRAND_NOT_FOUND);
        }
        // 解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveBrand(Brand brand, List<Long> cids) throws Exception {
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count != 1) {
            throw new LyException(LyExceptionEnum.BRAND_SAVE_ERROR);
        }
        //给中间表添加数据
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1) {
                throw new LyException(LyExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateBrand(Brand brand, List<Long> cids) throws Exception {
        int count = brandMapper.updateByPrimaryKey(brand);
        if (count != 1) {
            throw new LyException(LyExceptionEnum.BRAND_UPDATE_ERROR);
        }
        //删除中间表的关系，重新建立关联
        count = brandMapper.deleteCategoryBrand(brand.getId());
        if(count == 0 ){
            throw new LyException(LyExceptionEnum.BRAND_UPDATE_ERROR);
        }
        //给中间表添加数据
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count != 1) {
                throw new LyException(LyExceptionEnum.BRAND_SAVE_ERROR);
            }
        }
    }

    public Brand queryById(Long id) throws Exception {
        Brand brand = new Brand();
        brand.setId(id);
        brand = brandMapper.selectOne(brand);
        if (brand == null) {
            throw new LyException(LyExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id)  throws Exception{
        Brand brand = new Brand();
        brand.setId(id);
        int count = brandMapper.delete(brand);
        if(count != 1){
            throw new LyException(LyExceptionEnum.BRAND_DELETE_ERROR);
        }
        //删除中间表的关系，重新建立关联
        count = brandMapper.deleteCategoryBrand(brand.getId());
        if(count == 0 ){
            throw new LyException(LyExceptionEnum.BRAND_DELETE_ERROR);
        }
    }

    public List<Brand> queryByCId(Integer cid) {
        List<Brand> brandList = brandMapper.queryByCId(cid);
        if(CollectionUtils.isEmpty(brandList)){
            throw new LyException(LyExceptionEnum.BRAND_NOT_FOUND);
        }
        return  brandList;
    }

    public Brand queryBrandById(Long id) {
        Brand brand = new Brand();
        brand.setId(id);
        return brandMapper.selectOne(brand);
    }

    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brandList = brandMapper.selectByIdList(ids);
        return  brandList;
    }
}
