package com.ly.item.controller;

import com.ly.item.SpecParam;
import com.ly.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 70719
 */
@RestController
@RequestMapping("/spec/param")
public class SpecParamController {

    @Autowired
    SpecParamService specParamService;

    @GetMapping
    public ResponseEntity<List<SpecParam>> findSpecParams(@RequestParam(value = "gid", required = false) Long gid,
                                                          @RequestParam(value = "cid", required = false) Long cid,
                                                          @RequestParam(value = "searching", required = false) Boolean searching
    ) throws Exception {
        return ResponseEntity.ok(specParamService.findSpecParams(gid, cid, searching));
    }

    @PostMapping
    public ResponseEntity saveSpecParams(@RequestBody SpecParam specParam) throws Exception {
        specParamService.saveSpecParams(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity updateSpecParams(@RequestBody SpecParam specParam) throws Exception {
        specParamService.updateSpecParams(specParam);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSpecParams(@PathVariable Long id) throws Exception {
        specParamService.deleteSpecParams(id);
        return ResponseEntity.ok().build();
    }
}
