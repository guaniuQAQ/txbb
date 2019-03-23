package com.wrf.txbb_res_8001.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.wrf.txbb_web_api.feign.UserFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.GlobalConstant;
import util.QRCodeUtil;
import util.ResultData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/res")
@Slf4j
public class ResController {

    @Autowired
    private FastFileStorageClient client;

    @Autowired
    private UserFeign userFeign;

    @Value("${fdfs.server.ip}")
    private String serverIp;

    /**
     * 上传头像
     *
     * @param file
     * @param uid
     * @return
     */
    @RequestMapping("/updHead")
    public ResultData updHead(MultipartFile file, Integer uid) {
        log.info("file==>{} uid:{}", file, uid);
        log.info("文件名:{}", file.getOriginalFilename());
        //上传到fdfs
        try {
            StorePath storePath = client.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), "PNG", null);
            //大图
            String fullPath = storePath.getFullPath();
            //缩略图
            String fullPathCrt = fullPath.replace(".", "_80x80.");

            //调用txbb-user修改头像信息
            Map<String, String> headMap = new HashMap<>();
            headMap.put("headUrl", fullPath);
            headMap.put("headUrlSmall", fullPathCrt);
            headMap.put("id", uid.toString());
            ResultData resultData = userFeign.updHead(headMap);

            if (resultData != null || resultData.getCode().equals(GlobalConstant.SUCC_CODE)) {
                headMap.put("headUrl", serverIp + fullPath);
                headMap.put("headUrlSmall", serverIp + fullPathCrt);
                resultData.setData(headMap);
                return resultData;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, "头像修改失败");
    }

    /**
     * 创建并上传二维码
     *
     * @param username
     * @return
     */
    @RequestMapping("/createQRCode")
    public String createQRCode(String username) {
        File file = null;
        try {
            file = File.createTempFile(username + "QRCode", ".PNG");
            QRCodeUtil.createQRCode(file, GlobalConstant.QR_CODE_PRE + username);
            //上传到fdfs
            String path = client.uploadFile(new FileInputStream(file), file.length(), "PNG", null).getFullPath();
            if (path != null) {
                return path;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                file.delete();
            }
        }
        return null;
    }

    @RequestMapping("/voiceMsg")
    public ResultData voiceMsg(MultipartFile file) {
        log.info("file==>{}", file);
        log.info("文件名:{}", file.getOriginalFilename());
        //上传到fdfs
        try {
            StorePath storePath = client.uploadFile(file.getInputStream(), file.getSize(), "amr", null);
            //返回路径
            String fullPath = storePath.getFullPath();
            return ResultData.succResultData(serverIp + fullPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultData.errorResultData(GlobalConstant.ERROR_CODE, "语音上传失败");
    }
}
