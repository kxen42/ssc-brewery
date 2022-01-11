package guru.sfg.brewery.web.controllers.api;


import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;


@SpringBootTest
class BeerRestControllerIT extends BaseIT {

    public static final String BAD_ADMIN_PASSWORD = "gXuXrXuX";
    public static final String GOOD_ADMIN_PASSWORD = "guru";
    public static final String ADMIN_USER = "admin";

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        public Beer beerToDelete() {
            Random rand = new Random();

            return beerRepository.saveAndFlush(
            Beer.builder()
                .beerName("Delete Me Beer")
                .beerStyle(BeerStyleEnum.IPA)
                .minOnHand(12)
                .quantityToBrew(200)
                .upc(String.valueOf(rand.nextInt(99999999)))
                .build());
        }

        @Test
        @DisplayName("😱 bad credentials")
        void deleteBeerHttpBasicBadCredentials() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                .with(httpBasic(ADMIN_USER, BAD_ADMIN_PASSWORD)))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} Access denied to '/api/vi/beer/{upc}' with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.api.JUnit5MethodSource#nonAdminRoleUserProvider")
        void deleteBeerHttpBasicNotAdminRole(String username, String password) throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                .with(httpBasic(username, password)))
                .andExpect(status().isForbidden());
        }


        @Test
        @DisplayName("╯°□°）╯ delete no auth")
        void deleteBeerNoAuth() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId()))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerHttpBasicGoodCredentials() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/" + beerToDelete().getId())
                .with(httpBasic(ADMIN_USER, GOOD_ADMIN_PASSWORD)))
                .andExpect(status().is2xxSuccessful());
        }
    }


    @ParameterizedTest(name = "Anonymous cannot access {0}")
    @ValueSource(strings = {"/api/v1/beer/", "/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311", "/api/v1/beerUpc/0631234200036"})
    void findBeersNoAuth(String url) throws Exception {
        mockMvc.perform(get(url)
            .with(anonymous()))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "Role {2} has access to /api/v1/beer/")
    @CsvSource({
        "admin, guru, ADMIN",
        "user, password, USER",
        "scott, tiger, CUSTOMER",
    })
    void findBeers(String username, String password, String role) throws Exception {
        mockMvc.perform(get("/api/v1/beer/")
            .with(httpBasic(username, password)))
            .andExpect(status().isOk());
    }


    @ParameterizedTest(name = "Role {2} has access to '/api/v1/beer/{bearId}'")
    @CsvSource({
        "admin, guru, ADMIN",
        "user, password, USER",
        "scott, tiger, CUSTOMER",
    })
    void findBeerById(String username, String password, String role) throws Exception {
        mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .with(httpBasic(username, password))).andExpect(status().isOk());
    }

    @ParameterizedTest(name = "Role {2} has access to '/api/v1/beerUpc/{upc}'")
    @CsvSource({
        "admin, guru, ADMIN",
        "user, password, USER",
        "scott, tiger, CUSTOMER",
    })
    void findBeerByUpc(String username, String password, String role) throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036")
            .with(httpBasic(username, password)))
            .andExpect(status().isOk());
    }

}