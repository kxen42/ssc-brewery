package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

@SpringBootTest
public class BeerControllerIT extends BaseIT {

    @Test
    void initCreationFormWithAdmin() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("admin", "guru")))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/createBeer"))
            .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormWithUser() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("user", "password")))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/createBeer"))
            .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormWithScott() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("scott", "tiger")))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/createBeer"))
            .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeers() throws Exception {
        // confirm that security is PermitsAll
        mockMvc.perform(get("/beers/find"))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/findBeers"))
            .andExpect(model().attributeExists("beer"));
    }

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
    @WithAnonymousUser /* using the annotation rather than anonymous */
    void findBeersAnonymousUserUsingAnnotation() throws Exception {
        // verify it gets through AnonymousAuthenticationFilter
        // aka the anonymous "authentication" facility
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
}
