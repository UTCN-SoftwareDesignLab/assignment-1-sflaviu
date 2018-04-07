package controller;

import model.User;
import model.validation.Notification;
import service.user.UserService;
import view.UserCRUDView;

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

public class UserCRUDController implements Controller {

    private UserCRUDView userView;
    private Map<String,Controller> nextControllers;
    private UserService userService;

    public UserCRUDController(UserCRUDView userView, Map<String, Controller> nextControllers, UserService userService) {
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
            else
                JOptionPane.showMessageDialog(userView.getContentPane(), "Delete succesfull");
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
            Notification<Boolean> userNotification=userService.update(userView.getSelectedUserId(),userView.getTxtUsername(),userView.getTxtPassword());
            if (userNotification.hasErrors()) {
                JOptionPane.showMessageDialog(userView.getContentPane(), userNotification.getFormattedErrors());
            } else {
                if (!userNotification.getResult()) {
                    JOptionPane.showMessageDialog(userView.getContentPane(), "Updating the user not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(userView.getContentPane(), "Updating successful!");
                    populateUsersTable(userService.findAll());
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
        String[][] tableData;

        ArrayList<String> firstRow=new ArrayList<>();

        int row=0;
        for(Field f: usersList.get(0).getClass().getDeclaredFields()) {
            f.setAccessible(true);
            if(!Collection.class.isAssignableFrom(f.getType()))
            {
                firstRow.add(f.getName());
                row++;
            }
        }
        tableData=new String[usersList.size()][row];

        int column;
        row=0;
        for(User c:usersList) {
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

        JTable usersTable=new JTable(tableData,firstRow.toArray());

        usersTable.getSelectionModel().addListSelectionListener(new UserListSelectionListener());

        userView.setUsersTable(usersTable);
    }
}
