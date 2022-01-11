package guru.sfg.brewery.web.controllers.api;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

/**
 * Used in integration test for use in
 * <pre>
 *  @MethodSource("guru.sfg.brewery.web.controllers.api.JUnit5MethodSource#nonAdminRoleUserProvider")
 * </pre>
 */
public class JUnit5MethodSource {

    // Inner class can't have static member in Java 11
    static Stream<Arguments> nonAdminRoleUserProvider() {
        return Stream.of(
            arguments("user", "password"),
            arguments("scott", "tiger")
        );
    }
}
