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

public class UserServiceImpl implements UserService {

    private UserBuilder userBuilder;
    private UserRepository userRepository;
    private RightsRolesRepository rightsRolesRepository;
    private AuthenticationService authenticationService;

    public UserServiceImpl(UserBuilder userBuilder, UserRepository userRepository,RightsRolesRepository rightsRolesRepository,AuthenticationService authenticationService) {
        this.userBuilder = userBuilder;
        this.userRepository = userRepository;
        this.rightsRolesRepository=rightsRolesRepository;
        this.authenticationService=authenticationService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id);
    }

    @Override
    public Notification<Boolean> save(String username, String password) {
        return authenticationService.register(username,password);
    }

    @Override
    public Notification<Boolean> update(Long id, String username, String password) {
        User user=userBuilder.setUserName(username).setPassword(password).build();
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
}
