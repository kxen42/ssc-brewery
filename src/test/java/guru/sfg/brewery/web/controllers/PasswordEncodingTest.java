package guru.sfg.brewery.web.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

/**
 * Demos DigestUtils and MD5
 */
public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void hashingExample() {
        String first = DigestUtils.md5DigestAsHex(PASSWORD.getBytes());
        System.out.println(first);
        String second = DigestUtils.md5DigestAsHex(PASSWORD.getBytes());
        System.out.println(second);
        assertEquals(first, second);

        String salted = PASSWORD + "ThisIsMySALTVALUE";
        String third = DigestUtils.md5DigestAsHex(salted.getBytes());
        System.out.println(third);
        assertNotEquals(first, third);
    }
}
