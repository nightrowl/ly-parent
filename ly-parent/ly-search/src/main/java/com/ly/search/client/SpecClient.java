package com.ly.search.client;

import com.ly.item.SpecParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("item-service")
public interface SpecClient  {

    @GetMapping("spec/param")
    ResponseEntity<List<SpecParam>> findSpecParams(@RequestParam(value = "gid", required = false) Long gid,
                                                   @RequestParam(value = "cid", required = false) Long cid,
                                                   @RequestParam(value = "searching", required = false) Boolean searching
    ) throws Exception;
}
