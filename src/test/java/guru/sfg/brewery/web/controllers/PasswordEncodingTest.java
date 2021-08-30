package guru.sfg.brewery.web.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * Demos
 * <ul>
 *  <li>DigestUtils and MD5</li>
 * <li>NoOpPasswordEncoder</li>
 * <li>LdapShaPasswordEncoder</li>
 * <li>StandardPasswordEncoder</li>
 *</ul>
 */
public class PasswordEncodingTest {

    static final String PASSWORD = "password";

    @Test
    void sha256Example() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        String first = sha256.encode(PASSWORD);
        System.out.printf("XENSON ---- 1st\t%s%n", first);

        String encoded = sha256.encode(PASSWORD);
        System.out.printf("XENSON ---- 2nd\t%s%n", encoded);

        assertTrue(sha256.matches(PASSWORD, encoded));

    }

    @Test
    void ldapShaExample() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        String first = ldap.encode(PASSWORD);
        System.out.println("XENSON ---- ldap sha 1st " + first);
        String second = ldap.encode(PASSWORD);
        System.out.println("XENSON ---- ldap sha 2nd " + second);

        String encodedPwd = ldap.encode(PASSWORD);
        Assertions.assertTrue(ldap.matches(PASSWORD, encodedPwd));

    }

    @Test
    void md5HashingExample() {
        String first = DigestUtils.md5DigestAsHex(PASSWORD.getBytes());
        System.out.println("XENSON ---- MD5 1st not salted " + first);
        String second = DigestUtils.md5DigestAsHex(PASSWORD.getBytes());
        System.out.println("XENSON ---- MD5 2nd not salted " + second);
        assertEquals(first, second);

        String salted = PASSWORD + "ThisIsMySALTVALUE";
        String third = DigestUtils.md5DigestAsHex(salted.getBytes());
        System.out.println("XENSON ---- MD5 salted " + third);
        assertNotEquals(first, third);
    }

    @Test
    void noopExample() {
        // NoOp encoding is plaintext
        String encoded = NoOpPasswordEncoder.getInstance().encode(PASSWORD);
        System.out.println("XENSON ---- NoOp encoded " + encoded);
        assertEquals(PASSWORD, encoded);
    }
}
