package br.com.softplan.services;

import br.com.softplan.exceptions.DataInvalidException;
import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.MsgError;
import br.com.softplan.models.Seem;
import br.com.softplan.repositories.SeemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SeemServiceImpl implements SeemService {

    @Autowired
    private SeemRepository seemRepository;

    @Autowired
    private MsgError handleException;

    @Override
    public List<Seem> index() {
            return seemRepository.findAll();
    }

    @Override
    public Seem show(Long id) {
        try {
            Optional<Seem> seem = seemRepository.findById(id);
            return seem.get();
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException(handleException.getWithoutSeem());
        }
    }

    @Override
    public Seem store(Seem seem) {
        try {
            return seemRepository.save(seem);
        } catch (DataIntegrityViolationException e) {
            throw new DataInvalidException(handleException.getStoreSeem());
        }
    }

    @Override
    public Seem update(Seem seem) {
        try {
            return seemRepository.save(seem);
        } catch (DataIntegrityViolationException e) {
            throw new DataInvalidException(handleException.getUpdateSeem());
        }
    }

    @Override
    public void delete(Long id) {
        Seem seem = this.show(id);
        seemRepository.delete(seem);
    }

    @Override
    public List<Seem> storeAll(List<Seem> opinions) {
        try {
            return seemRepository.saveAll(opinions);
        } catch (DataIntegrityViolationException e) {
            throw new DataInvalidException(handleException.getStoreSeem());
        }
    }
}
