package controller;

import model.Role;
import model.User;
import model.validation.Notification;
import repository.user.AuthenticationException;
import service.user.AuthenticationService;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import static database.Constants.Roles.EMPLOYEE;

/**
 * Created by Alex on 18/03/2017.
 */
public class LoginController implements Controller {
    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    private Map<String,Controller> nextControllers;

    public LoginController(LoginView loginView, AuthenticationService authenticationService,HashMap<String,Controller> nextControllers) {

        this.loginView = loginView;
        this.authenticationService = authenticationService;

        loginView.setLoginButtonListener(new LoginButtonListener());
        loginView.setRegisterButtonListener(new RegisterButtonListener());

        this.nextControllers=nextControllers;
    }

    private class LoginButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = null;
            try {
                loginNotification = authenticationService.login(username, password);
            } catch (AuthenticationException e1) {
                e1.printStackTrace();
            }

            if (loginNotification != null) {
                if (loginNotification.hasErrors()) {
                    JOptionPane.showMessageDialog(loginView.getContentPane(), loginNotification.getFormattedErrors());
                } else {
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Login successful!");
                    String role=getRole(loginNotification.getResult());
                    openNextController(role);
                }
            }
        }
    }

    private class RegisterButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);
            if (registerNotification.hasErrors()) {
                JOptionPane.showMessageDialog(loginView.getContentPane(), registerNotification.getFormattedErrors());
            } else {
                if (!registerNotification.getResult()) {
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration successful!");
                    openNextController(EMPLOYEE);
                }
            }
        }
    }

    public void hideGUI()
    {
        loginView.setVisible(false);
    }
    public void showGUI()
    {
        loginView.setVisible(true);
    }
    public void openNextController(String next)
    {
        hideGUI();
        nextControllers.get(next).showGUI();
    }

    private String getRole(User user)
    {
        System.out.println( user.getRoles().get(0).getRole());
        return user.getRoles().get(0).getRole();
    }


}
