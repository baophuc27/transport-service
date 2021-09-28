package com.reeco.utils;

import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Utils {
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();

    public static byte[] getBytes(Object t){
        String line = gson.toJson(t);
        return line.getBytes(CHARSET);
    }
}
