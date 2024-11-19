package com.github.springframework.boot.commons.util.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class MessageDigests {

    private static final String MESSAGE_DIGEST_ALGORITHM_MD5 = "MD5";

    public MessageDigests() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static byte[] md5(final String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM_MD5);
            return md5.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
			// should never happen
            return new byte[0];
        }
    }

    public static String md5Hex(final String input) {
        StringBuilder sb = new StringBuilder();
        byte[] digest = md5(input);
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
