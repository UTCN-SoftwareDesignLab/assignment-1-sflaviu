import database.DBConnectionFactory;
import repository.activity.ActivityRepository;
import repository.activity.ActivityRepositoryMySQL;
import repository.client.ClientAccountRepository;
import repository.client.ClientAccountRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;

/**
 * Created by Alex on 18/03/2017.
 */
public class ComponentFactory {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final ClientAccountRepository clientAccountRepository;
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
        this.clientAccountRepository = new ClientAccountRepositoryMySQL(connection);
        this.activityRepository = new ActivityRepositoryMySQL(connection,userRepository,clientAccountRepository);
    }


    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }

    public ClientAccountRepository getClientAccountRepository() {
        return clientAccountRepository;
    }

    public ActivityRepository getActivityRepository() {
        return activityRepository;
    }
}


