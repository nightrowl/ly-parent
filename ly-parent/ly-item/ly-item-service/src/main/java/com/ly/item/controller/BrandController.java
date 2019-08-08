package com.ly.item.controller;

import com.ly.common.vo.PageResult;
import com.ly.item.Brand;
import com.ly.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author 70719
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    /**
     * 分页查询
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     * @throws Exception
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", required = false) Boolean desc
    ) throws Exception {
        return ResponseEntity.ok(brandService.queryByPage(key, page, rows, sortBy, desc));
    }


    /**
     * 新增
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity saveBrand(Brand brand , @RequestParam("cids") List<Long> cids) throws Exception{
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("bid/{id}")
    public ResponseEntity<Brand> queryById (@PathVariable Long id) throws Exception{
        return ResponseEntity.ok(brandService.queryById(id));
    }

    @PutMapping
    public ResponseEntity updateBrand(Brand brand ,@RequestParam("cids") List<Long> cids) throws Exception{
        brandService.updateBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("deleteByBid/{id}")
    public ResponseEntity deleteBrand(@PathVariable Long id) throws Exception{
        brandService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("cid/{cid}")
    public ResponseEntity queryByCId(@PathVariable Integer cid) throws Exception{
        return ResponseEntity.ok(brandService.queryByCId(cid));
    }


    @GetMapping("/{id}")
    public  Brand queryBrandById(@PathVariable("id") Long id) throws Exception{
        return brandService.queryBrandById(id);
    }

    @GetMapping("/list")
    public List<Brand> queryBrandByIds(@RequestParam("ids")List<Long> ids){
        return brandService.queryBrandByIds(ids);
    }

}
