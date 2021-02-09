package com.example.michaelbettis_term_scheduler.utils;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Helper {
    //==============================Date converters===============================================//
    public static Date stringToDate(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        return sdf.parse(string);
    }

    public static long stringToLong(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date date = sdf.parse(string);
        return Objects.requireNonNull(date).getTime();
    }

    public static long shortStringToLong(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        Date date = sdf.parse(string);
        return Objects.requireNonNull(date).getTime();
    }

    public static String formatter(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static String sdf(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date parsedDate = null;
        try {
            parsedDate = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);

        return sdf2.format(Objects.requireNonNull(parsedDate));
    }

    //===================================Java mail helper=========================================//
    public static final String EMAIL = "27NlUuV1qTRpqvUVAs9/VA==";
    public static final String PASSWORD = "0kKnejGHJYM0s2agxwrNpg==";

    public static String decrypt(String key, String encryptedMsg) {

        String messageAfterDecrypt = null;
        try {
            messageAfterDecrypt = AESCrypt.decrypt(key, encryptedMsg);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return messageAfterDecrypt;

    }
}
