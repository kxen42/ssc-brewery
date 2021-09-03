package guru.sfg.brewery.web.controllers.api;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import guru.sfg.brewery.security.RestAuthFilter;
import guru.sfg.brewery.web.controllers.BaseIT;


@WebMvcTest
class BeerRestControllerIT extends BaseIT {

    public static final String BAD_ADMIN_PASSWORD = "gXuXrXuX";
    public static final String GOOD_ADMIN_PASSWORD = "guru";
    public static final String ADMIN_USER = "admin";

    @Test
    void deleteBeerBadHeaderCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .header(RestAuthFilter.API_KEY, ADMIN_USER).header(RestAuthFilter.API_SECRET, BAD_ADMIN_PASSWORD))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerGoodHeaderCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .header(RestAuthFilter.API_KEY, ADMIN_USER).header(RestAuthFilter.API_SECRET, GOOD_ADMIN_PASSWORD))
            .andExpect(status().isOk());
    }

    @Test
    void deleteBeerGoodParameterCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .param(RestAuthFilter.API_KEY, ADMIN_USER).param(RestAuthFilter.API_SECRET, GOOD_ADMIN_PASSWORD))
            .andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadParameterCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .param(RestAuthFilter.API_KEY, ADMIN_USER).param(RestAuthFilter.API_SECRET, BAD_ADMIN_PASSWORD))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerGoodHeaderBadParameterCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .header(RestAuthFilter.API_KEY, ADMIN_USER).header(RestAuthFilter.API_SECRET, GOOD_ADMIN_PASSWORD)
            .param(RestAuthFilter.API_KEY, ADMIN_USER).param(RestAuthFilter.API_SECRET, BAD_ADMIN_PASSWORD))
            .andExpect(status().isOk());
    }

    @Test
    void deleteBeerBadHeaderGoodParameterCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .header(RestAuthFilter.API_KEY, ADMIN_USER).header(RestAuthFilter.API_SECRET, BAD_ADMIN_PASSWORD)
            .param(RestAuthFilter.API_KEY, ADMIN_USER).param(RestAuthFilter.API_SECRET, GOOD_ADMIN_PASSWORD))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerOneHeaderOneParameterCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
            .header(RestAuthFilter.API_SECRET, GOOD_ADMIN_PASSWORD)
            .param(RestAuthFilter.API_KEY, ADMIN_USER))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
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