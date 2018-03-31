import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ClientBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {

        ComponentFactory cf=ComponentFactory.instance(true);

        ClientBuilder cb=new ClientBuilder();

        Client flaviu=cb.setCnp("123").setName("Flaviu").setAddress("aici").setCardNr("456").build();

        cf.getClientAccountRepository().saveClient(flaviu);
        cf.getClientAccountRepository().saveClient(cb.setCnp("456").setName("Nu Flaviu").setAddress("dincolo").setCardNr("567").build());

        System.out.println(cf.getClientAccountRepository().findClientById(1L).getName());

        java.util.Date utilDate = new java.util.Date();

        AccountBuilder ab=new AccountBuilder();
        cf.getClientAccountRepository().saveAccount(ab.setOwner(flaviu).setBalance(100000).setCreation(new Date(utilDate.getTime())).setType("1 dollar for each SOLID principle broken").build());

        cf.getClientAccountRepository().saveAccount(ab.setOwner(flaviu).setBalance(0).setCreation(new Date(utilDate.getTime())).setType("1 dollar for each project delieverd on time").build());

        List<Account> myAccounts=cf.getClientAccountRepository().findAccountsByOwnerId(flaviu.getId());
        for(Account a: myAccounts)
            System.out.println(a.getBalance());

        System.out.println(cf.getRightsRolesRepository().findRoleById(1L).getRole());
    }
}

