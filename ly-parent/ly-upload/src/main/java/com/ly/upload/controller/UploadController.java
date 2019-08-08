package com.ly.upload.controller;

import com.ly.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author 70719
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws Exception{
        String url = uploadService.uploadImage(file);
        return ResponseEntity.ok(url);
    }
}
