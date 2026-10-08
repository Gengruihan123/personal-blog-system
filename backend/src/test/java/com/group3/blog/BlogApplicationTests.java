package com.group3.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class BlogApplicationTests {
    @Autowired MockMvc mvc;
    @Test void publicArticlesAreAvailable() throws Exception {mvc.perform(get("/api/articles")).andExpect(status().isOk()).andExpect(jsonPath("$.content[0].title").exists());}
    @Test void adminCanLogin() throws Exception {mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"admin\",\"password\":\"admin123\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.role").value("ADMIN"));}
}
