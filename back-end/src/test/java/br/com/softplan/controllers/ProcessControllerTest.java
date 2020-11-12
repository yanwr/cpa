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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String getToken() throws Exception {
        String user = "{\"email\": \"triador@gmail.com\", \"password\" : \"triador\"}";

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(user)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        return  result.andReturn().getResponse().getHeader("Authorization");
    }
    
    private String getTokenFinal() throws Exception {
        String user = "{\"email\": \"final2@gmail.com\", \"password\" : \"final\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
            .content(user)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
        return  result.andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    public void listAllProcessTest() throws Exception {
        mockMvc.perform(get("/processes").header("Authorization", getToken()))
            .andExpect(status().isOk());
    }
    
    @Test
    public void detailsTest() throws Exception{
        mockMvc.perform(get("/processes/1").header("Authorization", getToken()))
            .andExpect(status().isOk());
    }
    
    @Test
    public void noOptionTest() throws Exception{
        mockMvc.perform(get("/processes/no-opinion").header("Authorization", getTokenFinal()))
            .andExpect(status().isOk());
    }
    
    @Test
    public void noOptionForbiddenTest() throws Exception{
        mockMvc.perform(get("/processes/no-opinion").header("Authorization", getToken()))
            .andExpect(status().isForbidden());
    }
    
    @Test
    public void saveTest() throws Exception {
        String processes = "{\"description\": \"Test Process create in test\", \"name\" : \"Junit Process\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/processes")
            .header("Authorization", getToken())
            .content(processes)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    public void updateTest() throws Exception {
        String processes = "{\"description\": \"UpdateTest Process create in test\", \"name\" : \"Junit update Process\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/processes/1")
            .header("Authorization", getToken())
            .content(processes)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteTest() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/processes/1")
            .header("Authorization", getToken())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}