package com.ly.item.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import com.ly.common.vo.PageResult;
import com.ly.item.*;
import com.ly.item.mapper.GoodsMapper;
import com.ly.item.mapper.SkuMapper;
import com.ly.item.mapper.SpuDetailMapper;
import com.ly.item.mapper.StockMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class GoodsService {


    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    SpuDetailMapper spuDetailMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    StockMapper stockMapper;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandService brandService;

    public PageResult querySpuByPageAndSort(Integer page, Integer rows, String key, Boolean saleable) throws Exception{
        //分页
        PageHelper.startPage(page,rows);
        //构造查询参数
        Example example = new Example(Spu.class);

        Example.Criteria criteria = example.createCriteria();
        //是否有搜索条件
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%" + key + "%");
        }
        //是否过滤上下架
        if(saleable != null) {
            criteria.andEqualTo("saleable",saleable);
        }
        //默认排序
        example.setOrderByClause("last_update_time desc");

        //开始查询
        List<Spu> spuList = goodsMapper.selectByExample(example);

        //根据spu中的ID查询分类名称 和规格名称
        loadCategoryAndBrandName(spuList);

        PageInfo<Spu> pageInfo = new PageInfo(spuList);

        return  new PageResult<>(pageInfo.getTotal(),spuList);
    }

    private void loadCategoryAndBrandName(List<Spu> spuList) throws Exception{
        for (Spu spu : spuList){
            //查询商品分类名称
            List<String> categoryNameList = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(categoryNameList,"/"));
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());

        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSpu(Spu spu) throws Exception{
        //修改spu
        spu.setLastUpdateTime(new Date());
        spu.setSaleable(true);
        spu.setValid(true);
        int count = goodsMapper.updateByPrimaryKeySelective(spu);
        if(count != 1 ){
            throw new LyException(LyExceptionEnum.SPU_UPDATE_ERROR);
        }
        //修改spuDetail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPU_UPDATE_ERROR);
        }
        //sku其中包括 新增 修改两个操作  所以这里直接采用删除 新增操作
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        //查询库存
        List<Long> idList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        //删除库存
       count =  skuMapper.delete(sku);
       if(count != idList.size() ){
           throw new LyException(LyExceptionEnum.SPU_UPDATE_ERROR);
       }
        stockMapper.deleteByIdList(idList);
        //新增sku
        //定义库存集合
        List<Stock> stockList = new ArrayList<>();
        for (Sku sku1: spu.getSkus()) {
            sku1.setCreateTime(new Date());
            sku1.setLastUpdateTime(sku1.getCreateTime());
            sku1.setSpuId(spu.getId());
            count = skuMapper.insert(sku1);
            if(count != 1){
                throw new LyException(LyExceptionEnum.SPU_UPDATE_ERROR);
            }
            //新增库存  采用批量更新方式
            Stock stock = new Stock();
            stock.setSkuId(sku1.getId());
            stock.setStock(sku1.getStock());
            stockList.add(stock);
        }
        count =  stockMapper.insertList(stockList);
        if(count != stockList.size()){
            throw new LyException(LyExceptionEnum.SPU_UPDATE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveSpu(Spu spu) throws Exception{
        //新增spu  除了页面参数 设置其他默认值
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setId(null);
        spu.setSaleable(true);
        spu.setValid(true);
        int count = goodsMapper.insert(spu);
        if(count != 1 ){
            throw new LyException(LyExceptionEnum.SPU_SAVE_ERROR);
        }
        //新增spu detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(spuDetail);
        if(count != 1){
            throw new LyException(LyExceptionEnum.SPU_SAVE_ERROR);
        }
        //新增sku
        //定义库存集合
        List<Stock> stockList = new ArrayList<>();
        for (Sku sku: spu.getSkus()) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if(count != 1){
                throw new LyException(LyExceptionEnum.SPU_SAVE_ERROR);
            }
            //新增库存  采用批量更新方式
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockList.add(stock);
        }
           count =  stockMapper.insertList(stockList);
        if(count != stockList.size()){
            throw new LyException(LyExceptionEnum.SPU_SAVE_ERROR);
        }
    }




    public SpuDetail querySpuDetailByPid(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        if(spuDetail == null){
            throw new LyException(LyExceptionEnum.GOODS_DETAIL_NOT_FOUND);
        }
        return spuDetail;
    }

    public List<Sku> querySkuListByPid(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(LyExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        //查询库存
        List<Long> idList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(idList);
        if (CollectionUtils.isEmpty(stockList)){
            throw new LyException(LyExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        //我们把stock变成map，其key是：sku的id，值是库存值
        Map<Long, Long> stockMap = stockList.stream()
                .collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));

        return  skuList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSpu(Long spuId) throws Exception{
        //删除采用从下往上删除的逻辑
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skuList)){
            throw new LyException(LyExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
        List<Long> skuIdList = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        //删除库存表
        int count = stockMapper.deleteByIdList(skuIdList);
        if (count != skuIdList.size()){
            throw new LyException(LyExceptionEnum.GOODS_DELETE_ERROR);
        }
        //删除sku表
        count = skuMapper.delete(sku);
        if(count != skuIdList.size()) {
            throw new LyException(LyExceptionEnum.GOODS_DELETE_ERROR);
        }
        //删除spu表
        Spu spu = new Spu();
        spu.setId(spuId);
        count = goodsMapper.delete(spu);
        if(count != 1){
            throw new LyException(LyExceptionEnum.GOODS_DELETE_ERROR);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFlag(Long spuId) throws Exception{
        Spu spu = new Spu();
        spu.setId(spuId);
        Spu spu1 = goodsMapper.selectByPrimaryKey(spu);
        spu.setSaleable(!spu1.getSaleable());
        spu.setLastUpdateTime(new Date());
        int count = goodsMapper.updateByPrimaryKeySelective(spu);
        if(count != 1){
            throw new LyException(LyExceptionEnum.GOODS_FLAG_UPDATE_ERROR);
        }
    }

    public List<Spu> selectAll() {

        List<Spu> spus = goodsMapper.selectAll();
        return spus;
    }
}
