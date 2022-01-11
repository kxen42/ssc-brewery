package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public abstract class BaseIT {
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

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
