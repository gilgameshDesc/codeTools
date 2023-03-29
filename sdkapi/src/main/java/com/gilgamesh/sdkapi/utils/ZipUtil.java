package com.gilgamesh.sdkapi.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gilgamesh
 * @date 2022/9/15
 */
public class ZipUtil {
    /**
     * @param srcFiles 要压缩的文件绝对路径列表（支持多个文件的合并压缩）
     * @param destFile 要压缩的zip文件名
     * @param passwd   压缩密码
     * @param fileSize 分卷大小
     * @return 压缩文件路径（如分卷会返回以 "," 分隔的文件路径列表）
     */
    public static String zipBySplit(List<String> srcFiles, String destFile, String passwd, long fileSize) throws ZipException {

        String zipFiles = null;
        File tmpFile = new File(destFile);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        //创建压缩文件对象
        ZipFile zipFile = new ZipFile(destFile);
        //创建文档对象集合
        ArrayList<File> filesToAdd = new ArrayList<File>();
        //判断源压缩文件列表是否为空
        if (null != srcFiles && srcFiles.size() > 0) {
            int fileCount = srcFiles.size();
            for (int i = 0; i < fileCount; i++) {
                filesToAdd.add(new File(srcFiles.get(i)));
            }

            //设置压缩参数
            ZipParameters parameters = new ZipParameters();
            //设置压缩密码
            if (!StringUtils.isBlank(passwd)) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
//                parameters.setPassword(passwd.toCharArray());
            }
            //设置压缩方式-默认
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            //设置压缩级别-一般
            parameters.setCompressionLevel(CompressionLevel.NORMAL);
            //设置默认分割大小为64KB
            //SplitLenth has to be greater than 65536 bytes
            if (fileSize == 0) {
                fileSize = 10485760;
            }
            //创建分卷压缩文件
            zipFile.createSplitZipFile(filesToAdd, parameters, true, fileSize);

            //获取分卷下载列表
            List<File> zipList = zipFile.getSplitZipFiles();
            System.out.println(zipList.size());
//            if (null != zipList && zipList.size() > 0) {
//                String surFix = ".z010";
//                String surFixReplace = ".z10";
//                //单独处理第10个包的文件名做特殊处理
////                for (int i = 0; i < zipList.size(); i++) {
////                    String file = zipList.get(i).trim();
////                    int length = file.length();
////                    String surFixTmp = file.substring(length - 5, length);
////                    if (surFix.equals(surFixTmp)) {
////                        file = file.replace(surFix, surFixReplace);
////                    }
////                    zipList.set(i, file);
////                }
//
//                //初始化压缩数组
//                String[] zipArray = new String[zipList.size()];
//                zipList.toArray(zipArray);
//                zipFiles = Arrays.toString(zipArray);
//                int length = zipFiles.length();
//                zipFiles = zipFiles.substring(1, length - 1);
//            }

        }
        return zipFiles;
    }

    public static void main(String[] args) throws ZipException {
        List<String> filesToAdd = Arrays.asList(
                "D:\\temp\\tools\\tools\\sdkapi\\target\\sdkapi-0.0.1-SNAPSHOT.jar"
        );
        String zipFiles = zipBySplit(filesToAdd, "D:\\temp\\splitzip.zip", "", 0);
        System.out.println(zipFiles);
    }
}
