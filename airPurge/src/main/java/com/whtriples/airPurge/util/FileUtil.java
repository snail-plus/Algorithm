package com.whtriples.airPurge.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.FileCopyUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class FileUtil {

    public static HashMap<String, String> saveFile(byte[] b, String fileName,String basePath) {
    	HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String ymd = sdf.format(new Date());
        String extPath = File.separator + ymd + File.separator;
        basePath = basePath + extPath;
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String fileExt = null;
        String srcPath = "";
        fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        // 图片重命名
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
        File uploadFile = new File(basePath + newFileName);
        srcPath = extPath + newFileName;
        srcPath = StringUtils.replace(srcPath, "\\", "/");

        System.out.println("-----------------------------------basePath:" + basePath);

        try {
            byte[] imgBytes = b;
            boolean isTiff = StringUtils.equalsIgnoreCase(fileExt, "tif") || StringUtils.equalsIgnoreCase(fileExt, "tiff");
            imgBytes = ImageConverter.convertImage(new ByteArrayInputStream(b), isTiff).toByteArray();
            FileCopyUtils.copy(imgBytes, uploadFile);
            map.put("url", srcPath);
           // map.put("previewUrl", ConfigUtil.getConfig("nginx.img.server") + srcPath);
            map.put("previewUrl", srcPath);
            System.out.println("----------url---------" + srcPath);
            map.put("error", "0");
        } catch (Exception e) {
            map.put("error", "1");
            map.put("message", "发生异常");
            e.printStackTrace();
        }
        return map;
    }
}
