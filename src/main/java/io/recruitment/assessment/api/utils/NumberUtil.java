package io.recruitment.assessment.api.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class NumberUtil {

    private NumberUtil() {}

    /**
     * @Description Get token value
     * @param timeStr
     * @param userId
     * @return
     */
    public static String getNewToken(String timeStr, Integer userId) {
        String src = timeStr + userId + genRandomNum(4);
        return genToken(src);
    }

    /**
     * @Description Generate token value
     * @param src
     * @return
     */
    private static String genToken(String src) {
        if (null == src || "".equals(src)) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            String result = new BigInteger(1, md.digest()).toString(16);
            if (result.length() == 31) {
                result = result + "-";
            }
            System.out.println("Token: "+result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Description Generate fixed length random number
     * @param length
     * @return
     */
    private static int genRandomNum(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * @Description Generate Order Number
     * @return
     */
    public static String genOrderNo() {
        StringBuffer buffer = new StringBuffer(String.valueOf(System.currentTimeMillis()));
        int num = genRandomNum(4);
        buffer.append(num);
        return buffer.toString();
    }
}
