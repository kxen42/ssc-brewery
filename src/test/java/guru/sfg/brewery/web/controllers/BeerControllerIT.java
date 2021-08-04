package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest
class BeerControllerIT extends BaseIT {

    @WithMockUser("someuser") // forces mock login credentials in the wac
    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/beers/find"))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/findBeers"))
            .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeersWithHttpBasic() throws Exception {
        mockMvc.perform(get("/beers/find").with(httpBasic("fred", "bob")))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/findBeers"))
            .andExpect(model().attributeExists("beer"));
    }
}
