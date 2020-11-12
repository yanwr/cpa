package br.com.softplan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.softplan.models.User;
import br.com.softplan.repositories.UserRepository;
import br.com.softplan.security.UserSS;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class UserDetailServiceImplTest {
	
	@InjectMocks
	UserDetailServiceImpl userDetailService;
	
	@Mock
    UserRepository repository;
	
	@BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	void testLoadUserByUsername() {
        User userToBeReturned = new User();
        userToBeReturned.setName("name");
		
		when( repository.findByEmail( anyString() )).thenReturn ( userToBeReturned );       
		
		UserDetails returnedUser = userDetailService.loadUserByUsername("random@email");
		
		assertEquals(returnedUser.getUsername(), "name");
	}
	
	@Test
	void testLoadUserByUsername_UsernameNotFoundException() {
        		
		when( repository.findByEmail( anyString() )).thenThrow(UsernameNotFoundException.class);      
		
		assertThrows(UsernameNotFoundException.class, () -> {
			userDetailService.loadUserByUsername( "abc" );
		});
	}

}
