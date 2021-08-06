package guru.sfg.brewery.web.controllers.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import guru.sfg.brewery.web.controllers.BaseIT;


@WebMvcTest
class BeerRestControllerIT extends BaseIT {

 @Test
 public void listBeersNoParams() throws Exception {
     mockMvc.perform(get("/api/v1/beer/")).andExpect(status().isOk());
 }

 @Test
 public void getBeerById() throws Exception{
     mockMvc.perform(get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")).andExpect(status().isOk());
 }

}