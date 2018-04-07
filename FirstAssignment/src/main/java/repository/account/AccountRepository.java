package repository.account;

import model.Account;
import repository.EntityNotFoundException;

import java.util.List;

public interface AccountRepository {

    List<Account> findAll();

    Account findById(Long id) throws EntityNotFoundException;

    boolean save(Account account,Long clientId);

    List<Account> findAccountsByOwnerId(Long ownerId);

    boolean update(Long id,Account account);

    boolean remove(Long id);

    void removeAll();

}
