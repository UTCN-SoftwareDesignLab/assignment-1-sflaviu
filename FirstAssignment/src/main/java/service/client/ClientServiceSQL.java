package service.client;

import model.Client;
import model.builder.Builder;
import model.builder.ClientBuilder;
import model.validation.ClientValidator;
import model.validation.Notification;
import model.validation.Validator;
import repository.EntityNotFoundException;
import repository.client.ClientRepository;

import java.util.List;

public class ClientServiceSQL implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceSQL(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }


    @Override
    public Notification<Client> save(String name, String cnp, String cardID,String address) {

        Client client=new ClientBuilder().setName(name).setCnp(cnp).setCardNr(cardID).setAddress(address).build();

        Validator clientValidator=new ClientValidator(client);
        boolean clientValid = clientValidator.validate();
        Notification<Client> clientAddingNotification = new Notification<>();

        if (!clientValid) {
            clientValidator.getErrors().forEach(clientAddingNotification::addError);
        } else {
           clientAddingNotification.setResult(clientRepository.save(client).getResult());
        }
        return clientAddingNotification;
    }

    @Override
    public Notification<Boolean> update(Long id,String name, String cnp, String cardID,String address)
    {
        Client client=new ClientBuilder().setName(name).setCnp(cnp).setCardNr(cardID).setAddress(address).build();
        Validator clientValidator=new ClientValidator(client);

        boolean clientValid = clientValidator.validate();
        Notification<Boolean> clientAddingNotification = new Notification<>();

        if (!clientValid) {
            clientValidator.getErrors().forEach(clientAddingNotification::addError);
            clientAddingNotification.setResult(Boolean.FALSE);
        } else {
            clientAddingNotification.setResult(clientRepository.update(id,client));
        }
        return clientAddingNotification;

    }

    public Notification<Client> findByCnp(String cnp)
    {
        Notification<Client> findByCnpNotification=new Notification<>();

        try {
            findByCnpNotification.setResult(clientRepository.findByCnp(cnp));
        }
        catch(EntityNotFoundException e)
        {
            findByCnpNotification.addError("No client with "+e.getEntityId()+" as cnp was found!");
        }
        return findByCnpNotification;
    }

    @Override
    public boolean remove(Long id)
    {
        return (clientRepository.remove(id));
    }

    @Override
    public void removeAll()
    {
        clientRepository.removeAll();;
    }
}
