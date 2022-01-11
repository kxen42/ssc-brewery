package guru.sfg.brewery.web.controllers.api;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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


    @Autowired
    BeerRepository beerRepository;


    // These @MockBeans will not work with @SpringBootTest, you'll get NPE
    // Works with @WebMvcTest
/*
    @MockBean
    protected BeerRepository beerRepository;
    @MockBean
    protected BeerInventoryRepository beerInventoryRepository;
    @MockBean
    protected BreweryService breweryService;
    @MockBean
    protected CustomerRepository customerRepository;
    @MockBean
    protected BeerService beerService;
*/

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
        @MethodSource("guru.sfg.brewery.web.controllers.api.BaseIT#getStreamNotAdmin")
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

    @DisplayName("/api/v1/beer with unauthorized user")
    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
            .andExpect(status().isUnauthorized());
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

    @DisplayName("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311 with unauthorized user")
    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")).andExpect(status().isUnauthorized());
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

    @DisplayName("/api/v1/beerUpc/0631234200036 with unauthorized user")
    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
            .andExpect(status().isUnauthorized());
    }

    @DisplayName("Initial Find Beer Form")
    @Nested
    class FindBeersForm {
        @Test
        void findBeersFormNoAuth() throws Exception {
            mockMvc.perform(get("/beers/find"))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersFormAuth(String username, String password) throws Exception {
            mockMvc.perform(get("/beers/find")
                    .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
        }
    }

    @DisplayName("Process Find Beer Form")
    @Nested
    class ProcessFindForm {

        @Test
        void findBeerFormNoAuth() throws Exception {
            mockMvc.perform(get("/beers").param("beerName", ""))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerFormAuth(String username, String password) throws Exception {
            mockMvc.perform(get("/beers").param("beerName", "")
                    .with(httpBasic(username, password)))
                .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Beer By Id")
    @Nested
    class GetByID {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void getBeerByIdAuth(String username, String password) throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/beers/" + beer.getId())
                    .with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/beerDetails"))
                .andExpect(model().attributeExists("beer"));
        }

        @Test
        void getBeerByIdNoAuth() throws Exception {
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(get("/beers/" + beer.getId()))
                .andExpect(status().isUnauthorized());
        }
    }
}