package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class BaseIT {
    public static final String BAD_ADMIN_PASSWORD = "gXuXrXuX";
    public static final String GOOD_ADMIN_PASSWORD = "guru";
    public static final String ADMIN_USER = "admin";

    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            // be sure to get the one from MVC rather the one in Reactive
            // this activates the SpSec filters
            .apply(springSecurity())
            .build();
    }

    public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of(ADMIN_USER, GOOD_ADMIN_PASSWORD),
            Arguments.of("scott", "tiger"),
            Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of("scott", "tiger"),
            Arguments.of("user", "password"));
    }
}
