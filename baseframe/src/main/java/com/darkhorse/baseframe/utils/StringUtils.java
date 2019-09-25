package com.darkhorse.baseframe.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author chenzhenjie
 * @time 2019/9/4 20:21
 **/
public class StringUtils {
    /**
     * 字符串转换成byte字节数组
     *
     * @param str
     * @return
     */
    public static byte[] stringToGbkBytes(String str) {
        byte[] byteArray = null;
        try {
            if (!TextUtils.isEmpty(str)) {
                byte[] data = str.getBytes("GBK");
                byteArray = new byte[data.length + 1];
                System.arraycopy(data, 0, byteArray, 0, data.length);
                byteArray[data.length] = '\0';
            } else {
                byteArray = new byte[1];
                byteArray[0] = '\0';
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public static String stringToMd5(String str) {
        String md5String = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            for (Byte b : bytes) {
                int num = b & 255;
                String hexString = Integer.toHexString(num);
                if (hexString.length() == 1) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(hexString);
            }
            md5String = stringBuilder.toString();
            return md5String;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5String;
    }
}
