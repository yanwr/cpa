package br.com.softplan.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.softplan.exceptions.DataInvalidException;
import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.Process;
import br.com.softplan.repositories.ProcessRepository;


class ProcessServiceImplTest {

	@InjectMocks
	ProcessServiceImpl service;
	
	@Mock
	ProcessRepository repository;
	
	@BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	void testFindAll() {
        Process process = new Process(null, "Foreign process", "Foreign Language", null);
        Process process2 = new Process(null, "Processo nacional", "Linguagem nacional", null);
        
		List<Process> listToBeReturned = new ArrayList<Process>();
        Collections.addAll(listToBeReturned, process, process2);
        
        when(repository.findAll()).thenReturn(listToBeReturned);
        
        List<Process> receivingList = service.index();
        
        assertEquals(2, receivingList.size());
        assertEquals("Foreign process", receivingList.get(0).getName());
	}

	@Test
	void testFindById() {
        Process originalProcess = new Process(null, "Processo padrão", "Descrição padrão", null);

        when( repository.findById( anyLong() )).thenReturn(Optional.of(originalProcess));
        
        Process receivedProcess = service.show(1L);

        assertNotNull(receivedProcess);
        assertEquals(originalProcess.getDescription(), receivedProcess.getDescription());
	}

	@Test
	void testSave() {

        Process processToBeReturned = new Process(null, "Processo padrão", "Descrição padrão", null);

        when( repository.save( any(Process.class))).thenReturn(processToBeReturned);
        
        Process returnedProcess = service.store( processToBeReturned );

        assertNotNull(returnedProcess);
        assertEquals(returnedProcess.getDescription(), "Descrição padrão");

	
	}

	@Test
	void testUpdate() {
		
        Process processToBeReturned = new Process(null, "Processo padrão", "Descrição padrão", null);

        when( repository.save( any(Process.class))).thenReturn(processToBeReturned);
        
        Process returnedProcess = service.update( processToBeReturned );

        assertNotNull(returnedProcess);
        assertEquals(returnedProcess.getDescription(), "Descrição padrão");

		
	}

	@Test
	void testDelete() {
		Process processToBeDeleted = new Process(null, "Processo padrão", "Descrição padrão", null);
		when( repository.findById( anyLong() )).thenReturn( Optional.of(processToBeDeleted) );
		
		assertDoesNotThrow(() -> {
			service.delete(1L);
		});
		verify(repository).delete(processToBeDeleted);
	}

	@Test
	void testProcessesNoOpinionByUserId() {
		Process processToBeReturned1 = new Process(null, "Processo padrão", "Descrição padrão", null);
		Process processToBeReturned2 = new Process(null, "Processo padrão 2", "Descrição padrão 2", null);
		
		List<Process> listToBeReturned = new ArrayList<Process>();
        Collections.addAll(listToBeReturned, processToBeReturned1, processToBeReturned2);
		
		when(repository.processesNoOpinionByUserId( anyLong() )).thenReturn(listToBeReturned);
		
		List<Process> receivedProcesses = service.processesNoOpinionByUserId(1L);
		
		assertNotNull(receivedProcesses);
		assertEquals(2, receivedProcesses.size());
		assertEquals("Processo padrão", receivedProcesses.get(0).getName());
	}
	
	@Test
	void testFindAll_EXCEPTION() {
        when(repository.findAll()).thenThrow(DataNotFoundException.class);
        
        assertThrows(DataNotFoundException.class, () -> {
        	service.index();
        });
	}

	@Test
	void testFindById_EXCEPTION() {
		when(repository.findById( anyLong() )).thenThrow(DataNotFoundException.class);
        
        assertThrows(DataNotFoundException.class, () -> {
        	service.show(1L);
        });	
    }

	@Test
	void testSave_EXCEPTION() {
		when(repository.save( any(Process.class) )).thenThrow(DataInvalidException.class);
        
        assertThrows(DataInvalidException.class, () -> {
        	Process process = new Process();
        	service.store(process);
        });		
	}

	@Test
	void testUpdate_EXCEPTION() {
		when(repository.save( any(Process.class) )).thenThrow(DataInvalidException.class);
        
        assertThrows(DataInvalidException.class, () -> {
        	Process process = new Process();
        	service.update(process);
        });	
	}

	@Test
	void testDelete_EXCEPTION() {
		when(repository.findById( anyLong() )).thenThrow(DataIntegrityViolationException.class);
        
        assertThrows(DataIntegrityViolationException.class, () -> {
        	service.delete(1L);
        });	
	}

	@Test
	void testProcessesNoOpinionByUserId_EXCEPTION() {
		when(repository.processesNoOpinionByUserId( anyLong() )).thenThrow(DataNotFoundException.class);
        
        assertThrows(DataNotFoundException.class, () -> {
        	service.processesNoOpinionByUserId(1L);
        });
	}

}
