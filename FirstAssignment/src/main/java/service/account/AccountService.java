package service.account;

import model.Account;
import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.sql.Date;
import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(Long id) throws EntityNotFoundException;

    Notification<Boolean> save(String type,Integer balance,Long clientId, Date creation);

    Notification<Boolean> update(Long id,String type,Integer balance, Date creation);

    Notification<Boolean> transfer(Long receiverId,Long senderId, int sum);

    Notification<Boolean> payBill(Long payerId,int sum);

    boolean remove(Long id);

    List<Account> findClientsAccounts(Long clientId);
}