package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
class BreweryControllerIT extends BaseIT {

    @ParameterizedTest(name = "Role {3} has access to {2}")
    @CsvSource({
        "admin, guru, /brewery/breweries, ADMIN",
        "admin, guru, /brewery/breweries/index, ADMIN",
        "admin, guru, /brewery/breweries/index.html, ADMIN",
        "admin, guru, /brewery/breweries.html, ADMIN",
        "scott, tiger, /brewery/breweries, CUSTOMER",
        "scott, tiger, /brewery/breweries/index, CUSTOMER",
        "scott, tiger, /brewery/breweries/index.html, CUSTOMER",
        "scott, tiger, /brewery/breweries.html, CUSTOMER",
    })
    void listBreweries(String username, String password, String url, String role) throws Exception {
        mockMvc.perform(get(url)
            .with(httpBasic(username, password)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(view().name("breweries/index"))
            .andExpect(model().attributeExists("breweries"));
    }

    @ParameterizedTest(name = "403 Forbidden when USER role tries GET {0}")
    @ValueSource(strings = {"/brewery/breweries", "/brewery/breweries/index", "/brewery/breweries/index.html", "/brewery/breweries.html"})
    void listBreweries(String url) throws Exception {
        mockMvc.perform(get(url)
            .with(httpBasic("user", "password")))
            .andExpect(status().isForbidden());
    }


    @ParameterizedTest(name = "401 Unauthorized when USER role tries GET {0}")
    @ValueSource(strings = {"/brewery/breweries", "/brewery/breweries/index", "/brewery/breweries/index.html", "/brewery/breweries.html"})
    @WithAnonymousUser
    void listBreweriesWithAnonymous(String url) throws Exception {
        mockMvc.perform(get(url))
            .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "Role {3} has access to {2}")
    @CsvSource({
        "admin, guru, /brewery/api/v1/breweries, ADMIN",
        "scott, tiger, /brewery/api/v1/breweries, CUSTOMER",
    })
    void getBreweriesJson(String username, String password, String url, String role) throws Exception {
         mockMvc.perform(get(url)
            .with(httpBasic(username, password)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType("application/json"))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getBreweriesJsonUser() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
            .with(httpBasic("user", "password")))
            .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonAnonymous() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
            .with(anonymous()))
            .andExpect(status().isUnauthorized());
    }
}