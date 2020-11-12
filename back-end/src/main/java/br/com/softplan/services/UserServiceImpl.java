package br.com.softplan.services;

import br.com.softplan.exceptions.DataInvalidException;
import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.MsgError;
import br.com.softplan.models.User;
import br.com.softplan.models.enuns.Profiles;
import br.com.softplan.repositories.UserRepository;
import br.com.softplan.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MsgError handleException;

    @Override
    public List<User> index() {
        return repository.findAll();
    }

    @Override
    public User show(Long id) {
        try {
            Optional<User> user = repository.findById(id);
            return user.get();
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException(handleException.getWithoutUser());
        }
    }

    @Override
    public User store(User user) {
        try {
            return repository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataInvalidException(handleException.getStoreUser());
        }
    }

    @Override
    public User update(Long id, User user) {
        if (this.hasCurrentUser(id)) {
            user.setId(id);
            this.store(user);
        }
        return user;
    }

    @Override
    public boolean destroy(Long id) {
        if (this.hasAdmin(id)) {
            return false;
        } else if (this.hasCurrentUser(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserSS authenticated() {
        return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public List<User> findFinishers() {
        return repository.findFinishers();
    }

    private boolean hasCurrentUser(Long id) {
        User currentUser = this.show(id);
        return currentUser != null && currentUser.getId() != null;
    }

    private boolean hasAdmin(Long id) {
        Optional<User> user = Optional.of(this.show(id));
        return user.map(value -> value.getProfile().contains(Profiles.ADMIN)).orElse(false);
    }
}
