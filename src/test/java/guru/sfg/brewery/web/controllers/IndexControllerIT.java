package guru.sfg.brewery.web.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;

/**
 * Created by jt on 6/13/20.
 */
@WebMvcTest
public class IndexControllerIT extends BaseIT {

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

    @Test
    void testGetIndexSlash() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk());
    }

    @Test
    void allowsLoginPageeReturned() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }

    @Test
    void allowsWebJarsResourcesReturned() throws Exception {
        mockMvc.perform(get("/webjars/jquery/jquery.min.js"))
            .andExpect(status().isOk());
    }

//    @Test
//    void allowsBreweryCssReturned() throws Exception {
//        mockMvc.perform(get("/resources/css/brewery.css"))
//            .andExpect(status().isOk());
//    }

    @Test
    void allowsBreweryPngReturned() throws Exception {
        mockMvc.perform(get("/resources/images/brewery.png"))
            .andExpect(status().isOk());
    }

}