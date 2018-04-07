import controller.*;
import view.*;

import java.util.HashMap;

import static database.Constants.Operations.*;
import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

public class main {
    public static void main(String[] args) {

        ComponentFactory cf=ComponentFactory.instance(true);
        /*

        ClientBuilder cb=new ClientBuilder();

        Client flaviu=cb.setCnp("123").setName("Flaviu").setAddress("aici").setCardNr("456").build();

        ClientService cs=cf.getClientService();

        cs.save("Flaviu","1234567891","1234567891234567","aici");
        try {
            Notification<Client> rez = cs.findByCnp("123");
            System.out.println(rez.getResult().getName());
        }
        catch(Exception e)
        {
            System.out.println("Nope");
        }


        java.util.Date utilDate = new java.util.Date();

        AccountBuilder ab=new AccountBuilder();

        AccountService as=cf.getAccountService();

       // as.save(ab.setBalance(100000).setCreation(new Date(utilDate.getTime())).setType("1 dollar for each SOLID principle broken").build(),1L);

       // as.save(ab.setBalance(0).setCreation(new Date(utilDate.getTime())).setType("1 dollar for each project delieverd on time").build(),1L);

        try {
            System.out.println(as.findClientsAccounts(flaviu).get(0).getBalance());
        }
        catch(Exception e){};

        ActivityService acts=cf.getActivityService();
        ActivityBuilder acb=new ActivityBuilder();

        List<Role> roles=new ArrayList<Role>();
        roles.add(cf.getRightsRolesRepository().findRoleById(2L));

        Notification<Boolean> bossReg=cf.getAuthenticationService().register("nothingisworking@help.me","Parola123?");

        Notification<User> boss=null;
        try {
            boss = cf.getAuthenticationService().login("nothingisworking@help.me", "Parola123?");
        }
        catch(Exception e)
        {

        }
        //System.out.println(boss.getResult().getId());

        acts.save(acb.setDate(new Date(utilDate.getTime())).setModifiedAccount(as.findAll().get(0)).setType("Messing arround").setPerformer(boss.getResult()).build());

        //System.out.println(cf.getUserRepository().findAll().get(0).getUserName());
        // System.out.println(acts.findAll().get(0).getType());

*/
        Controller clientController=new ClientCRUDController(new ClientCRUDView(),cf.getClientService(),new HashMap<>());
        Controller transferController=new TransferController(new TransferView(),new HashMap<>(),cf.getAccountService());

        Controller payBillController=new BillController(new BillView(),new HashMap<>(),cf.getAccountService(),cf.getClientService());

        Controller userController=new UserCRUDController(new UserCRUDView(),new HashMap<>(),cf.getUserService());

        HashMap<String, Controller> nextAdmin=new HashMap<>();
        nextAdmin.put(CRUD_CLIENT,clientController);
        nextAdmin.put(TRANSFER_MONEY,transferController);
        nextAdmin.put(PAY_BILLS,payBillController);
        nextAdmin.put(CRUD_EMPLOYEE,userController);

        HashMap<String, Controller> nextUser=new HashMap<>();
        nextUser.put(CRUD_CLIENT,clientController);
        nextUser.put(TRANSFER_MONEY,transferController);
        nextUser.put(PAY_BILLS,payBillController);

        AdminOperationsController ac=new AdminOperationsController(new AdminOperationsView(),nextAdmin);
        UserOperationsController uc=new UserOperationsController(new UserOperationsView(),nextUser);

        HashMap<String, Controller> nextLogIn=new HashMap<>();
        nextLogIn.put(ADMINISTRATOR,ac);
        nextLogIn.put(EMPLOYEE,uc);


        new LoginController(new LoginView(), cf.getAuthenticationService(),nextLogIn);

    }
}
