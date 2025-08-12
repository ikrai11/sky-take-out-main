package com.sky.controller.admin;

import java.io.IOException;
import java.util.UUID;

import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传图片
     * 
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("上传图片：{}", file);
        String originalFilename = file.getOriginalFilename();
        String suffix = ".jpg";
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + suffix;
//        String imgUrl = "http://localhost/media/" + fileName;
        String imgUrl = aliOssUtil.upload(file.getBytes(), fileName);
        try {
            file.transferTo(new java.io.File("D:\\nginx-1.20.2\\media\\" + fileName));
            return Result.success(imgUrl);
        } catch (IllegalStateException | IOException e) {
            log.error("上传图片失败：{}", e);
            return Result.error("上传图片失败");
        }
    }
}
