package com.ly.search.pojo;

import com.ly.common.vo.PageResult;
import com.ly.item.Brand;
import com.ly.item.Category;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class SearchResult extends PageResult<Goods> {

    /**
     * //分类过滤条件
     */
    private List<Category> categories;

    /**
     * //品牌过滤条件
     */
    private List<Brand> brands;

    /**
     * //过滤参数key及带选项
     */
    private List<Map<String,Object>> specs;

    public SearchResult(){

    }
    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}