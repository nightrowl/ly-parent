package com.ly.search.controllor;


import com.ly.search.pojo.SearchRequest;
import com.ly.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("page")
    public ResponseEntity searchPage(@RequestBody SearchRequest searchRequest) throws Exception{
        return ResponseEntity.ok(searchService.searchPage(searchRequest));
    }
}
