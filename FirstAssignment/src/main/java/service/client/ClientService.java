package service.client;

import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    Client findById(Long id) throws EntityNotFoundException;

    Notification<Boolean> save(String name, String cnp, String cardID,String address);

    Notification<Boolean> update(Long id,String name, String cnp, String cardID,String address);

    boolean remove(Long id);

    Notification<Client> findByCnp(String cnp) throws EntityNotFoundException;


}