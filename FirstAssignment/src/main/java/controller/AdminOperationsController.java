package controller;

import view.AdminOperationsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class AdminOperationsController implements Controller {

    private AdminOperationsView adminView;
    private Map<String,Controller> nextControllers;
    private Long activeUserId;

    public AdminOperationsController(AdminOperationsView adminView, Map<String,Controller> nextControllers) {
        this.adminView = adminView;
        this.nextControllers=nextControllers;

        adminView.setBtnListeners(new OperationsListener());
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
        adminView.setVisible(false);
    }

    @Override
    public void showGUI() { adminView.setVisible(true); }

    @Override
    public void setActiveUser(Long userId){this.activeUserId=userId;}
}
