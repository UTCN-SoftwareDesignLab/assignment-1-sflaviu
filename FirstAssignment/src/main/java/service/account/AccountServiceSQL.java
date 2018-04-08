package service.account;

import model.Account;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.Builder;
import model.validation.AccountValidator;
import model.validation.Notification;
import model.validation.SubtractionValidator;
import model.validation.Validator;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.account.AccountRepository;
import repository.user.UserRepository;
import service.client.ClientService;
import service.user.UserService;

import java.sql.Date;
import java.util.List;

public class AccountServiceSQL implements AccountService {

    private final AccountRepository accountRepository;

    private final ClientService clientService;

    public AccountServiceSQL(AccountRepository accountRepository, ClientService clientService) {
        this.accountRepository = accountRepository;
        this.clientService=clientService;
    }

    @Override
    public Notification<Account> findById(Long id)
    {
        Notification<Account> notification=new Notification<>();
        try {
            notification.setResult(accountRepository.findById(id));
            return notification;
        }catch(EntityNotFoundException e)
        {
            notification.addError(e.getMessage());
            return notification;
        }
    }
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }


    @Override
    public Notification<Account> save(String type, Integer balance, String clientCNP, Date creation) {

        Account account=new AccountBuilder().setType(type).setBalance(balance).setCreation(creation).build();
        Validator accountValidator=new AccountValidator(account);

        Notification<Account> accountAddingNotification=new Notification<>();

        Notification<Client> clientFindNotification= findOwner(clientCNP);

        if(clientFindNotification.hasErrors()) {
            accountValidator.getErrors().forEach(accountAddingNotification::addError);
            return accountAddingNotification;
        }
        boolean accountValid = accountValidator.validate();

        if (!accountValid) {
            accountValidator.getErrors().forEach(accountAddingNotification::addError);
        } else {
            accountAddingNotification.setResult(accountRepository.save(account,clientFindNotification.getResult().getId()).getResult());
        }
        return accountAddingNotification;
    }

    @Override
    public Notification<Boolean> update(Long id, String type, Integer balance, Date creation) {
        Account account=new AccountBuilder().setType(type).setBalance(balance).setCreation(creation).build();
        Validator accountValidator=new AccountValidator(account);

        boolean accountValid = accountValidator.validate();
        Notification<Boolean> accountAddingNotification = new Notification<>();

        if (!accountValid) {
            accountValidator.getErrors().forEach(accountAddingNotification::addError);
            accountAddingNotification.setResult(Boolean.FALSE);
        } else {
            accountAddingNotification.setResult(accountRepository.update(id,account));
        }
        return accountAddingNotification;
    }

    @Override
    public boolean remove(Long id) {
        return accountRepository.remove(id);
    }

    @Override
    public List<Account> findClientsAccounts(Long clientId){
        return accountRepository.findAccountsByOwnerId(clientId);
    }

    @Override
    public Notification<Boolean> subtract(Long payerId,int sum) {

        Notification<Boolean> subtractionNotification = new Notification<>();
        subtractionNotification.setResult(false);

        try {
            Account account = accountRepository.findById(payerId);

            Validator subtractionValidator=new SubtractionValidator(account,sum);

            if(subtractionValidator.validate())
            {
                account.setBalance(account.getBalance() - sum);
                if (!accountRepository.update(payerId, account)) {
                    subtractionNotification.addError("Updating the account could not be done! Subtraction not performed");
                    account.setBalance(account.getBalance() + sum);
                }
                else
                    subtractionNotification.setResult(Boolean.TRUE);
            }
            else
            {
                subtractionValidator.getErrors().forEach(subtractionNotification::addError);
                subtractionNotification.setResult(Boolean.FALSE);
            }

        } catch (EntityNotFoundException e) {
            subtractionNotification.addError("Account doesn't exist, possible database problems!");
        }
        return subtractionNotification;
    }
    @Override
    public Notification<Boolean> transfer(Long receiverId,Long senderId, int sum)
    {
        Notification<Boolean> transferNotification=new Notification<>();
        transferNotification.setResult(false);

        try {
            Account sender=accountRepository.findById(senderId);

            Validator subtractionValidator=new SubtractionValidator(sender,sum);
            if(subtractionValidator.validate())
            {
                sender.setBalance(sender.getBalance()-sum);
                if(!accountRepository.update(senderId,sender)) {
                    transferNotification.addError("Updating the sender could not be done!");
                    sender.setBalance(sender.getBalance() + sum);
                }
                else
                {
                    Account receiver=accountRepository.findById(receiverId);
                    receiver.setBalance(receiver.getBalance()+sum);
                    if(!accountRepository.update(receiverId,receiver))
                    {
                        transferNotification.addError("Updating the receiver could not be done!");
                        receiver.setBalance(receiver.getBalance()-sum);
                        sender.setBalance(sender.getBalance()+sum);
                        accountRepository.update(senderId,sender);
                    }
                    else
                        transferNotification.setResult(true);
                }
            }
            else
            {
                subtractionValidator.getErrors().forEach(transferNotification::addError);
                transferNotification.setResult(Boolean.FALSE);
            }


        } catch (EntityNotFoundException e) {
            transferNotification.addError("Accounts don't exist, possible database problems!");
        }
        return transferNotification;
    }

    private Notification<Client> findOwner(String cnp)
    {
        return clientService.findByCnp(cnp);
    }
}
