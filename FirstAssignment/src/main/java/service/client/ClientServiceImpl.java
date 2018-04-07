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

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private ClientBuilder clientBuilder;

    public ClientServiceImpl(ClientRepository clientRepository, ClientBuilder clientBuilder) {
        this.clientRepository = clientRepository;
        this.clientBuilder=clientBuilder;
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(Long id) throws EntityNotFoundException {
        return clientRepository.findById(id);
    }

    @Override
    public Notification<Boolean> save(String name, String cnp, String cardID,String address) {

        Client client=clientBuilder.setName(name).setCnp(cnp).setCardNr(cardID).setAddress(address).build();

        Validator clientValidator=new ClientValidator(client);
        boolean clientValid = clientValidator.validate();
        Notification<Boolean> clientAddingNotification = new Notification<>();

        if (!clientValid) {
            clientValidator.getErrors().forEach(clientAddingNotification::addError);
            clientAddingNotification.setResult(Boolean.FALSE);
        } else {
           clientAddingNotification.setResult(clientRepository.save(client));
        }
        return clientAddingNotification;
    }

    @Override
    public Notification<Client> findByCnp(String cnp) throws EntityNotFoundException {
        return clientRepository.findByCnp(cnp);
    }

    @Override
    public Notification<Boolean> update(Long id,String name, String cnp, String cardID,String address)
    {
        Client client=clientBuilder.setName(name).setCnp(cnp).setCardNr(cardID).setAddress(address).build();
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

    @Override
    public boolean remove(Long id)
    {
        return (clientRepository.remove(id));
    }
}
