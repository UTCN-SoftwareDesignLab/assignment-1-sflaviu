package controller;

import view.UserOperationsView;
import view.UserOperationsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class UserOperationsController implements Controller {
    private UserOperationsView userView;
    private Map<String,Controller> nextControllers;
    private Long activeUserId;

    public UserOperationsController(UserOperationsView userView, Map<String,Controller> nextControllers) {
        this.userView = userView;
        this.nextControllers=nextControllers;

        userView.setBtnListeners(new OperationsListener());
    }

    private class OperationsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            openNextController(e.getActionCommand());
        }
    }

    @Override
    public void openNextController(String next) {
        nextControllers.get(next).showGUI();
        nextControllers.get(next).setActiveUser(activeUserId);
    }

    @Override
    public void hideGUI() {
        userView.setVisible(false);
    }

    @Override
    public void showGUI() { userView.setVisible(true); }

    @Override
    public void setActiveUser(Long userId){this.activeUserId=userId;}
}
