package com.pinet.core.util;

import java.io.UnsupportedEncodingException;

public class CharsetUtils {
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String ISO_8859_1 = "ISO-8859-1";

    public static final String CONTENT_JSON="application/json";

    public static String utf82GBK(String s) throws UnsupportedEncodingException {
        return convert(s, "utf-8", "gbk");
    }


    public static String utf82ISO88591(String s) throws UnsupportedEncodingException {
        return convert(s, "utf-8", "ISO-8859-1");
    }

    public static String iSO885912UTF8(String s) throws UnsupportedEncodingException {
        return convert(s, "ISO-8859-1", "UTF-8");
    }


    public static String gbk2UTF8(String s) throws UnsupportedEncodingException {
        return convert(s, "gbk", "utf-8");
    }

    public static String convert(String s, String srcChartset, String destCharset) throws UnsupportedEncodingException {
        return new String(s.getBytes(srcChartset), destCharset);
    }
}
