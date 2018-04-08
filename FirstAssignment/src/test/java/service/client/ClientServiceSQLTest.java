package service.client;

import database.DBConnectionFactory;
import model.Client;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.client.ClientRepositoryMySQL;

import java.sql.Connection;

public class ClientServiceSQLTest {

        public static Client testClient;
        private static ClientService clientService;

        @BeforeClass
        public static void setUp() {
            Connection connection = new DBConnectionFactory().getConnectionWrapper(true).getConnection();

            ClientRepositoryMySQL clientRepositoryMySQL=new ClientRepositoryMySQL(connection);

            clientService=new ClientServiceSQL(clientRepositoryMySQL);
        }

        @Before
        public void cleanUp() {clientService.removeAll();
            testClient=clientService.save("Flaviu Samarghitan","1231567891","1234567891234567","Aici").getResult();
            clientService.save("Mihai Mihaescu","9999999999","1234567191234367","Nu stiu nr.6");
            }

        @Test
        public void save() {
            Assert.assertTrue(clientService.save("Altcineva","9999999999","123456711891234568","Undeva departe").getFormattedErrors().matches("Card number must have 16 characters!"));

        }
        @Test
        public void findAll(){
            Assert.assertTrue(clientService.findAll().size()==2);
        }
        @Test
        public void update(){
            Assert.assertTrue(clientService.update(testClient.getId(),testClient.getName(),"7777777777",testClient.getCardNr(),testClient.getAddress()).getResult());
        }
        @Test
        public void findByCnp()
        {
            Assert.assertTrue(clientService.findByCnp("1234567891").hasErrors());
            Assert.assertFalse(clientService.findByCnp("9999999999").hasErrors());
        }
        @Test
        public void remove()
        {
            int previousSize=clientService.findAll().size();
            clientService.remove(testClient.getId());
            Assert.assertTrue(clientService.findAll().size()==previousSize-1);
        }

}

