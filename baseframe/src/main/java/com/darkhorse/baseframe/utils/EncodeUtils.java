package com.darkhorse.baseframe.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class EncodeUtils {
    public static String gbk2utf8(String s){
        try {
            return new String(s.getBytes("GBK"), StandardCharsets.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String utf82gbk(String s){
        try {
            return new String(s.getBytes("UTF-8"), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
