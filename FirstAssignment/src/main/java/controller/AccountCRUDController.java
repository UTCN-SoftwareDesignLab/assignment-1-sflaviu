package controller;

import model.Account;
import model.validation.Notification;
import service.account.AccountService;
import view.AccountCRUDView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.List;


public class AccountCRUDController implements Controller {

    private AccountCRUDView accountView;
    private Map<String,Controller> nextControllers;
    private AccountService accountService;

    public AccountCRUDController(AccountCRUDView accountView, Map<String, Controller> nextControllers, AccountService accountService) {
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

    private class AccountListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            accountView.updateTextBoxes(accountView.getSelectedAccount());
        }
    }

    private class DeleteAccountButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Long accountId=accountView.getSelectedAccountId();

            if(!accountService.remove(accountId))
                JOptionPane.showMessageDialog(accountView.getContentPane(), "Deleting the account failed! Please try again later");
            else
                JOptionPane.showMessageDialog(accountView.getContentPane(), "Delete succesfull");
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
                    date=(Date)new SimpleDateFormat("dd/MM/yyyy").parse(accountView.getTxtDate());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                accountService.save(accountView.getTxtType(), Integer.parseInt(accountView.getTxtBalance()),Long.parseLong(accountView.getTxtClientId()),date);
                Notification<Boolean> accountNotification = new Notification<>();
                if (accountNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(accountView.getContentPane(), accountNotification.getFormattedErrors());
                } else {
                    if (!accountNotification.getResult()) {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), "Adding the account was not successful, please try again later.");
                    } else {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), "Adding successful!");
                        populateAccountTable(accountService.findAll());
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

            if(!inputNotification.hasErrors()) {
                Date date = null;
                try {
                    date = (Date) new SimpleDateFormat("dd/MM/yyyy").parse(accountView.getTxtDate());
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                Notification<Boolean> accountNotification = accountService.update(accountView.getSelectedAccountId(), accountView.getTxtType(), Integer.parseInt(accountView.getTxtBalance()), date);
                if (accountNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(accountView.getContentPane(), accountNotification.getFormattedErrors());
                } else {
                    if (!accountNotification.getResult()) {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), "Updating the account not successful, please try again later.");
                    } else {
                        JOptionPane.showMessageDialog(accountView.getContentPane(), "Updating successful!");
                        populateAccountTable(accountService.findAll());
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(accountView.getContentPane(),inputNotification.getFormattedErrors());
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

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            df.parse(accountView.getTxtDate());
        } catch (ParseException e) {
            inputNotification.addError("Date is not in the correct format ! dd/MM/yyyy");
        }


        return inputNotification;
    }
    private void populateAccountTable(List<Account> accountsList)
    {
        String[][] tableData;

        ArrayList<String> firstRow=new ArrayList<>();

        int row=0;
        for(Field f: accountsList.get(0).getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(!Collection.class.isAssignableFrom(f.getType()))
            {
                firstRow.add(f.getName());
                row++;
            }
        }
        tableData=new String[accountsList.size()][row];

        int column;
        row=0;
        for(Account c:accountsList) {
            column = 0;
            for (Field f : c.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                if(!Collection.class.isAssignableFrom(f.getType()))
                    try {
                        tableData[row][column] = f.get(c).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                column++;
            }
            row++;
        }
        JTable table=new JTable(tableData,firstRow.toArray());

        table.getSelectionModel().addListSelectionListener(new AccountListSelectionListener());

        accountView.setAccountsTable(table);
    }
}
