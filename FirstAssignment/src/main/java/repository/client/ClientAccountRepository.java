package repository.client;

import model.Account;
import model.Client;
import model.validation.Notification;

import java.util.List;

public interface ClientAccountRepository {

    List<Client> findAllClients();

    List<Account> findAllAccounts();

    Client findClientById(Long id);

    Account findAccountById(Long id);

    Notification<Client> findClientByCnp(String cnp);

    List<Account> findAccountsByOwnerId(Long ownerId);

    boolean saveClient(Client client);

    boolean saveAccount(Account account);

    void removeAll();
}
