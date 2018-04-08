package service.client;

import model.Client;
import model.validation.Notification;
import repository.EntityNotFoundException;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    Notification<Boolean> save(String name, String cnp, String cardID,String address);

    Notification<Boolean> update(Long id,String name, String cnp, String cardID,String address);

    Notification<Client> findByCnp(String cnp);

    boolean remove(Long id);



}