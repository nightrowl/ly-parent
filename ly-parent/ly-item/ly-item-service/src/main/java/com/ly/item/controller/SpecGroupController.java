package com.ly.item.controller;


import com.ly.item.SpecGroup;
import com.ly.item.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author 70719
 */
@RestController
@RequestMapping("/spec/group")
public class SpecGroupController {

    @Autowired
    SpecGroupService specGroupService;


    @GetMapping("/{cid}")
    public ResponseEntity findByCId(@PathVariable Long cid) throws Exception{

        return ResponseEntity.ok(specGroupService.findByCId(cid));
    }

    @PostMapping
    public ResponseEntity saveSpecGroup(@RequestBody SpecGroup group) throws Exception{
        specGroupService.saveSpecGroup(group);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateSpecGroup(@RequestBody SpecGroup group) throws Exception{
        specGroupService.updateSpecGroup(group);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSpecGroup (@PathVariable Long id ) throws Exception{
        specGroupService.deleteSpecGroup(id);
        return ResponseEntity.ok().build();
    }
}
