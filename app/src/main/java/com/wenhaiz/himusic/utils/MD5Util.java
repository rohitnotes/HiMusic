package com.wenhaiz.himusic.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    public static String hexDigest(String message){
        StringBuilder hexString = new StringBuilder();

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(message.getBytes(Charset.forName("UTF-8")));
            byte[] digest = md5.digest();
            for (int i = 0; i < digest.length; i++) {
                if ((0xff & digest[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & digest[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & digest[i]));
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }
}
