package com.toher.common.utils.file;

import com.toher.common.constants.UploadEnum;
import com.toher.common.utils.CommonUtils;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @Author: 同恒科技-李怀明
 * @Date: 2018/12/24 15:44
 */
public class FileuploadUtil {

    /**
     * 默认文件大小 2M
     **/
    private static int default_file_size = 20000000;
    /**
     * 默认文件名生成规则
     * UUID
     * Date
     **/
    private static String generating_rule = "date";

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(String baseDir, MultipartFile file) throws FileSizeLimitExceededException, IOException {
            return upload(baseDir, file, generating_rule);
    }

    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file    上传的文件
     * @param rule    文件名称生成规则
     * @return 文件名称
     * @throws Exception
     */
    public static final String upload(String baseDir, MultipartFile file, String rule) throws FileSizeLimitExceededException , IOException {
            return upload(baseDir, file, rule, null);
    }

    /**
     * 文件上传
     *
     * @param baseDir   相对应用的基目录
     * @param file      上传的文件
     * @param rule      文件名称生成规则
     * @param extension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file, String rule, String extension) throws FileSizeLimitExceededException, IOException {

        String suffix = null;
        //判断文件是否超出上传大小限制
        CheckFileSize(file);
        //获取原文件名
        String fileName = file.getOriginalFilename();
        //判断是jquery插件cropbox 通过 data:image 方式上传
        if(fileName.equals("blob")){
            suffix = "jpg";
        }else {
            //如果没有传递extension文件类型 则取原文件类型
            if (StringUtils.isEmpty(extension)) {
                suffix = StringUtils.substringAfterLast(fileName, ".");
            }else{
                suffix = extension;
            }
        }
        String fileRealName = setFileName(rule, suffix);
        File upLoadFile = getRealFile(baseDir,fileRealName);
        file.transferTo(upLoadFile);
        return fileRealName;
    }

    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public static final void CheckFileSize(MultipartFile file) throws FileSizeLimitExceededException {
        long size = file.getSize();
        if (default_file_size != -1 && size > default_file_size) {
            throw new FileSizeLimitExceededException("not allowed upload upload", size, default_file_size);
        }
    }

    /**
     * 按照规则生成文件名
     * @param rule
     * @return
     */
    public static String setFileName(String rule, String extension) {
        if (rule.equals(UploadEnum.UUID.getTypeName())) {
            return CommonUtils.get32UUID() + "." + extension;
        } else if (rule.equals(UploadEnum.DATE.getTypeName())) {
            String dateStr = CommonUtils.dateToString(new Date(), "yyyyMMddHHmmssSSS") + "." + extension;
            return dateStr;
        }
        return null;
    }

    /**
     * 创建文件及目录
     * @param baseDir  相对应用的基目录
     * @param fileRealName 最终保存的文件名
     */
    public static File getRealFile(String baseDir, String fileRealName) throws IOException {
        File file = new File(baseDir + File.separator + fileRealName);
        //没有目录创建目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //创建文件
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
