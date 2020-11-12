package br.com.softplan.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.softplan.exceptions.DataInvalidException;
import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.Process;
import br.com.softplan.models.Seem;
import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import br.com.softplan.repositories.SeemRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class SeemServiceImplTest {

	@InjectMocks
	SeemServiceImpl service;
	
    @Mock
    private SeemRepository repository;
    
    @Autowired
    private BCryptPasswordEncoder crypto;
	
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
	@Test
	void testFindAll() {
        User userFinalizador = new User("finalizador", "final@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        Process process = new Process(1L, "Processo SOS", "Grandiosa descrição", null);
        Seem seem = new Seem("Processo finalizado por falta de provas.");
        Seem seem2 = new Seem("Processo finalizado por motivos secretos.");
        seem.setFinisher(userFinalizador);
        seem.setProcess(process);
        seem2.setFinisher(userFinalizador);
        seem2.setProcess(process);
        
        List<Seem> listToBeReturned = new ArrayList<Seem>();
        Collections.addAll(listToBeReturned, seem, seem2);
        
        when(repository.findAll()).thenReturn(listToBeReturned);
        List<Seem> returnedList = service.index();
        
        assertEquals(2, returnedList.size());
        assertEquals(returnedList.get(0).getFinisher().getEmail(), "final@gmail.com");
        assertEquals(returnedList.get(1).getProcess().getDescription(), "Grandiosa descrição");
        assertEquals(returnedList.get(1).getDescription(), "Processo finalizado por motivos secretos.");
	}
	
	@Test
	void testFindById() {
		User userFinalizador = new User("finalizador", "final@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        Process process = new Process(1L, "Processo SOS", "Grandiosa descrição", null);
        
        Seem seemToBeReturned = new Seem("Processo finalizado por falta de provas.");
        	 seemToBeReturned.setFinisher(userFinalizador);
        	 seemToBeReturned.setProcess(process);
        
        when(repository.findById( anyLong() )).thenReturn(Optional.of(seemToBeReturned));
        Seem returnedSeem = service.show(1L);
        
        assertEquals("Processo finalizado por falta de provas.", returnedSeem.getDescription());
        assertEquals("final@gmail.com", returnedSeem.getFinisher().getEmail());
        assertEquals("Grandiosa descrição", returnedSeem.getProcess().getDescription());
	}

	@Test
	void testSave() {
		User userFinalizador = new User("finalizador", "final@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        Process process = new Process(1L, "Processo SOS", "Grandiosa descrição", null);
        
        Seem seemToBeSaved = new Seem("Processo finalizado por falta de provas.");
        seemToBeSaved.setFinisher(userFinalizador);
        seemToBeSaved.setProcess(process);
        	 
    	when(repository.save( any(Seem.class) )).thenReturn( seemToBeSaved );
    	
        Seem returnedSeem = service.store(seemToBeSaved);
        
        assertNotNull(returnedSeem);
        assertEquals("Processo finalizado por falta de provas.", returnedSeem.getDescription());
	}

	@Test
	void testUpdate() {
		User userFinalizador = new User("finalizador", "final@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        Process process = new Process(1L, "Processo SOS", "Grandiosa descrição", null);
        Seem updatedSeem = new Seem("Processo complicadíssimo.");
        updatedSeem.setFinisher(userFinalizador);
        updatedSeem.setProcess(process);
        	 
    	when(repository.save( any(Seem.class ))).thenReturn( updatedSeem );
    	
        Seem returnedSeem = service.update(updatedSeem);
        
        assertEquals("Processo complicadíssimo.", returnedSeem.getDescription());
	}

	@Test
	void testDelete() {
		Seem seemToBeDeleted = new Seem("Parecer complicadíssimo.");
		when( repository.findById( anyLong() )).thenReturn( Optional.of(seemToBeDeleted) );
		
		assertDoesNotThrow(() ->{
			service.delete(1L);
		});
		verify(repository).delete(seemToBeDeleted);
	}

	@Test
	void testSaveAll() {
		User userFinalizador = new User("finalizador", "final@gmail.com", crypto.encode("final"), Profiles.FINALIZADOR);
        Process process = new Process(1L, "Processo SOS", "Grandiosa descrição", null);
        Seem seem = new Seem("Processo finalizado por falta de provas.");
        Seem seem2 = new Seem("Processo finalizado por motivos secretos.");

        seem.setFinisher(userFinalizador);
        seem.setProcess(process);
        seem2.setFinisher(userFinalizador);
        seem2.setProcess(process);
        
        List<Seem> listToBeSaved = new ArrayList<Seem>();
        Collections.addAll(listToBeSaved, seem, seem2);
        
        when(repository.saveAll( anyList() )).thenReturn(listToBeSaved);
        
        List<Seem> returnedList = service.storeAll(listToBeSaved);
        
        assertNotNull(returnedList);
        assertEquals(2, returnedList.size());
        assertEquals("Processo finalizado por motivos secretos.", returnedList.get(1).getDescription());
        
	}
	
	@Test
	void testFindAll_EXCEPTION() {
        when( repository.findAll() ).thenThrow(DataNotFoundException.class);
        
        assertThrows(DataNotFoundException.class, () -> {
			service.index();
		});
	}
	
	@Test
	void testFindById_EXCEPTION() {
		when( repository.findById( anyLong() ) ).thenThrow(DataNotFoundException.class);
        
        assertThrows(DataNotFoundException.class, () -> {
			service.show(1L);
		});
	}

	@Test
	void testSave_EXCEPTION() {
		when( repository.save( any( Seem.class ))).thenThrow(DataInvalidException.class);
        
        assertThrows(DataInvalidException.class, () -> {
			Seem seem = new Seem();
        	service.store( seem );
		});
	}

	@Test
	void testUpdate_EXCEPTION() {
		when( repository.save( any( Seem.class ))).thenThrow(DataInvalidException.class);
        
        assertThrows(DataInvalidException.class, () -> {
			Seem seem = new Seem();
        	service.update( seem );
		});
	}

	@Test
	void testDelete_EXCEPTION() {
		when( repository.findById( anyLong() )).thenThrow( DataIntegrityViolationException.class );
		
        assertThrows(DataIntegrityViolationException.class, () -> {
        	service.delete( 1L );
		});
	}

	@Test
	void testSaveAll_EXCEPTION() {
		
		when( repository.saveAll( anyList() )).thenThrow( DataInvalidException.class );
		
        assertThrows(DataInvalidException.class, () -> {
            List<Seem> listToBeSaved = new ArrayList<Seem>();
        	service.storeAll( listToBeSaved );
		});
	}

}
