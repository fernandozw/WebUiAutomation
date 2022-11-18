package com.thinkingdata.lib;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class MD5Utils {
    
//     使用md5的算法进行加密
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

   /* //测试
    public static void main(String[] args) {
        System.out.println("MD5Utils:"+md5("866399036018264_3599c5ce0704"+"wj@34324a%6232"));
        System.out.println("MD5:"+MD5.md5("866399036018264_3599c5ce0704"+"wj@34324a%6232"));
    }*/

}
