package br.com.softplan.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void LoginTestSuccess() throws Exception {
        String user = "{\"email\": \"administrador@gmail.com\", \"password\" : \"admin\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(user)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void LoginTestFailUsername() throws Exception {
        Assertions.assertThrows(RuntimeException.class, () -> {
            String user = "{\"email\": \"administrator@gmail.com\", \"password\" : \"admin\"}";
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        });
    }

    @Test
    public void LoginTestFailPassword() throws Exception {
        Assertions.assertThrows(RuntimeException.class, () -> {
            String user = "{\"email\": \"administrador@gmail.com\", \"password\" : \"adminerror\"}";
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .content(user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        });
    }
}