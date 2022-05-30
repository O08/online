package com.et.be.online.controller;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.et.be.inbox.domain.vo.ResponseVO;
import com.et.be.online.constant.SysConfigConstant;
import com.et.be.online.domain.vo.ImageVO;
import com.et.be.online.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

import java.awt.Image;

@Api(value = "平台基础功能-文件", tags = "基础功能")
@RequestMapping("api/v1/common")
@RestController
@Slf4j
public class CommonFileController {
    @Autowired
    private ProductService productService;
    @ApiOperation("文件上传")
    @ResponseBody
    @RequestMapping(value = "productImageFileUploadBatch",method = RequestMethod.POST)
    public ResponseVO fileUploadBatch(@RequestParam("files") MultipartFile[] files, String productCode,
                                      String images) throws IOException {


        JSONArray imagesJson = JSON.parseArray(images);
        ArrayList<ImageVO> imageVOS = new ArrayList<>();
        for (MultipartFile file : files){
            ImageVO imageVO= uploadMultipartFile(file);
            imageVOS.add(imageVO);
            imagesJson.add(imageVO);
        }
        // 更新商品图片 1 产品编码 2 图片json string
         productService.updateProductImage(productCode,imagesJson.toJSONString());
        return new ResponseVO(imageVOS);
    }
    // 存储单一文件
    private ImageVO uploadMultipartFile(MultipartFile file) throws IOException {
        // 后缀
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 重命名
        String fileName = IdUtil.simpleUUID() +extName;
        // 存储
        FileUtil.mkdir(SysConfigConstant.SERVER_DIR+SysConfigConstant.DOC_DIR);
        String path = SysConfigConstant.SERVER_DIR+SysConfigConstant.DOC_DIR+fileName;
        FileUtil.writeFromStream(file.getInputStream(),path );

        // 压缩图片
        String  scalPath= SysConfigConstant.SERVER_DIR+SysConfigConstant.DOC_DIR+"scale_"+fileName;
        FileUtil.copy(new File(path),new File(scalPath),true);
        Image scaleImage = new ImageIcon(scalPath).getImage();
        ImgUtil.scale(scaleImage,270,380);

        ImageVO imageVO = new ImageVO();
        imageVO.setFileUrl(SysConfigConstant.DOC_DIR+fileName)
                .setScaleFileUrl(SysConfigConstant.DOC_DIR+"scale_"+fileName);

        return imageVO;
    }






}
