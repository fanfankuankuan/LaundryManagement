package com.recycle.laundrymanagement;

import com.google.android.gms.common.util.*;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Util Class stores util static method
 */
public class Utils {
    /**
     * Md5 encryption, encode string
     * @param input the string to be encoded
     * @return encoded string
     */
    public static String md5Encryption (final String input){
        String result = "";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(input.getBytes(Charset.forName("UTF8")));
            byte [] resultByte = messageDigest.digest();
            result = Hex.bytesToStringLowercase(resultByte);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

}
