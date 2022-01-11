package guru.sfg.brewery.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Demos
 * <ul>
 *     <li>{@code CommandLineRunner}</li>
 *      <li>{@code @RequiredArgsConstructor}</li>
 *      <li>{@code JpaRepository count()}</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // You could check the number of users, but the relationships
        // and data creation are such that users can't be created without the authorities
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }

    private void loadSecurityData() {
        Authority admin = authorityRepository.save(
            Authority.builder()
                .role("ROLE_ADMIN")
                .build()
        );
        Authority user = authorityRepository.save(
            Authority.builder()
                .role("ROLE_USER")
                .build()
        );
        Authority customer = authorityRepository.save(
            Authority.builder()
                .role("ROLE_CUSTOMER")
                .build()
        );

        userRepository.save(
        User.builder()
            .username("admin")
            .password(passwordEncoder.encode("guru"))
            .authority(admin)
            .build());

        userRepository.save(
            User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .authority(user)
                .build());

        userRepository.save(
            User.builder()
                .username("scott")
                .password(passwordEncoder.encode("tiger"))
                .authority(customer)
                .build());

        log.info("xxxxxxxxxxxxxxxxxxxxxxxxx Users Loaded: {}", userRepository.count());
    }
}
