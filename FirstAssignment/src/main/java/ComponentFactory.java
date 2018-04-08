import controller.*;
import database.DBConnectionFactory;
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
import service.account.AccountServiceSQL;
import service.activity.ActivityService;
import service.activity.ActivityServiceSQL;
import service.client.ClientService;
import service.client.ClientServiceSQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceSQL;
import service.user.UserService;
import service.user.UserServiceSQL;
import view.*;

import java.sql.Connection;
import java.util.HashMap;

import static database.Constants.Operations.*;
import static database.Constants.Operations.GENERATE_REPORT;
import static database.Constants.Operations.LOG_OUT;
import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

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

    private final Controller clientController;
    private final Controller transferController;
    private final Controller payBillController;
    private final Controller userController;
    private final Controller reportController;
    private final Controller accountController;

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
        this.clientRepository = new ClientRepositoryMySQL(connection);
        this.activityRepository = new ActivityRepositoryMySQL(connection);

        this.authenticationService=new AuthenticationServiceSQL(userRepository,rightsRolesRepository);
        this.clientService=new ClientServiceSQL(clientRepository);
        this.accountService=new AccountServiceSQL(accountRepository,clientService);
        this.activityService= new ActivityServiceSQL(activityRepository) ;
        this.userService=new UserServiceSQL(userRepository,rightsRolesRepository,authenticationService);


        this.clientController=new ClientCRUDController(new ClientCRUDView(),clientService,new HashMap<>(),activityService);
        this.transferController=new TransferController(new TransferView(),new HashMap<>(),accountService,activityService);
        this.payBillController=new BillController(new BillView(),new HashMap<>(),accountService,clientService,activityService);
        this.userController=new UserCRUDController(new UserCRUDView(),new HashMap<>(),userService,activityService);
        this.reportController=new ReportController(new ReportView(),new HashMap<>(),userService,activityService);
        this.accountController=new AccountCRUDController(new AccountCRUDView(),new HashMap<>(),accountService,activityService);

        HashMap<String, Controller> nextAdmin=new HashMap<>();
        nextAdmin.put(CRUD_CLIENT,clientController);
        nextAdmin.put(TRANSFER_MONEY,transferController);
        nextAdmin.put(PAY_BILLS,payBillController);
        nextAdmin.put(CRUD_EMPLOYEE,userController);
        nextAdmin.put(CRUD_ACCOUNTS,accountController);
        nextAdmin.put(GENERATE_REPORT,reportController);

        HashMap<String, Controller> nextUser=new HashMap<>();
        nextUser.put(CRUD_CLIENT,clientController);
        nextUser.put(TRANSFER_MONEY,transferController);
        nextUser.put(PAY_BILLS,payBillController);
        nextUser.put(CRUD_ACCOUNTS,accountController);
        nextUser.put(GENERATE_REPORT,reportController);

        AdminOperationsController adminOperationsController=new AdminOperationsController(new AdminOperationsView(),nextAdmin);
        UserOperationsController userOperationsController=new UserOperationsController(new UserOperationsView(),nextUser);

        HashMap<String, Controller> nextLogIn=new HashMap<>();
        nextLogIn.put(ADMINISTRATOR,adminOperationsController);
        nextLogIn.put(EMPLOYEE,userOperationsController);

        LoginController loginController=new LoginController(new LoginView(), authenticationService,nextLogIn);

        nextAdmin.put(LOG_OUT,loginController);
        nextUser.put(LOG_OUT,loginController);
    }

    public static ComponentFactory getInstance() {
        return instance;
    }
}


