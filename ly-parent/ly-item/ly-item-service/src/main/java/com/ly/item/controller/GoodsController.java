package com.ly.item.controller;

import com.ly.item.Sku;
import com.ly.item.Spu;
import com.ly.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 70719
 */
@RestController
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @GetMapping("/spu/all")
    public ResponseEntity<List<Spu>> queryCount() throws Exception{
        return ResponseEntity.ok(goodsService.selectAll());
    }


    @RequestMapping("/spu/page")
    public ResponseEntity queryByPage(
            @RequestParam(value = "page" ,defaultValue = "1") Integer page ,
            @RequestParam(value = "rows" ,defaultValue = "10") Integer rows,
            @RequestParam(value = "key" ,required = false) String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    ) throws Exception{
        return ResponseEntity.ok(goodsService.querySpuByPageAndSort(page ,rows,key,saleable));
    }

    @PostMapping("/spu")
    public ResponseEntity saveSpu(@RequestBody Spu spu) throws Exception{
        goodsService.saveSpu(spu);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/spu")
    public  ResponseEntity updateSpu(@RequestBody Spu spu) throws Exception{
        goodsService.updateSpu(spu);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity querySpuDetailByPid(@PathVariable Long spuId) throws Exception{

        return ResponseEntity.ok(goodsService.querySpuDetailByPid(spuId));
    }

    @GetMapping("/sku/list")
    public ResponseEntity querySkuListByPid(@RequestParam("id") Long spuId) throws Exception{
        return ResponseEntity.ok(goodsService.querySkuListByPid(spuId));
    }

    @GetMapping("/spu/deleteBysid/{spuId}")
    public ResponseEntity deleteBysid(@PathVariable Long spuId) throws Exception{
        goodsService.deleteSpu(spuId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/spu/updateFlag/{spuId}")
    public ResponseEntity updateFlag(@PathVariable Long spuId) throws Exception{
        goodsService.updateFlag(spuId);
        return ResponseEntity.ok().build();
    }

}
