package controller;

import model.User;
import model.validation.Notification;
import service.activity.ActivityService;
import service.user.UserService;
import view.UserCRUDView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.*;

public class UserCRUDController extends TableBasedController<User> {

    private UserCRUDView userView;
    private Map<String,Controller> nextControllers;
    private UserService userService;
    public Long activeUserId;

    public UserCRUDController(UserCRUDView userView, Map<String, Controller> nextControllers, UserService userService, ActivityService bigBrother) {
        super(bigBrother);
        this.userView = userView;
        this.nextControllers = nextControllers;
        this.userService = userService;

        userView.setBtnAddEmployeeListener(new AddUserButtonListener());
        userView.setBtnUpdateEmployeeListener(new UpdateUserButtonListener());
        userView.setBtnDeleteEmployeeListener(new DeleteUserButtonListener());

    }


    @Override
    public void openNextController(String next) {
        JOptionPane.showMessageDialog(userView.getContentPane(), "This should implement the \"back\" functionality!");

    }

    @Override
    public void hideGUI() {
        userView.setVisible(false);
    }

    @Override
    public void showGUI() {
        populateUsersTable(userService.findAll());
        userView.setVisible(true);
    }

    private class DeleteUserButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Long userId=userView.getSelectedUserId();

            if(!userService.remove(userId))
                JOptionPane.showMessageDialog(userView.getContentPane(), "Deleting the user failed! Please try again later");
            else {
                JOptionPane.showMessageDialog(userView.getContentPane(), "Delete succesfull");

            }
        }
    }
    private class AddUserButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> userNotification=userService.save(userView.getTxtUsername(),userView.getTxtPassword());
            if (userNotification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), userNotification.getFormattedErrors());
            } else {
                if (!userNotification.getResult()) {
                    JOptionPane.showMessageDialog(userView.getContentPane(), "Adding the user was not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(userView.getContentPane(), "Adding successful!");
                    populateUsersTable(userService.findAll());
                }
            }
        }
    }

    private class UpdateUserButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Long userId=userView.getSelectedUserId();
            Notification<Boolean> userNotification=userService.update(userId,userView.getTxtUsername(),userView.getTxtPassword());
            if (userNotification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), userNotification.getFormattedErrors());
            } else {
                if (!userNotification.getResult()) {
                    JOptionPane.showMessageDialog(userView.getContentPane(), "Updating the user not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(userView.getContentPane(), "Updating successful!");
                    populateUsersTable(userService.findAll());
                    logActivity("Updated user ",activeUserId,convertToSqlDate(new Date()),null,userId);

                }
            }
        }
    }

    private class UserListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            userView.updateTextBoxes(userView.getSelectedUser());
        }
    }
    private void populateUsersTable(List<User> usersList)
    {
        if(usersList.size()>0) {
            JTable usersTable = populateTable(usersList);

            usersTable.getSelectionModel().addListSelectionListener(new UserListSelectionListener());

            userView.setUsersTable(usersTable);
        }
    }
    @Override
    public void setActiveUser(Long userId){this.activeUserId=userId;}
}
