package guru.sfg.brewery.web.controllers.api;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import guru.sfg.brewery.security.RestAuthFilter;
import guru.sfg.brewery.web.controllers.BaseIT;


@SpringBootTest
class BeerRestControllerIT extends BaseIT {

    public static final String BAD_ADMIN_PASSWORD = "gXuXrXuX";
    public static final String GOOD_ADMIN_PASSWORD = "guru";
    public static final String ADMIN_USER = "admin";

    @Test
    void deleteBeerHttpBasicBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .with(httpBasic(ADMIN_USER, BAD_ADMIN_PASSWORD)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerHttpBasicGoodCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .with(httpBasic(ADMIN_USER, GOOD_ADMIN_PASSWORD)))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/")).andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")).andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
            .andExpect(status().isOk());
    }
}