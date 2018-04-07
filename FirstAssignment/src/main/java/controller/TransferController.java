package controller;

import model.Account;
import model.validation.Notification;
import service.account.AccountService;
import view.TransferView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TransferController implements Controller{

    private TransferView transferView;
    private Map<String,Controller> nextControllers;
    private AccountService accountService;

    private Long receiverId;
    private Long senderId;

    public TransferController(TransferView transferView, Map<String, Controller> nextControllers, AccountService accountService) {
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
                Notification<Boolean> transferNotification = accountService.transfer(receiverId,senderId,Integer.parseInt(transferView.getTxtSum()));
                if (transferNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(transferView.getContentPane(), transferNotification.getFormattedErrors());
                } else {
                    if (!transferNotification.getResult()) {
                        JOptionPane.showMessageDialog(transferView.getContentPane(), "Unable to transfer money, please try again later.");
                    } else {
                        JOptionPane.showMessageDialog(transferView.getContentPane(), "Transfer successful!");
                        populateAccountsTables(accountService.findAll());
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
        if(sum<=0)
            inputNotification.addError("The sum must be positive");

        if(!inputNotification.hasErrors())
            inputNotification.setResult(true);

        return inputNotification;
    }

    private void populateAccountsTables(List<Account> accountsList)
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

        JTable receiverTable=new JTable(tableData,firstRow.toArray());
        JTable senderTable=new JTable(tableData,firstRow.toArray());

        senderTable.getSelectionModel().addListSelectionListener(new SenderListSelectionListener());
        receiverTable.getSelectionModel().addListSelectionListener(new ReceiverListSelectionListener() );

        transferView.setTableReceiverAccounts(receiverTable);
        transferView.setTableSenderAccounts(senderTable);
    }

}
