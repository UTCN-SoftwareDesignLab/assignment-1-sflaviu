package service.user;

import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import model.validation.UserValidator;
import model.validation.Validator;
import repository.EntityNotFoundException;
import repository.security.RightsRolesRepository;
import repository.user.AuthenticationException;
import repository.user.UserRepository;

import java.util.List;

public class UserServiceSQL implements UserService {

    private UserRepository userRepository;
    private AuthenticationService authenticationService;

    public UserServiceSQL(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService=authenticationService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public Notification<Boolean> save(String username, String password) {
        return authenticationService.register(username,password);
    }

    @Override
    public Notification<Boolean> update(Long id, String username, String password) {
        User user=new UserBuilder().setUserName(username).setPassword(password).build();
        Validator userValidator=new UserValidator(user);

        boolean userValid = userValidator.validate();
        Notification<Boolean> userAddingNotification = new Notification<>();
        if (!userValid) {
            userValidator.getErrors().forEach(userAddingNotification::addError);
            userAddingNotification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(authenticationService.encodePassword(password));
            userAddingNotification.setResult(userRepository.update(id,user));
        }
        return userAddingNotification;
    }

    @Override
    public boolean remove(Long id) {
        return userRepository.remove(id);
    }

    @Override
    public void removeAll()
    {
        userRepository.removeAll();
    }
}
