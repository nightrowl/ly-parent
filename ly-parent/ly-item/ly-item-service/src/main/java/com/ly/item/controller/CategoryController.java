package com.ly.item.controller;

import com.ly.item.Category;
import com.ly.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")

public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryListByPid(@RequestParam("pid") Long pid) throws Exception{

        return  categoryService.queryListByPid(pid);
    }

    @GetMapping("/list/ids")
    public List<Category> queryCategoryByIds(@RequestParam("ids")List<Long> ids) throws Exception{

        return categoryService.queryCategoryByIds(ids);
    }

    @PostMapping
    public ResponseEntity saveCategory (Category category) throws Exception{
        categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public  ResponseEntity updateCategory(Category category) throws Exception{
        categoryService.updateCategory(category);
        return ResponseEntity.ok().build();
    }
}
