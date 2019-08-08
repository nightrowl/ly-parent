package com.ly.upload.service;

import com.ly.common.enums.LyExceptionEnum;
import com.ly.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UploadService {

    private  static final List<String> ALLOW_TYPE = Arrays.asList("image/png","image/jpeg","image/bmp");

    public String uploadImage(MultipartFile file) {
        try {
            //校验文件
            String contentType = file.getContentType();
            if (!ALLOW_TYPE.contains(contentType)) {
                throw new LyException(LyExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(LyExceptionEnum.INVALID_FILE_TYPE);
            }
            //准备目标路径
            File dest = new File("C:/tools/upload/images",
                    file.getOriginalFilename());
            //保存文件到本地
            file.transferTo(dest);
        }catch (IOException e) {
                //上传失败
                log.error("上传文件失败!", e);
                throw  new LyException(LyExceptionEnum.UPLOAD_FILE_ERROR);
            }
            return "http://localhost:8082/images/" + file.getOriginalFilename();
    }
}
