package com.ly.search.client;

import com.ly.item.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface CategoryClient {


    @GetMapping("category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids")List<Long> ids);


}
