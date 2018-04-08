package controller;

import model.Activity;
import model.User;
import service.activity.ActivityService;
import service.user.UserService;
import view.ReportView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.List;

public class ReportController extends TableBasedController<User> {

    private ReportView reportView;
    private UserService userService;
    private Map<String,Controller> nextControllers;
    private Long activeUserId;

    public ReportController(ReportView reportView, Map<String, Controller> nextControllers,UserService userService,ActivityService bigBrother ) {
        super(bigBrother);
        this.reportView = reportView;
        this.nextControllers = nextControllers;

        this.userService=userService;

        reportView.setBtnGenerateReportListener(new ReportButtonListener());

    }

    @Override
    public void openNextController(String next) {
        JOptionPane.showMessageDialog(reportView.getContentPane(), "This should implement the \"back\" functionality!");

    }

    @Override
    public void hideGUI(){reportView.setVisible(false);
    }

    @Override
    public void showGUI() {
        populateUsersTable(userService.findAll());
        reportView.setVisible(true);
    }

    private class ReportButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Long userId=0L;
            try {
                userId = reportView.getSelectedUserId();
            }catch(IndexOutOfBoundsException e)
            {
                JOptionPane.showMessageDialog(reportView.getContentPane(), "No user selected!");
            }

            List<Activity> activities=bigBrother.findByPerformer(userId);
            createReport(activities,reportView.getSelectedUserName());
        }
    }

    private void createReport(List<Activity> activities,String clientName)
    {
        try {
            PrintWriter writer = new PrintWriter("report-"+clientName, "UTF-8");
            writer.write(clientName+"'s report on "+new Date().toString()+"\n\n");

            for(Activity a:activities)
            {
                writer.write(a.toString());
                writer.write("\n\n");
            }
            writer.close();
            JOptionPane.showMessageDialog(reportView.getContentPane(), "Report generated successfully!\nCheck your local folder.");

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            JOptionPane.showMessageDialog(reportView.getContentPane(), "Can't generate report!");
        }
    }

    private void populateUsersTable(List<User> usersList)
    {
        if(usersList.size()>0) {
            JTable usersTable = populateTable(usersList);
            reportView.setUsersTable(usersTable);
        }
    }
    @Override
    public void setActiveUser(Long userId){this.activeUserId=userId;}

}
