package guru.sfg.brewery.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A UserDetailsService that uses our User and Authority entities.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("xxxxxxxxxxxxxxxxxxxxxxxxx Using JPA to find username [{}]", username);

        // Spring Data JPA runs this method in a transaction
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User name [%s] not found.", username)));
        // at this point the transaction is closed and the Hibernate session is gone
        // which is a problem because user.getAuthorities needs a Hibernate session
        // because the default behavior is a lazy load so the authorities haven't been loaded.
        // Solution:
        // Option 1. Put @Transactional on this loadUserByUsername
        // Option 2. Use an eager fetch
        // Watch the performance to see which is quicker

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getEnabled(),
            user.getAccountNonExpired(),
            user.getCredentialsNonExpired(),
            user.getAccountNonLocked(),
            convertToSpringAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> convertToSpringAuthorities(Set<Authority> authorities) {
        if (!CollectionUtils.isEmpty(authorities)) {
           return authorities.stream()
                .map(Authority::getRole)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        } else {
            return Collections.emptySet();
        }
    }
}
