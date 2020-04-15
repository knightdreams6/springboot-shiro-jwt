package com.learn.project.common.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;

/**
 * @author lixiao
 * @date 2019/8/12 9:37
 */
public class FileUtils {

    /**
     * 获取文件后缀
     * @param fileName 文件名称
     * @return 返回文件后缀
     */
    private static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return  返回新的文件名
     */
    public static String getFileName(String fileOriginName){
        return CommonsUtils.uuid() + getSuffix(fileOriginName);
    }


    /**
     *
     * @param file 文件
     * @param path 文件存放路径
     * @return 图片名称
     */
    public static String upload(MultipartFile file, String path){
        // 生成新的文件名
        String imgName = FileUtils.getFileName(file.getOriginalFilename());
        String realPath = path + "/" + imgName;
        File dest = new File(realPath);
        // 判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            //保存文件
            file.transferTo(dest);
            return imgName;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * 得到图片字节流 数组大小
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 将文件转换成Byte数组
     *
     * @param file
     * @return
     */
    public static byte[] getBytesByFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * MultipartFile转File
     *
     * @param param
     * @return
     */
    public static File transfer(MultipartFile param) {
        if (!param.isEmpty()) {
            File file = null;
            try {
                InputStream in = param.getInputStream();
                file = new File(Objects.requireNonNull(param.getOriginalFilename()));
                inputStreamToFile(in, file);
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                return file;
            }
        }
        return null;
    }

    /**
     * 获取指定文件的输入流
     *
     * @param path 文件的路径
     * @return
     */
    public static InputStream getResourceAsStream(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        try {
            return classPathResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将InputStream写入到File中
     *
     * @param ins
     * @param file
     * @throws IOException
     */
    public static void inputStreamToFile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }


    /**
     * 如果文件存在，删除文件
     * @param path
     */
    public static void deletedFile(String path){
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }
}
