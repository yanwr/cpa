package br.com.softplan.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import br.com.softplan.exceptions.DataInvalidException;
import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.Seem;
import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import br.com.softplan.repositories.UserRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {
   
	@InjectMocks
    UserServiceImpl userService;
    
	@Mock
    UserRepository userRepository;
    
	@BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
	
    @Test
    void testShow() {
        User user = new User();
        user.setId(1L);
        user.setEmail("usuario@mock.com");
        user.setName("Mocked name");
        user.setPassword("mockpassword");

        when( userRepository.findById( anyLong() )).thenReturn( Optional.of(user) );

        User user2 = userService.show(1L);
        assertNotNull(user2);
        assertEquals("Mocked name", user2.getName());
    }

    @Test
    void testStore() {
        User userToBeStored = new User();
        userToBeStored.setEmail("usuario@mock.com");
        userToBeStored.setName("Mocked name");
        userToBeStored.setPassword("mockpassword");
        userToBeStored.setProfile(Profiles.TRIADOR);

        User userToReceiveReturnedUser = new User();

        when(userRepository.save(any(User.class))).thenReturn(userToBeStored);

    	userToReceiveReturnedUser = userService.store(userToBeStored);
    	System.out.print(userToReceiveReturnedUser.getName());
    	
    	assertNotNull(userToReceiveReturnedUser);
    	assertEquals(userToBeStored.getName(), userToReceiveReturnedUser.getName());
	}
    
    @Test
    void testIndex() {
    	User user1 = new User();
    	user1.setEmail("usuario@mock.com");
    	user1.setName("Mocked name");
    	user1.setPassword("mockpassword");
        user1.setProfile(Profiles.TRIADOR);
        
        User user2 = new User();
        user2.setEmail("usuario2@mock.com");
        user2.setName("Mocked name2");
        user2.setPassword("mockpassword2");
        user2.setProfile(Profiles.TRIADOR);
        
        List<User> userList = new ArrayList<User>();
        userList.add(user1);
        userList.add(user2);
        
        when(userRepository.findAll()).thenReturn(userList);
        
        List<User> receivingList = new ArrayList<User>();
        receivingList = userService.index();
        
        assertNotNull(receivingList);
        assertEquals(2, receivingList.size());
        assertEquals("usuario2@mock.com", receivingList.get(1).getEmail());
    }
    
	@Test
	void testUpdate() {
		User originalUserInfo = new User();
		originalUserInfo.setId((long) 50);
		originalUserInfo.setEmail("usuario@mock.com");
		originalUserInfo.setName("Mocked name");
		originalUserInfo.setPassword("mockpassword");
		originalUserInfo.setProfile(Profiles.TRIADOR);
		   
		User modifiedUserInfo = new User();
		modifiedUserInfo.setId((long) 50);
		modifiedUserInfo.setEmail("usuario2@mock.com");
		modifiedUserInfo.setName("Mocked name2");
		modifiedUserInfo.setPassword("mockpassword2");
		modifiedUserInfo.setProfile(Profiles.TRIADOR);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(originalUserInfo));

		User userUpdate = userService.update(modifiedUserInfo.getId(), modifiedUserInfo);
        assertEquals(modifiedUserInfo, userUpdate);
        assertEquals(modifiedUserInfo.getEmail(), userUpdate.getEmail());

	}

	@Test
	void testDestroy() {
		User userToBeDeleted = new User();
		userToBeDeleted.setId((long) 50);
		userToBeDeleted.setEmail("usuario@mock.com");
		userToBeDeleted.setName("Mocked name");
		userToBeDeleted.setPassword("mockpassword");
		userToBeDeleted.setProfile(Profiles.TRIADOR);
		
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToBeDeleted));
				
		assertEquals(true, userService.destroy(1L));
	}
	
	@Test
    void testShow_EXCEPTION() {
        when( userRepository.findById( anyLong() )).thenThrow( DataNotFoundException.class );
        assertThrows(DataNotFoundException.class, ()-> {
                userService.show(1L);
            }
        );
    }
			
	@Test
	void testDestroy_EXCEPTION() {
		when(userRepository.findById( anyLong() )).thenThrow(DataNotFoundException.class);
		assertThrows(DataNotFoundException.class, ()-> {
                userService.destroy(1L);
            }
        );
	}
	
	@Test
	void testUpdate_EXCEPTION() {
        when( userRepository.findById( anyLong() )).thenThrow( DataNotFoundException.class );
		assertThrows(DataNotFoundException.class, ()-> {
            User user = new User();
            userService.update(1L, user);
        });
	}
	
	@Test
	void testIndex_EXCEPTION()  {
        when(userRepository.findAll()).thenReturn( null );
		assertThrows(NullPointerException.class, ()-> {
            List<User> users = userService.index();
            users.get(0).getEmail();
        });
	}

	
	@Test
    void testStore_EXCEPTION() {
		when( userRepository.save( any( User.class ))).thenThrow(DataInvalidException.class);
		assertThrows(DataInvalidException.class, ()-> {
			User user = new User();
			userService.store( user );
        });
    }
}