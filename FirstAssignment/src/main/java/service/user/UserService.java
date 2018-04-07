package service.user;

import model.User;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.sql.Date;
import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id) throws EntityNotFoundException;

    Notification<Boolean> save(String username, String password);

    Notification<Boolean> update(Long id,String username, String password);

    boolean remove(Long id);
}
