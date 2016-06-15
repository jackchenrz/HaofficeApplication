package com.publish.haoffice.api.utils;

public class BooleanAndintUtils {

    /**
     * Boolean类型转换int型
     * @param b
     * @return
     */
    public static int Boolean2int(Boolean b) {
        int i = 0;
        if (b) {
            i = 1;
        }
        return i;
    }

    /**
     * int型转换Boolean型
     * @param i
     * @return
     */
    public static boolean int2Boolean(int i) {
        boolean b = false;
        if (i == 1) {
            b = true;
        }
        return b;
    }
}

