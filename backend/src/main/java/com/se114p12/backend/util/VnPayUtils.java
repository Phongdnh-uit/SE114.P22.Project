package com.se114p12.backend.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class VnPayUtils {

    private static final DateTimeFormatter DATE_FMT  = DateTimeFormatter.ofPattern("yyMMdd");
    private static final DateTimeFormatter TIME_FMT  = DateTimeFormatter.ofPattern("HHmmssSSS");
    private static final Random RANDOM = new Random();

    /** Sinh chuỗi transaction reference duy nhất, tối đa 100 ký tự */
    public static String generateTxnRef(String prefix) {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(DATE_FMT);
        String timePart = now.format(TIME_FMT);

        // 4–6 ký tự hex random
        String randomPart = Integer.toHexString(RANDOM.nextInt(0x100000)).toUpperCase();

        String txnRef = String.format("%s%s-%s-%s", prefix, datePart, timePart, randomPart);
        return txnRef.length() > 100 ? txnRef.substring(0, 100) : txnRef;
    }
}