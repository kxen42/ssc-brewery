package guru.sfg.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Demos custom delegating password encoder.
 */
public class SfgPasswordEncoderFactories {

    // modification of PasswordEncoderFactories.createDelegatingPasswordEncoder()
    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("ldap", new org.springframework.security.crypto.password.LdapShaPasswordEncoder());
        encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    private SfgPasswordEncoderFactories() {
        throw new UnsupportedOperationException("not allowed to construct class");
    }
}
