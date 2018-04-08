package service.account;

import model.Account;
import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.sql.Date;
import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Notification<Account> findById(Long id);

    Notification<Account> save(String type,Integer balance,String clientCNP, Date creation);

    Notification<Boolean> update(Long id,String type,Integer balance, Date creation);

    Notification<Boolean> transfer(Long receiverId,Long senderId, int sum);

    Notification<Boolean> subtract(Long payerId,int sum);

    boolean remove(Long id);

    List<Account> findClientsAccounts(Long clientId);
}