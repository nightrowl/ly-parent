package com.ly.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.common.utils.JsonUtils;
import com.ly.common.vo.PageResult;
import com.ly.item.*;
import com.ly.search.client.BrandClient;
import com.ly.search.client.CategoryClient;
import com.ly.search.client.GoodsClient;
import com.ly.search.client.SpecClient;
import com.ly.search.pojo.Goods;
import com.ly.search.pojo.SearchRequest;
import com.ly.search.pojo.SearchResult;
import com.ly.search.repository.GoodRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    CategoryClient categoryClient;

    @Autowired
    BrandClient brandClient;

    @Autowired
    GoodsClient goodsClient;

    @Autowired
    SpecClient specClient;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    GoodRepository goodRepository;

    public Goods buildGoods(Spu spu) throws Exception {
        Long spuId = spu.getId();
        //查询分类
        List<Category> categoryList = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categoryList)) {
            throw new LyException(LyExceptionEnum.CATEGORY_NOT_FOUND);
        }
        //需要分类的名字，所以需要处理一下
        List<String> categoryNameList = categoryList.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(LyExceptionEnum.BRAND_NOT_FOUND);
        }
        //设置all属性,搜过字段
        String all = spu.getTitle() + StringUtils.join(categoryNameList, " ") + brand.getName();
        //查询sku
        ResponseEntity<List<Sku>> listResponseEntity = goodsClient.querySkuBySpuId(spuId);
        List<Sku> skuList = listResponseEntity.getBody();
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(LyExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        List<Map<String, Object>> skus = new ArrayList<>();
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            //工具类（StringUtils）截取逗号前面字段
            map.put("images", StringUtils.substringBefore(sku.getImages(), ","));
            skus.add(map);
            //处理价格
            priceList.add(sku.getPrice());
        }
        //查询规格参数
        ResponseEntity specParams = specClient.findSpecParams(null, spu.getCid3(), true);
        List<SpecParam> params = (List) specParams.getBody();
        if (params == null) {
            throw new LyException(LyExceptionEnum.SPEC_PARAMS_NOT_FOUND);
        }
        //查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailById(spuId);
        //获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.toMap(spuDetail.getGenericSpec(), Long.class, String.class);
        //获取特有规格参数
        Map<Long, List<String>> specialSpec = JsonUtils
                .nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
                });
        //规格参数,key是规格参数的名，value是规格参数的值
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam param : params) {
            //规格名称
            String key = param.getName();
            Object value;
            //判断是否是通用规格
            if (param.getGeneric()) {
                value = genericSpec.get(param.getId());
                //判断是否是数值类型
                if (param.getNumeric()) {
                    //处理成段
                    value = chooseSegment(value.toString(), param);
                } else {
                    value = specialSpec.get(param.getId());
                }
                //存入map
                specs.put(key, value);
            }
        }
        Goods goods = new Goods();
        goods.setBrandId((spu.getBrandId()));
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spuId);
        //搜索字段很多
        goods.setAll(all);
        //sku价格集合
        goods.setPrice(priceList);
        //所有sku的结合的json格式
        goods.setSkus(JsonUtils.toString(skus));
        //所有的可所有的规格参数
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult searchPage(SearchRequest searchRequest) {

        int page = searchRequest.getPage() -1;
        int size = searchRequest.getSize();
        //创建查询构建起 nativequerybuilder
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //过滤条件
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"} ,null));
        queryBuilder.withPageable(PageRequest.of(page,size));
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()));
        //添加聚合分类品牌情况
        String categoryAggName = "category_agg";
        String brandAggName ="brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        AggregatedPage<Goods> goods = (AggregatedPage)goodRepository.search(queryBuilder.build());
        //取出聚合结果进行组装  解析聚合结果
        Aggregations aggs = goods.getAggregations();
        List<Category> categories = parseCategoryAgg(aggs.get(categoryAggName));
        List<Brand> brands = parseBrandAgg(aggs.get(brandAggName));
        Long total = goods.getTotalElements();
        Long totalPages = (long)goods.getTotalPages();
        List<Goods> content = goods.getContent();
        return new SearchResult(total,totalPages,content,categories,brands,null);
    }

    private List<Brand> parseBrandAgg(LongTerms aggregation) {
        List<Long> collect = aggregation.getBuckets().stream().map(bucket ->
                bucket.getKeyAsNumber().longValue()
        ).collect(Collectors.toList());
        List<Brand> brandList = this.brandClient.queryBrandByIds(collect);
        return brandList;
    }

    private List<Category> parseCategoryAgg(LongTerms aggregation) {
        List<Long> collect = aggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());
        List<Category> categoryList = this.categoryClient.queryCategoryByIds(collect);
        return categoryList;
    }
}
