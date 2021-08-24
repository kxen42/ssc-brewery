package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

@WebMvcTest
class BeerControllerIT extends BaseIT {

    @Test
    void findBeers() throws Exception {
        // confirm that security is PermitsAll
        mockMvc.perform(get("/beers/find"))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/findBeers"))
            .andExpect(model().attributeExists("beer"));
    }

// TODO: complete this experiment
//    @Test
//    void findBeersWithParameters() throws Exception {
//        // confirm that security is PermitsAll
//        mockMvc.perform(get("/beers").queryParam("beerName", ""))
//            .andExpect(status().isOk());
//    }

// TODO: complete this experiment
//    @Test
//    void findBeersWithParametersAlternate() throws Exception {
//        // confirm that security is PermitsAll
//        // see https://www.baeldung.com/integration-testing-in-spring
//        mockMvc.perform(get("/beers?beerName={value}", "Galaxy"))
//            .andExpect(status().isOk());
//    }

    @Test
    void findBeersAnonymousUser() throws Exception {
        // verify it gets through AnonymousAuthenticationFilter
        // aka the anonymous "authentication" facility
        mockMvc.perform(get("/beers/find").with(anonymous()))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/findBeers"))
            .andExpect(model().attributeExists("beer"));
    }

    @Test
    @WithAnonymousUser
    void findBeersAnonymousUserUsingAnnotation() throws Exception {
        // verify it gets through AnonymousAuthenticationFilter
        // aka the anonymouse "authentication" facility
        mockMvc.perform(get("/beers/find"))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/findBeers"))
            .andExpect(model().attributeExists("beer"));
    }
}
