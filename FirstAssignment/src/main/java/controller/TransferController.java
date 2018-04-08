package controller;

import model.Account;
import model.validation.Notification;
import service.account.AccountService;
import service.activity.ActivityService;
import view.TransferView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.*;

public class TransferController extends TableBasedController<Account>{

    private TransferView transferView;
    private Map<String,Controller> nextControllers;
    private AccountService accountService;

    private Long receiverId;
    private Long senderId;

    private Long activeUserId;

    public TransferController(TransferView transferView, Map<String, Controller> nextControllers, AccountService accountService, ActivityService bigBrother) {

        super(bigBrother);
        this.transferView = transferView;
        this.nextControllers = nextControllers;
        this.accountService = accountService;

        receiverId=null;
        senderId=null;

        transferView.setBtnTransferListener(new TransferButtonListener());

    }

    @Override
    public void openNextController(String next) {
        JOptionPane.showMessageDialog(transferView.getContentPane(), "This should be the \"back\" functionality!");

    }

    @Override
    public void hideGUI() {
        transferView.setVisible(false);
    }

    @Override
    public void showGUI() {
        populateAccountsTables(accountService.findAll());
        transferView.setVisible(true);

    }

    @Override
    public void setActiveUser(Long userId){this.activeUserId=userId;}

    private class ReceiverListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            receiverId=transferView.getSelectedReceiverId();
        }
    }

    private class SenderListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            senderId=transferView.getSelectedSenderId();
        }
    }

    private class TransferButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Notification<Boolean> inputNotification=checkInput();
            if(!inputNotification.hasErrors())
            {
                int sum=Integer.parseInt(transferView.getTxtSum());
                Notification<Boolean> transferNotification = accountService.transfer(receiverId,senderId,sum);
                if (transferNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(transferView.getContentPane(), transferNotification.getFormattedErrors());
                } else {
                    if (!transferNotification.getResult()) {
                        JOptionPane.showMessageDialog(transferView.getContentPane(), "Unable to transfer money, please try again later.");
                    } else {
                        JOptionPane.showMessageDialog(transferView.getContentPane(), "Transfer successful!");
                        populateAccountsTables(accountService.findAll());
                        logActivity("Transfered "+sum+" dollars.",activeUserId,convertToSqlDate(new Date()),null,senderId);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(transferView.getContentPane(),inputNotification.getFormattedErrors());
            }

        }
    }

    private Notification<Boolean> checkInput()
    {
        Notification<Boolean> inputNotification=new Notification<>();
        inputNotification.setResult(false);

        if(receiverId==null)
            inputNotification.addError("No receiver selected!");

        if(senderId==null)
            inputNotification.addError("No sender selected!");

        int sum=0;
        try {
             sum = Integer.parseInt(transferView.getTxtSum());
        }catch (NumberFormatException e) {
            inputNotification.addError("The inputed sum is not a number!");
        }

        if(!inputNotification.hasErrors())
            inputNotification.setResult(true);

        return inputNotification;
    }

    private void populateAccountsTables(List<Account> accountsList)
    {
        if(accountsList.size()>0) {
            JTable receiverTable = populateTable(accountsList);
            JTable senderTable = populateTable(accountsList);

            senderTable.getSelectionModel().addListSelectionListener(new SenderListSelectionListener());
            receiverTable.getSelectionModel().addListSelectionListener(new ReceiverListSelectionListener());

            transferView.setTableReceiverAccounts(receiverTable);
            transferView.setTableSenderAccounts(senderTable);
        }
    }

}
