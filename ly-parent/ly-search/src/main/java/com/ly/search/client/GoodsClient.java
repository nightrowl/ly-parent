package com.ly.search.client;

import com.ly.common.vo.PageResult;
import com.ly.item.Sku;
import com.ly.item.Spu;
import com.ly.item.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface GoodsClient {



    @GetMapping("/spu/all")
    ResponseEntity<List<Spu>> queryCount() throws Exception;
    /**
     * 根据spu的id查询detail详情
     * @param spuId
     * @return
     */
    @RequestMapping("/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long spuId);
    /**
     * 根据spu查询下面的所有sku
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId);

    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) boolean saleable
            );
    /**
     * /根据spu的id查spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

}
