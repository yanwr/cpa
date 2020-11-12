package br.com.softplan.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String tokenTriador;

    private String getToken_ADMIN() throws Exception {
        String userA = "{\"email\": \"administrador@gmail.com\", \"password\" : \"admin\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(userA)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        return result.andReturn().getResponse().getHeader("Authorization");
    }

    private String getToken_TRIADOR() throws Exception {
        String userT = "{\"email\": \"triador@gmail.com\", \"password\" : \"triador\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(userT)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        return result.andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    void testIndex() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/users").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getToken_ADMIN())
        ).andExpect(status().isOk());
    }

    @Test
    void testShow() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/users/1").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getToken_ADMIN())
        ).andExpect(status().isOk());
    }

    @Test
    void testStore() throws Exception {
        String newUserInfo = "{\"id\": \"7\", \"name\": \"NewUser\", \"email\": \"newUser@gmail.com\", \"password\": \"123456\"}";
        mockMvc.perform( MockMvcRequestBuilders.post("/users").contentType(MediaType.APPLICATION_JSON)
            .content(newUserInfo).header("Authorization", getToken_ADMIN())
        ).andExpect(status().isCreated());
    }

    @Test
    void testUpdate() throws Exception {
        String newUserInfo = "{\"email\": \"triador2@gmail.com\", \"name\": \"triador2\", \"profile\": \"FINALIZADOR\"}";
        mockMvc.perform( MockMvcRequestBuilders.put("/users/3").contentType(MediaType.APPLICATION_JSON)
            .content(newUserInfo).header("Authorization", getToken_ADMIN())
        ).andExpect(status().isOk());
    }

    @Test
    void testShowFinisher() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.get("/users/finishers").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getToken_TRIADOR())
        ).andExpect(status().isOk());
    }

    @Test
    void testDestroy() throws Exception {
        tokenTriador = getToken_TRIADOR();
        mockMvc.perform( MockMvcRequestBuilders.delete("/users/4").contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", getToken_ADMIN())
        ).andExpect(status().isOk());
    }

    @Test
    void testForbiddenGetRequests() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isForbidden());
        mockMvc.perform(get("/users/1")).andExpect(status().isForbidden());
    }

    @Test
    void testForbiddenPostRequests() throws Exception {
        mockMvc.perform(post("/users")).andExpect(status().isForbidden());
    }

    @Test
    void testForbiddenPutRequests() throws Exception {
        mockMvc.perform(put("/users/1")).andExpect(status().isForbidden());
    }

    @Test
    void testForbiddenDeleteRequests() throws Exception {
        mockMvc.perform(put("/users/1")).andExpect(status().isForbidden());
    }
}