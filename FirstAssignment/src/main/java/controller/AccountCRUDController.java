package controller;

import model.Account;
import model.Activity;
import model.validation.Notification;
import service.account.AccountService;
import service.activity.ActivityService;
import view.AccountCRUDView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Map;
import java.util.List;


public class AccountCRUDController extends TableBasedController<Account> {

    private AccountCRUDView accountView;
    private Map<String,Controller> nextControllers;
    private AccountService accountService;
    private Long activeUserId;

    public AccountCRUDController(AccountCRUDView accountView, Map<String, Controller> nextControllers, AccountService accountService, ActivityService bigBrother) {

        super(bigBrother);
        this.accountView = accountView;
        this.nextControllers = nextControllers;
        this.accountService = accountService;

        accountView.setBtnUpdateAccountListener(new UpdateAccountButtonListener());
        accountView.setBtnAddAccountListener(new AddAccountButtonListener());
        accountView.setBtnDeleteAccountListener(new DeleteAccountButtonListener());
    }

    @Override
    public void openNextController(String next) {
        hideGUI();
        nextControllers.get(next).showGUI();
        nextControllers.get(next).setActiveUser(activeUserId);
    }

    @Override
    public void hideGUI() {
        accountView.setVisible(false);
    }

    @Override
    public void showGUI() {
        accountView.setVisible(true);
        populateAccountTable(accountService.findAll());

    }
    @Override
    public void setActiveUser(Long userId){this.activeUserId=userId;}

    private class AccountListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            accountView.updateTextBoxes(accountView.getSelectedAccount());
        }
    }

    private class DeleteAccountButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Long accountId;
            try {
                accountId=accountView.getSelectedAccountId();
                if(!accountService.remove(accountId))
                    JOptionPane.showMessageDialog(accountView.getContentPane(), "Deleting the account failed! Please try again later");
                else {
                    JOptionPane.showMessageDialog(accountView.getContentPane(), "Delete successful");
                    populateAccountTable(accountService.findAll());
                    logActivity("Delete Account",activeUserId,convertToSqlDate(new Date()),null,accountId);
                }
               }
            catch(IndexOutOfBoundsException ie)
            {
                JOptionPane.showMessageDialog(accountView.getContentPane(), "No row selected!");
            }
        }
    }
    private class AddAccountButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Notification<Boolean> inputNotification=checkInput();

            if(!inputNotification.hasErrors())
            {
                Date date=null;
                try {
                    date=new SimpleDateFormat("yyyy-MM-dd").parse(accountView.getTxtDate());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                Notification<Boolean> accountNotification =accountService.save(accountView.getTxtType(), Integer.parseInt(accountView.getTxtBalance()),accountView.getTxtClientCnp(),convertToSqlDate(date));

                if (accountNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(accountView.getContentPane(), accountNotification.getFormattedErrors());
                } else {
                    if (!accountNotification.getResult()) {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), "Adding the account was not successful, please try again later.");
                    } else {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), "Adding successful!");
                        populateAccountTable(accountService.findAll());

                       // logActivity("Create account",activeUserId,new Date(),accountNotification.getResult());
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(accountView.getContentPane(),inputNotification.getFormattedErrors());
            }
        }
    }

    private class UpdateAccountButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Notification<Boolean> inputNotification=checkInput();

                if (!inputNotification.hasErrors()) {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(accountView.getTxtDate());
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                    Notification<Boolean> accountNotification;

                    Long accountId = 0L;
                    try {
                        accountId = accountView.getSelectedAccountId();
                        accountNotification = accountService.update(accountId, accountView.getTxtType(), Integer.parseInt(accountView.getTxtBalance()), convertToSqlDate(date));

                    } catch (Exception ie) {
                        accountNotification = new Notification<>();
                        accountNotification.addError("No row selected!");
                    }

                    if (accountNotification.hasErrors()) {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), accountNotification.getFormattedErrors());
                    } else {
                        if (!accountNotification.getResult()) {
                            JOptionPane.showMessageDialog(accountView.getContentPane(), "Updating the account not successful, please try again later.");
                        } else {
                            JOptionPane.showMessageDialog(accountView.getContentPane(), "Updating successful!");
                            populateAccountTable(accountService.findAll());
                            logActivity("Update Account", activeUserId, convertToSqlDate(new Date()),null,accountId);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(accountView.getContentPane(), inputNotification.getFormattedErrors());
                }
        }
    }

    private Notification<Boolean> checkInput()
    {
        Notification<Boolean> inputNotification=new Notification<>();
        inputNotification.setResult(false);

        try {
                Integer.parseInt(accountView.getTxtBalance());
        }catch (NumberFormatException e) {
            inputNotification.addError("The balance is not a number!");
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            df.parse(accountView.getTxtDate());
        } catch (ParseException e) {
            inputNotification.addError("Date is not in the correct format ! yyyy-MM-dd");
        }

        return inputNotification;
    }
    private void populateAccountTable(List<Account> accounts)
    {
        if(accounts.size()>0) {
            JTable accountsTable = populateTable(accounts);
            accountsTable.getSelectionModel().addListSelectionListener(new AccountListSelectionListener());

            accountView.setAccountsTable(accountsTable);
        }
    }
}
