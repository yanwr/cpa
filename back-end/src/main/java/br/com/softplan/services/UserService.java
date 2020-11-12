package br.com.softplan.services;

import br.com.softplan.models.User;
import br.com.softplan.security.UserSS;
import java.util.List;

public interface UserService {
    List<User> index() throws Exception;

    User show(Long id);

    User store(User user);

    User update(Long id, User user);

    boolean destroy(Long id);

    UserSS authenticated();

    List<User> findFinishers();
}