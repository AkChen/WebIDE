package com.akchen.WebIDE.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class IDEStringHelper {
    public static String doEncode(String s){
        try {
            s = s.replace("+","%2B");
            return URLEncoder.encode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doDecode(String s){
        try {
            s = URLDecoder.decode(URLDecoder.decode(s,"utf-8"),"utf-8");
            s = s.replace("%2B","+");
            return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
