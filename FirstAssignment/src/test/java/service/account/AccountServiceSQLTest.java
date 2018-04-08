package service.account;

import database.DBConnectionFactory;
import model.Account;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.account.AccountRepositoryMySQL;
import repository.client.ClientRepositoryMySQL;
import service.client.ClientService;
import service.client.ClientServiceSQL;

import java.sql.Connection;
import java.sql.Date;

public class AccountServiceSQLTest {

    private static Account firstTestAccount;
    private static  Account secondTestAccount;
    private static AccountService accountService;
    private static ClientService clientService;


    @BeforeClass
    public static void setUp() {
        Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();

        AccountRepositoryMySQL accountRepositoryMySQL=new AccountRepositoryMySQL(connection);

        clientService=new ClientServiceSQL(new ClientRepositoryMySQL(connection));
        accountService=new AccountServiceSQL(accountRepositoryMySQL,clientService);

    }

    @Before
    public void cleanUp() {clientService.removeAll();
        clientService.save("Flaviu Samarghitan","1231567891","1234567891234567","aici");
        clientService.save("NonFlaviu Samarghitan","1987654321","1234567891234568","nuaici");

        firstTestAccount=accountService.save("Balance",100,"1231567891",new Date(1000L)).getResult();
        secondTestAccount=accountService.save("Spending",300,"1987654321",new Date(100L)).getResult();

    }

    @Test
    public void save() {
        Assert.assertTrue(accountService.save("Balance",-100,"1231567891",new Date(1000L)).hasErrors());
    }

    @Test
    public void findAll(){
        Assert.assertTrue(accountService.findAll().size()==2);
    }
    @Test
    public void update(){
        Assert.assertTrue(accountService.update(firstTestAccount.getId(),"NotBalance",1000,firstTestAccount.getCreation()).getResult());
    }

    @Test
    public void transfer(){
        int firstPreviousSum=firstTestAccount.getBalance();
        int secondPreviousSum=secondTestAccount.getBalance();
        accountService.transfer(secondTestAccount.getId(),firstTestAccount.getId(),50);

        int newFirstSum=accountService.findById(firstTestAccount.getId()).getResult().getBalance();
        int secondNewSum=accountService.findById(secondTestAccount.getId()).getResult().getBalance();
        Assert.assertTrue(firstPreviousSum-50==newFirstSum && secondPreviousSum+50==secondNewSum);
    }

}
