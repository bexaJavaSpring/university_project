package uz.java.spring_boot_application.util;

import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;

public class MD5Decode {
    public static String md5Decode(Object source) {
        if (ObjectUtils.isEmpty(source))
            throw new RuntimeException("Object is empty");
        return DigestUtils.md5DigestAsHex(source.toString().getBytes(StandardCharsets.UTF_8));
    }
}
