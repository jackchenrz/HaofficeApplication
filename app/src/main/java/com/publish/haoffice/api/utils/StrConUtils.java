package com.publish.haoffice.api.utils;

import com.msystemlib.http.JsonToBean;

import java.util.List;

/**
 * Created by ACER on 2016/7/15.
 */
public class StrConUtils {

    public static boolean StrCon(String str,String str1) {

        boolean b = false;
        if (str.indexOf(str1) != -1) {
            b = true;
        }

        return b;
    }
}
