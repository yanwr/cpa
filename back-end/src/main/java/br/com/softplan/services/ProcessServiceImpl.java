package br.com.softplan.services;

import br.com.softplan.exceptions.DataInvalidException;
import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.MsgError;
import br.com.softplan.models.Process;
import br.com.softplan.repositories.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private MsgError handleException;

    @Override
    public List<Process> index() {
        return processRepository.findAll();
    }

    @Override
    public Process show(Long id) {
        try {
            Optional<Process> process = processRepository.findById(id);
            return process.get();
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException(handleException.getWithoutProcess());
        }
    }

    @Override
    public Process store(Process process) {
        try {
            return processRepository.save(process);
        } catch (DataIntegrityViolationException e) {
            throw new DataInvalidException(handleException.getStoreProcess());
        }
    }

    @Override
    public Process update(Process process) {
        try {
            return processRepository.save(process);
        } catch (DataIntegrityViolationException e) {
            throw new DataInvalidException(handleException.getUpdateProcess());
        }
    }

    @Override
    public void delete(Long id) {
        Process process = this.show(id);
        processRepository.delete(process);
    }

    @Override
    public List<Process> processesNoOpinionByUserId(Long userId) {
        return processRepository.processesNoOpinionByUserId(userId);
    }
}