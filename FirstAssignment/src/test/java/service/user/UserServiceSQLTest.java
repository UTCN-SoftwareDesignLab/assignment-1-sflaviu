package service.user;

import database.DBConnectionFactory;
import model.Role;
import model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;

public class UserServiceSQLTest {

    private static UserService userService;
    private static UserRepository userRepositoryMySQL;
    private static RightsRolesRepositoryMySQL rightsRolesRepositoryMySQL;
    private static AuthenticationService authenticationService;

    @BeforeClass
    public static void setUp() {

        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();

        rightsRolesRepositoryMySQL=new RightsRolesRepositoryMySQL(connection);
        userRepositoryMySQL=new UserRepositoryMySQL(connection,rightsRolesRepositoryMySQL);
        authenticationService=new AuthenticationServiceSQL(userRepositoryMySQL,rightsRolesRepositoryMySQL);
        userService=new UserServiceSQL(userRepositoryMySQL,authenticationService);
    }

    @Before
    public void cleanUp() throws Exception{
        userService.removeAll();
        userService.save("administrator@gmail.com","Administrator123?");
        userService.save("user@gmail.com","User123?");

        List<Role> roles=new ArrayList<>();
        roles.add(rightsRolesRepositoryMySQL.findRoleByTitle(ADMINISTRATOR));

        String encodedPassword=authenticationService.encodePassword("Administrator123?");
        rightsRolesRepositoryMySQL.addRolesToUser(userRepositoryMySQL.findByUsernameAndPassword("administrator@gmail.com",encodedPassword).getResult(),roles);
    }

    @Test
    public void findAll()
    {
        Assert.assertTrue(userService.findAll().size()==2);
    }

    @Test
    public void saveAndRemove() throws Exception
    {
        int previousSize=userService.findAll().size();

        Assert.assertTrue(userService.save("removable@gmail.com","badpassword?").getFormattedErrors().matches("Password must contain at least one number!"));
        Assert.assertTrue(userService.save("removable@gmail.com","1goodpassword!").getResult());

        Assert.assertTrue(previousSize==userService.findAll().size()-1);
        previousSize=previousSize+1;

        String encodedPassword=authenticationService.encodePassword("1goodpassword!");
        User removable=userRepositoryMySQL.findByUsernameAndPassword("removable@gmail.com",encodedPassword).getResult();

        userService.remove(removable.getId());
        Assert.assertTrue(previousSize==userService.findAll().size()+1);


    }

}
