import database.DBConnectionFactory;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;
import model.builder.UserBuilder;
import model.validation.ClientValidator;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.client.ClientRepository;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.account.AccountService;
import service.account.AccountServiceImpl;
import service.activity.ActivityService;
import service.activity.ActivityServiceImpl;
import service.client.ClientService;
import service.client.ClientServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import service.user.UserService;
import service.user.UserServiceImpl;

import java.sql.Connection;

/**
 * Created by Alex on 18/03/2017.
 */
public class ComponentFactory {

    private final AuthenticationService authenticationService;
    private final AccountService accountService;
    private final ActivityService activityService;
    private final ClientService clientService;
    private final UserService userService;

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final ActivityRepository activityRepository;

    private static ComponentFactory instance;

    public static ComponentFactory instance(Boolean componentsForTests) {
        if (instance == null) {
            instance = new ComponentFactory(componentsForTests);
        }
        return instance;
    }

    private ComponentFactory(Boolean componentsForTests) {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(componentsForTests).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.accountRepository= new AccountRepositoryMySQL(connection);
        this.clientRepository = new ClientRepositoryMySQL(connection,accountRepository);
        this.activityRepository = new ActivityRepositoryMySQL(connection,userRepository,clientRepository,accountRepository);

        this.authenticationService=new AuthenticationServiceImpl(userRepository,rightsRolesRepository);
        this.accountService=new AccountServiceImpl(accountRepository,new AccountBuilder());
        this.activityService= new ActivityServiceImpl(activityRepository) ;
        this.clientService=new ClientServiceImpl(clientRepository,new ClientBuilder());
        this.userService=new UserServiceImpl(new UserBuilder(),userRepository,rightsRolesRepository,authenticationService);
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }

    public ActivityRepository getActivityRepository() {
        return activityRepository;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public ActivityService getActivityService() {
        return activityService;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public UserService getUserService() {
        return userService;
    }
    public static ComponentFactory getInstance() {
        return instance;
    }
}


