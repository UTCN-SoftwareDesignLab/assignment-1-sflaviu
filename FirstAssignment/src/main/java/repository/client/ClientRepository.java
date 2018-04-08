package repository.client;

import model.Account;
import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientRepository {

    List<Client> findAll();

    Client findById(Long id) throws EntityNotFoundException;

    Client findByCnp(String cnp) throws EntityNotFoundException;

    Notification<Client> save(Client client);

    boolean update(Long id,Client client);

    boolean remove(Long id);

    void removeAll();
}
