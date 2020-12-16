package com.winningticketproject.in.IGOLF;

import android.util.Base64;

import com.winningticketproject.in.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class iGolfAuth {

    private static final String APIVersion = "1.1";
    private static final String SignatureVersion = "2.0";
    private static final String SignatureMethod = "HmacSHA256";
    private static final String ResponseFormat = "JSON";
    private static final String ApplicationAPIKey = BuildConfig.API_KEY;
    private static final String ApplicationSecretKey = BuildConfig.API_SECRET_KEY;
    private static final SimpleDateFormat TimeStampDateFormat = new SimpleDateFormat("yyMMddHHmmssZZZZ");

    public static String getUrlForAction(final String action) {
        final String url1;
        final String url2 = getTimestamp() + "/" + ResponseFormat;
        final String secret;
        secret = ApplicationSecretKey;
        url1 = action + "/" + ApplicationAPIKey + "/" + APIVersion + "/" + SignatureVersion + "/" + SignatureMethod + "/";

//        String url3 = "CourseList/dyzICyI_t_rnXp0/1.1/2.0/HmacSHA256/190104133025GMT+05:30/JSON";

        final String signature = makeSignature(url1 + url2, secret);


        return url1 + signature + "/" + url2;
    }


    private static String getTimestamp() {
        return TimeStampDateFormat.format(new Date());
    }

    private static String makeSignature(final String src, final String secret) {
        final String CHARACTER_ENCODING = "UTF-8";
        String res = null;
        try {
            final Mac mac = Mac.getInstance("HMACSHA256");
            final byte[] sc = secret.getBytes(CHARACTER_ENCODING);
            mac.init(new SecretKeySpec(sc, mac.getAlgorithm()));
            final byte[] bt = mac.doFinal(src.getBytes(CHARACTER_ENCODING));
            res = Base64.encodeToString(bt, Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP);
            res = res.replace('+', '-').replace('/', '_');
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {

        }
        return res;
    }
}

