package controller;

import model.Account;
import model.Client;
import model.validation.Notification;
import service.account.AccountService;
import service.client.ClientService;
import view.BillView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class BillController extends TableBasedController{

    private BillView billView;
    private HashMap<String,Controller> nextControllers;
    private AccountService accountService;
    private ClientService clientService;

    private Long accountId;
    private String clientName;

    public BillController(BillView billView, HashMap<String, Controller> nextControllers, AccountService accountService, ClientService clientService) {
        this.billView = billView;
        this.nextControllers = nextControllers;
        this.accountService = accountService;
        this.clientService=clientService;

        accountId=null;
        clientName=null;

        billView.setBtnPayBillListener(new PayBillButtonListener());
    }


    @Override
    public void openNextController(String next) {
        JOptionPane.showMessageDialog(billView.getContentPane(), "This should implement the \"back\" functionality!");

    }

    @Override
    public void hideGUI() {
        billView.setVisible(false);
    }

    @Override
    public void showGUI() {
        populateClientsTables(clientService.findAll());
        billView.setVisible(true);

    }

    private class AccountsListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            accountId=billView.getSelectedAccountId();
        }
    }

    private class ClientListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            clientName=billView.getSelectedClientName();

            List<Account> listAccounts=accountService.findClientsAccounts(Long.parseLong(billView.getSelectedClientId()));
            if(listAccounts.isEmpty())
                JOptionPane.showMessageDialog(billView.getContentPane(), "Selected client has no active accounts!");
            else
                populateAccountsTables(listAccounts);
        }
    }

    private class PayBillButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Notification<Boolean> inputNotification=checkInput();
            if(!inputNotification.hasErrors())
            {
                Notification<Boolean> paymentNotification = accountService.subtract(accountId,Integer.parseInt(billView.getTxtSum()));
                if (paymentNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(billView.getContentPane(), paymentNotification.getFormattedErrors());
                } else {
                    if (!paymentNotification.getResult()) {
                        JOptionPane.showMessageDialog(billView.getContentPane(), "Unable to pay bill, please try again later.");
                    } else {
                        paymentComplete();
                        populateAccountsTables(accountService.findAll());
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(billView.getContentPane(),inputNotification.getFormattedErrors());
            }

        }
    }

    private void paymentComplete()
    {
        JOptionPane.showMessageDialog(billView.getContentPane(),clientName+" has successfully payed " +billView.getTxtSum()+ " dollars to his " +billView.getSelectedBill()+ " provider.\nThis sum will be transferred to his provider immediately! ");
        //inform provider
    }

    private Notification<Boolean> checkInput()
    {
        Notification<Boolean> inputNotification=new Notification<>();
        inputNotification.setResult(false);

        if(accountId==null)
            inputNotification.addError("No account selected!");

        if(clientName==null)
            inputNotification.addError("No client selected!");

        int sum=0;
        try {
            sum = Integer.parseInt(billView.getTxtSum());
        }catch (NumberFormatException e) {
            inputNotification.addError("The inputed amount is not a number!");
        }
        if(sum<=0)
            inputNotification.addError("The payment amount must be positive");

        if(!inputNotification.hasErrors())
            inputNotification.setResult(true);

        return inputNotification;
    }

    private void populateClientsTables(List<Client> clientsList)
    {

        JTable clientsTable=populateTable(clientsList);

        clientsTable.getSelectionModel().addListSelectionListener(new ClientListSelectionListener());

        billView.setTableClients(clientsTable);
    }

    private void populateAccountsTables(List<Account> accountsList)
    {

        JTable accountsTable=populateTable(accountsList);

        accountsTable.getSelectionModel().addListSelectionListener(new AccountsListSelectionListener());

        billView.setTableAccounts(accountsTable);
    }
}
