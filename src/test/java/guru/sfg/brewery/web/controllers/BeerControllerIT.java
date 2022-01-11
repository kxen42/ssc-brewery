package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;

@SpringBootTest
public class BeerControllerIT extends BaseIT {

    @ParameterizedTest(name="{0} can create new beer at /beers/new")
    @CsvSource({
        "admin, guru",
        "user, password",
        "scott, tiger"
    })
    void initCreationFormWith(String username, String password) throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic(username, password)))
            .andExpect(status().isOk())
            .andExpect(view().name("beers/createBeer"))
            .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormWithAnonymouse() throws Exception {
        mockMvc.perform(get("/beers/new").with(anonymous()))
            .andExpect(status().isUnauthorized());
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
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithAnonymousUser /* using the annotation rather than anonymous */
    void findBeersAnonymousUserUsingAnnotation() throws Exception {
        // verify it gets through AnonymousAuthenticationFilter
        // aka the anonymous "authentication" facility
        mockMvc.perform(get("/beers/find"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeerFormADMIN() throws Exception {
        mockMvc.perform(get("/beers").param("beerName", "")
            .with(httpBasic("admin", "guru")))
            .andExpect(status().isOk());
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
