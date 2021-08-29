package guru.sfg.brewery.web.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * Demos
 * <ul>
 *  <li>DigestUtils and MD5</li>
 * <li>NoOpPasswordEncoder</li>
 *</ul>
 */
public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void md5HashingExample() {
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

    @Test
    void noopExample() {
        // NoOp encoding is plaintext
        String encoded = NoOpPasswordEncoder.getInstance().encode(PASSWORD);
        assertEquals(PASSWORD, encoded);
    }
}
