package com.ly.gateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/gateway/test")
    public String test(){

        return "success";
    }
}
