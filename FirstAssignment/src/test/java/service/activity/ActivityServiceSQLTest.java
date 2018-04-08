package service.activity;

import database.DBConnectionFactory;
import model.Account;
import model.Client;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepository;
import repository.account.AccountRepositoryMySQL;
import repository.activity.ActivityRepositoryMySQL;
import repository.client.ClientRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceSQL;
import service.user.UserService;
import service.user.UserServiceSQL;

import java.sql.Connection;
import java.sql.Date;

public class ActivityServiceSQLTest {

    private static ActivityService activityService;
    private static UserRepositoryMySQL userRepositoryMySQL;
    private static AccountRepositoryMySQL accountRepositoryMySQL;
    private static ClientRepositoryMySQL clientRepositoryMySQL;

    @BeforeClass
    public static void setUp() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();

        ActivityRepositoryMySQL activityRepositoryMySQL=new ActivityRepositoryMySQL(connection);
        activityService=new ActivityServiceSQL(activityRepositoryMySQL);

        userRepositoryMySQL=new UserRepositoryMySQL(connection,new RightsRolesRepositoryMySQL(connection));

        accountRepositoryMySQL=new AccountRepositoryMySQL(connection);
        clientRepositoryMySQL=new ClientRepositoryMySQL(connection);
    }

    @Before
    public void cleanUp() {
        activityService.removeAll();
        for(User user: userRepositoryMySQL.findAll())
            for(Client client:clientRepositoryMySQL.findAll())
                activityService.save("Modified client's data",user.getId(),new Date(5000L),client.getId(),null);
        for(User user: userRepositoryMySQL.findAll())
            for(Account account:accountRepositoryMySQL.findAll())
                activityService.save("Modified client's data",user.getId(),new Date(5000L),null,account.getId());

    }
    @Test
    public void findAll() {
        Assert.assertTrue(activityService.findAll().size() == userRepositoryMySQL.findAll().size()*(accountRepositoryMySQL.findAll().size()+clientRepositoryMySQL.findAll().size()));
    }

    @Test
    public void findBypPerformerId()
    {
        if(userRepositoryMySQL.findAll().size()!=0)
            Assert.assertFalse(activityService.findByPerformer(userRepositoryMySQL.findAll().get(0).getId()).isEmpty());
    }
}
