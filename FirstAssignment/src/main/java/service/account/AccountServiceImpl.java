package service.account;

import model.Account;
import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.validation.AccountValidator;
import model.validation.Notification;
import model.validation.Validator;
import repository.EntityNotFoundException;
import repository.account.AccountRepository;
import repository.account.AccountRepository;

import java.sql.Date;
import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountBuilder accountBuilder;

    public AccountServiceImpl(AccountRepository accountRepository, AccountBuilder accountBuilder) {
        this.accountRepository = accountRepository;
        this.accountBuilder=accountBuilder;
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account findById(Long id) throws EntityNotFoundException {
        return accountRepository.findById(id);
    }

    @Override
    public Notification<Boolean> save(String type, Integer balance, Long clientId, Date creation) {

        Account account=accountBuilder.setType(type).setBalance(balance).setCreation(creation).build();
        Validator accountValidator=new AccountValidator(account);

        boolean accountValid = accountValidator.validate();
        Notification<Boolean> accountAddingNotification = new Notification<>();

        if (!accountValid) {
            accountValidator.getErrors().forEach(accountAddingNotification::addError);
            accountAddingNotification.setResult(Boolean.FALSE);
        } else {
            accountAddingNotification.setResult(accountRepository.save(account,clientId));
        }
        return accountAddingNotification;
    }

    @Override
    public Notification<Boolean> update(Long id, String type, Integer balance, Date creation) {
        Account account=accountBuilder.setType(type).setBalance(balance).setCreation(creation).build();
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
    public Notification<Boolean> payBill(Long payerId,int sum) {
        Notification<Boolean> paymentNotification = new Notification<>();
        paymentNotification.setResult(false);

        try {
            Account payer = accountRepository.findById(payerId);

            int currentSenderBalance = payer.getBalance();
            if (currentSenderBalance - sum < 0)
                paymentNotification.addError("Not enough funds!");
            else {
                payer.setBalance(currentSenderBalance - sum);
                if (!accountRepository.update(payerId, payer)) {
                    paymentNotification.addError("Updating the payer could not be done! Bill not payed!");
                    payer.setBalance(currentSenderBalance + sum);
                }
                else
                    paymentNotification.setResult(true);
            }
        } catch (EntityNotFoundException e) {
            paymentNotification.addError("Account doesn't exist, possible database problems!");
        }
        return paymentNotification;
    }
    @Override
    public Notification<Boolean> transfer(Long receiverId,Long senderId, int sum)
    {
        Notification<Boolean> transferNotification=new Notification<>();
        transferNotification.setResult(false);

        try {
            Account sender=accountRepository.findById(senderId);

            int currentSenderBalance=sender.getBalance();
            if(currentSenderBalance-sum<0)
                transferNotification.addError("Not enough funds!");
            else
            {
                sender.setBalance(currentSenderBalance-sum);
                if(!accountRepository.update(senderId,sender)) {
                    transferNotification.addError("Updating the sender could not be done!");
                    sender.setBalance(currentSenderBalance + sum);
                }
                else
                {
                    Account receiver=accountRepository.findById(receiverId);
                    receiver.setBalance(receiver.getBalance()+sum);
                    if(!accountRepository.update(receiverId,receiver))
                    {
                        transferNotification.addError("Updating the receiver could not be done!");
                        receiver.setBalance(receiver.getBalance()-sum);
                        sender.setBalance(currentSenderBalance+sum);
                        accountRepository.update(senderId,sender);

                    }
                    else
                        transferNotification.setResult(true);

                }
            }

        } catch (EntityNotFoundException e) {
            transferNotification.addError("Accounts don't exist, possible database problems!");
        }
        return transferNotification;
    }
}
