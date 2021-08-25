package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class BaseIT {
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;
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

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            // be sure to get the one from MVC rather the one in Reactive
            // this activates the SpSec filters
            .apply(springSecurity())
            .build();
    }
}
