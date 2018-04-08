package controller;

import model.Client;
import model.validation.Notification;
import service.client.ClientService;
import view.ClientCRUDView;

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

public class ClientCRUDController extends TableBasedController<Client> {

    private ClientCRUDView clientView;
    private Map<String,Controller> nextControllers;
    private ClientService clientService;

    public ClientCRUDController(ClientCRUDView clientView, ClientService clientService, Map<String, Controller> nextControllers ) {
        this.clientView = clientView;
        this.nextControllers=nextControllers;
        this.clientService=clientService;

        clientView.setBtnAddClientListener(new AddClientButtonListener());
        clientView.setBtnUpdateClientListener(new UpdateClientButtonListener());
    }

    @Override
    public void openNextController(String next) {
        hideGUI();
        nextControllers.get(next).showGUI();
    }

    @Override
    public void hideGUI() {
        clientView.setVisible(false);
    }

    @Override
    public void showGUI() {
        populateClientTable(clientService.findAll());
        clientView.setVisible(true);

    }

    private class ClientListSelectionListener implements ListSelectionListener  {
        public void valueChanged(ListSelectionEvent e) {
           clientView.updateTextBoxes(clientView.getSelectedClient());

        }
    }
    private class AddClientButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> clientNotification=clientService.save(clientView.getTxtName(),clientView.getTxtCnp(),clientView.getTxtCardId(),clientView.getTxtAddress());
            if (clientNotification.hasErrors()) {
                JOptionPane.showMessageDialog(clientView.getContentPane(), clientNotification.getFormattedErrors());
            } else {
                if (!clientNotification.getResult()) {
                    JOptionPane.showMessageDialog(clientView.getContentPane(), "Adding the client not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(clientView.getContentPane(), "Adding successful!");
                    populateClientTable(clientService.findAll());
                }
            }
        }
    }

    private class UpdateClientButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> clientNotification=clientService.update(clientView.getSelectedClientId(),clientView.getTxtName(),clientView.getTxtCnp(),clientView.getTxtCardId(),clientView.getTxtAddress());
            if (clientNotification.hasErrors()) {
                JOptionPane.showMessageDialog(clientView.getContentPane(), clientNotification.getFormattedErrors());
            } else {
                if (!clientNotification.getResult()) {
                    JOptionPane.showMessageDialog(clientView.getContentPane(), "Updating the client not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(clientView.getContentPane(), "Updating successful!");
                    populateClientTable(clientService.findAll());
                }
            }
        }
    }
    private void populateClientTable(List<Client> clientList)
    {
        JTable clientsTable=populateTable(clientList);

        clientsTable.getSelectionModel().addListSelectionListener(new ClientListSelectionListener());

        clientView.setClientsTable(clientsTable);
    }
}

