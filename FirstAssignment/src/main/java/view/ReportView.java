package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;

import static database.Constants.Operations.GENERATE_REPORT;

public class ReportView extends JFrame {

    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JButton btnReport;

    private JTable tableUsers;

    public ReportView() {
        setTitle("Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 636, 356);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 29, 382, 236);
        contentPane.add(scrollPane);

        btnReport = new JButton("Generate Report");
        btnReport.setActionCommand(GENERATE_REPORT);
        btnReport.setBounds(402, 109, 185, 23);
        contentPane.add(btnReport);
    }
    public Long getSelectedUserId()
    {
        return Long.parseLong((String)tableUsers.getModel().getValueAt(tableUsers.getSelectedRow(),0));
    }
    public String getSelectedUserName()
    {
        return (String)tableUsers.getModel().getValueAt(tableUsers.getSelectedRow(),2);
    }
    public void setUsersTable(JTable users)
    {
        tableUsers=users;
        scrollPane.setViewportView(tableUsers);
        revalidate();
        repaint();
    }
    public void setBtnGenerateReportListener(ActionListener al)
    {
        btnReport.addActionListener(al);
    }
}