package com.owinfo.whc.util;

/**
 * 文件名：.java
 * 版权： 北京联众信安成都分公司
 * 描述： 文件与Base64码相互转换
 * 创建时间：2018年06月20日
 * <p>
 * 〈一句话功能简述〉文件与Base64码相互转换〈功能详细描述〉
 *
 * @author sun
 * @version [版本号, 2018年06月20日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class File2Base64 {
    /**
     * 将文件转成base64 字符串
     * @param  path文件路径
     * @return  *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void decoderBase64File(String base64Code, String targetPath)
            throws Exception {
        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();

    }

    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void toFile(String base64Code, String targetPath)
            throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    public static void main(String[] args) {
        try {
            String base64Code = encodeBase64File("D:/统计分析.doc");
            System.out.println(base64Code);
            decoderBase64File(base64Code, "D:/decoder.doc");
            toFile(base64Code, "D://file.txt");
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
