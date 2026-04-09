package uz.java.spring_boot_application.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
@Slf4j
public class BaseUtils {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public String encodeToMd5(String data) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(), 0, data.length());
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static String toHex(byte[] data) {
        char[] chars = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            chars[i * 2] = HEX_DIGITS[(data[i] >> 4) & 0xf];
            chars[i * 2 + 1] = HEX_DIGITS[data[i] & 0xf];
        }
        return new String(chars);
    }

    public boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public boolean isEmpty(List<?> items) {
        return items == null || items.isEmpty();
    }

    public boolean isEmpty(Object l) {
        return l == null;
    }
}
