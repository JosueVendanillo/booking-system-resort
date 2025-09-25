package com.bluebell.project.util;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class BookingIdGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHANUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private BookingIdGenerator() {}

    public static String generate() {
        return generate("BKG", LocalDate.now(), 6);
    }

    public static String generate(String prefix, LocalDate date, int randomLen) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append("-")
                .append(date.format(DATE_FMT)).append("-")
                .append(randomAlphaNum(randomLen));
        return sb.toString();
    }

    private static String randomAlphaNum(int len) {
        StringBuilder s = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            s.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return s.toString();
    }
}
