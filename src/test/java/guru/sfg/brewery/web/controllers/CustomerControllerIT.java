package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerControllerIT extends BaseIT {

    @ParameterizedTest(name="Access to /customers/find allowed for role {2}")
    @CsvSource({
        "admin, guru, ADMIN",
        "scott, tiger, CUSTOMER"
    })
    void findCustomers(String username, String password, String role) throws Exception {
        mockMvc.perform(get("/customers/find")
            .with(httpBasic(username, password)))
            .andExpect(status().isOk());
    }


    @Test
    @DisplayName("Access denied to /customers/find for role USER")
    void findCustomers() throws Exception {
        mockMvc.perform(get("/customers/find")
            .with(httpBasic("scott", "tiger")))
            .andExpect(status().isOk());
    }

    @Test
    void findCustomersNoAuth() throws Exception {
        mockMvc.perform(get("/customers/find"))
            .andExpect(status().isUnauthorized());
    }

//    @Test
//    @DisplayName("Access to /customers/new allowed for role ADMIN")
//    void registerCustomer() throws Exception {
//        mockMvc.perform(post("/customers/new")
//            .andExpect(status().isOk());
//    }
//
//
//    @ParameterizedTest(name = "Access denied to /customers/new for role {2}")
//    @CsvSource({
//        "user, password, USER",
//        "scott, tiger, CUSTOMER"
//    })
//    void registerCustomer(String username, String password, String role) throws Exception {
//        mockMvc.perform(post("/customers/new")
//            .with(httpBasic("scott", "tiger")))
//            .andExpect(status().isOk());
//    }
}