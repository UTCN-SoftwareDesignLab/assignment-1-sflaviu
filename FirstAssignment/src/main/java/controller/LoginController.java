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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;

/**
 * Created by Alex on 18/03/2017.
 */
public class LoginController implements Controller {
    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    private Map<String,Controller> nextControllers;
    private Long activeUserId;


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

                    activeUserId=loginNotification.getResult().getId();
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
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration successful! Test your credentials by logging in!");
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
        for(Controller c:nextControllers.values())
            c.hideGUI();
        loginView.emptyPassword();
        loginView.emptyUsername();
        loginView.setVisible(true);
        activeUserId=null;

    }
    public void setActiveUser(Long userId){this.activeUserId=userId;}

    public void openNextController(String next)
    {
        hideGUI();
        nextControllers.get(next).showGUI();
        nextControllers.get(next).setActiveUser(activeUserId);
    }

    private String getRole(User user)
    {
        for(Role r:user.getRoles())
            if(r.getRole().equals(ADMINISTRATOR))
                return ADMINISTRATOR;
        return user.getRoles().get(0).getRole();
    }


}
