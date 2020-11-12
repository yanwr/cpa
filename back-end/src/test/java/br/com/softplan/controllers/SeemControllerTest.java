package br.com.softplan.controllers;

import br.com.softplan.models.Process;
import br.com.softplan.models.Seem;
import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import br.com.softplan.repositories.ProcessRepository;
import br.com.softplan.repositories.UserRepository;
import br.com.softplan.services.SeemServiceImpl;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class SeemControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
    @Autowired
    private SeemServiceImpl service;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProcessRepository processRepository;
        
    @Order(1)
	@Test
	void testFindAll() throws Exception {
		mockMvc.perform( MockMvcRequestBuilders.get("/opinions")
			.contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", getToken_TRIADOR()))
			.andExpect(status().isOk());
	}

    @Order(2)
	@Test
	public void save() throws Exception {
		User user = new User("Mocked User", "mocked@gmail.com", "mock123", Profiles.TRIADOR);
	    user.setId(1L);
	    userRepository.save(user);
		    
	    Process process = new Process();
	    process.setId(4L);
	    process.setName("Mocked process");
	    process.setDescription("Mocked description");
	    processRepository.save(process);

		Seem seem = new Seem();
	    seem.setId(4L);
	    seem.setDescription("Mocked opinion");
	    process.addOpinion(seem);
	    seem.setProcess(process);
		    
		mockMvc.perform(post("/opinions")
			.contentType(MediaType.APPLICATION_JSON)
			.content("{\"process\":" + seem.getProcess().getId() + ", \"users\": [3,\n" + "4]}")
			.header("Authorization", getToken_TRIADOR())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andDo(MockMvcResultHandlers.print());
	}

    @Order(3)
	@Test
	void testUpdate() throws Exception {
        Seem seem = service.show((long) 1);
        
        seem.setDescription("Test opinion");
        
        mockMvc.perform(put("/opinions/" + seem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\": \"" + seem.getDescription() + "\"}")
                .header("Authorization", getToken_FINALIZADOR())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value(seem.getDescription()))
                .andExpect(jsonPath("$.process").value(1))
                .andDo(MockMvcResultHandlers.print());
	}

    @Order(4)
	@Test
	void testDetails() throws Exception {
		Seem seem = service.show((long) 1);

        mockMvc.perform(get("/opinions/" + seem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", getToken_FINALIZADOR())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value(seem.getDescription()))
                .andExpect(jsonPath("$.process").value(1))
                .andDo(MockMvcResultHandlers.print());
	}

    @Order(5)
	@Test
	void testRemove() throws Exception {
        mockMvc.perform(delete("/opinions/1").contentType(MediaType.APPLICATION_JSON)
			.header("Authorization", getToken_FINALIZADOR())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	private String getToken_ADMIN() throws Exception {
        String userA = "{\"email\": \"administrador@gmail.com\", \"password\" : \"admin\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
			.content(userA)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
        return  result.andReturn().getResponse().getHeader("Authorization");
    }
		
	private String getToken_TRIADOR() throws Exception {
		String userT = "{\"email\": \"triador@gmail.com\", \"password\" : \"triador\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
			.content(userT)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
        return  result.andReturn().getResponse().getHeader("Authorization");
    }
	
	private String getToken_FINALIZADOR() throws Exception {
		String userT = "{\"email\": \"final@gmail.com\", \"password\" : \"final\"}";
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
			.content(userT)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk());
        return  result.andReturn().getResponse().getHeader("Authorization");
    }
}