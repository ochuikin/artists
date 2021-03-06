package com.obabichev.artists.utils;

import java.util.List;

/**
 * Created by olegchuikin on 14/04/16.
 */
public class StringUtils {

    public static String join(String delimiter, List<String> strs){
        if (strs.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str + delimiter);
        }
        return sb.substring(0, sb.length() - delimiter.length());
    }

}
