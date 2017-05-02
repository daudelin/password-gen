package com.pg_test.crypto;

import java.nio.charset.StandardCharsets;

public class HashUtil {
    private static String lowers    = "abcdefghijklmnopqrstuvwxyz";
    private static String uppers    = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String digits    = "1234567890";
    private static String commonSym = "!@#$%^&*()-_+=";

    private static void insertChars(byte[] password,
                                    byte[] hashBytes,
                                    int n,
                                    int plen,
                                    int count,
                                    byte[] charset)
    {
        for (int i = 0; i < count; i++) { // adds 1 new char to password per iteration
            int emptyCount = plen - n;
            int x = hashBytes[n * 2] + hashBytes[n * 2 + 1] * 256;
            int o = 0;
            if (emptyCount > 0) {
                o = x % emptyCount;
                x = x / emptyCount;
            }
            int t = 0, replaceIndex = 0;
            for (int j = 0; j < plen; j++) { // determines the next empty index to add a new char to
                if (password[j] == 0) {
                    if (t == o) {
                        replaceIndex = j;
                        break;
                    }
                    t++;
                }
            }
            password[replaceIndex] = charset[x % charset.length];
            n++;
        }
    }

    public static String genPassword(String hash,
                                     int plen,
                                     int minLowers,
                                     int minUppers,
                                     int minDigits,
                                     int minSymbols)
    {
        if (hash == null || hash.length() == 0) {
            throw new IllegalArgumentException("hash is null or empty");
        }
        if (plen < (minLowers + minUppers + minDigits + minSymbols)) {
            throw new IllegalArgumentException("Impossible character constraints");
        }
        String[] hashParts = hash.split("\\$");
        if (hashParts.length != 5 || !hashParts[1].equals("s0")) {
            throw new IllegalArgumentException("Unknown hash format");
        }

        byte[] hashBytes = hashParts[4].getBytes(StandardCharsets.UTF_8);
        byte[] password = new byte[plen];
        for (int i = 0; i < plen; i++) {
            password[i] = 0;
        }

        int n = 0;
        insertChars(password, hashBytes, n, plen, minLowers, lowers.getBytes(StandardCharsets.UTF_8)); n += minLowers;
        insertChars(password, hashBytes, n, plen, minUppers, uppers.getBytes(StandardCharsets.UTF_8)); n += minUppers;
        insertChars(password, hashBytes, n, plen, minDigits, digits.getBytes(StandardCharsets.UTF_8)); n += minDigits;
        insertChars(password, hashBytes, n, plen, minSymbols, commonSym.getBytes(StandardCharsets.UTF_8)); n += minSymbols;

        if (n < plen) {
            String anys = "";
            if (minLowers > 0) { anys += lowers; }
            if (minUppers > 0) { anys += uppers; }
            if (minDigits > 0) { anys += digits; }
            if (minSymbols > 0) { anys += commonSym; }
            insertChars(password, hashBytes, n, plen, plen - n, anys.getBytes(StandardCharsets.UTF_8));
        }

        return new String(password, StandardCharsets.UTF_8);
    }
}
