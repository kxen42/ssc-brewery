package guru.sfg.brewery.domain.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;


/**
 * Test some lombok features I haven't used before: @Builder.Default
 */
class UserTest {

    @Test
    void builderSetsDefaultValues() {
        User user = guru.sfg.brewery.domain.security.User.builder().build();
        assertTrue(user.getAccountNonExpired());
        assertTrue(user.getAccountNonLocked());
        assertTrue(user.getAuthorities().isEmpty());
        assertTrue(user.getCredentialsNonExpired());
        assertTrue(user.getEnabled());
        assertNull(user.getId());
        assertNull(user.getPassword());
        assertNull(user.getUsername());
    }

    @Test
    void noArgsContructorSetsDefaultValues() {
        User user = new guru.sfg.brewery.domain.security.User();
        assertTrue(user.getAccountNonExpired());
        assertTrue(user.getAccountNonLocked());
        assertNull(user.getAuthorities());
        assertTrue(user.getCredentialsNonExpired());
        assertTrue(user.getEnabled());
        assertNull(user.getId());
        assertNull(user.getPassword());
        assertNull(user.getUsername());
    }


    @Test
    void demoLombokSingular() {
        Set<Authority> authoritySet = generatAauthoritySet();

        User user1 = new User.UserBuilder()
            .authorities(authoritySet)
            .build();

        Authority[] authorityArray = authoritySet.toArray(new Authority[0]);
        User user2 = new User.UserBuilder()
            .authority(authorityArray[0])
            .authority(authorityArray[1])
            .build();


        assertEquals(2, user1.getAuthorities().size());
        assertEquals(2, user2.getAuthorities().size());
        assertIterableEquals(user1.getAuthorities(), user2.getAuthorities());
    }


    private Set<Authority> generatAauthoritySet() {
        Authority authority1 = new Authority();
        authority1.setRole("ADMIN");
        Authority authority2 = new Authority();
        authority2.setRole("GRAND_PUH_BAH");
        return Set.of(authority1, authority2);
    }
}
