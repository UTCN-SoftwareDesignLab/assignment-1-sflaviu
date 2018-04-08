package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserCRUDView extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JLabel lblUsername;
    private JLabel lblPassword;

    private  JScrollPane scrollPane;

    private JTable tableUsers;

    private JButton btnAddEmployee;
    private JButton btnDeleteEmployee;
    private JButton btnUpdateEmployee;
    private JButton btnGenerateReport;


    public UserCRUDView() {
        setTitle("Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 620, 354);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(31, 11, 359, 204);
        contentPane.add(scrollPane);

        txtUsername = new JTextField();
        txtUsername.setColumns(10);
        txtUsername.setBounds(31, 262, 149, 20);
        contentPane.add(txtUsername);

        txtPassword = new JTextField();
        txtPassword.setColumns(10);
        txtPassword.setBounds(208, 262, 156, 20);
        contentPane.add(txtPassword);

        btnUpdateEmployee = new JButton("Update Employee");
        btnUpdateEmployee.setBounds(401, 287, 185, 22);
        contentPane.add(btnUpdateEmployee);

        btnAddEmployee = new JButton("Add Employee");
        btnAddEmployee.setBounds(401, 236, 185, 23);
        contentPane.add(btnAddEmployee);

        btnDeleteEmployee = new JButton("Delete Employee");
        btnDeleteEmployee.setBounds(401, 203, 185, 23);
        contentPane.add(btnDeleteEmployee);


        lblUsername = new JLabel("Username");
        lblUsername.setBounds(31, 237, 87, 14);
        contentPane.add(lblUsername);

        lblPassword = new JLabel("Password");
        lblPassword.setBounds(208, 240, 97, 14);
        contentPane.add(lblPassword);
    }

    public void updateTextBoxes(int row)
    {
        txtUsername.setText((String)tableUsers.getModel().getValueAt(row,2));
    }
    public Long getSelectedUserId()
    {
        return Long.parseLong((String)tableUsers.getModel().getValueAt(tableUsers.getSelectedRow(),0));
    }
    public int getSelectedUser()
    {
        return tableUsers.getSelectedRow();
    }
    public void setUsersTable(JTable users)
    {
        tableUsers=users;
        scrollPane.setViewportView(tableUsers);
        revalidate();
        repaint();
    }

    public String getTxtUsername() {
        return txtUsername.getText();
    }

    public String getTxtPassword() {
        return txtPassword.getText();
    }

    public void setBtnAddEmployeeListener(ActionListener al)
    {
        btnAddEmployee.addActionListener(al);
    }
    public void setBtnDeleteEmployeeListener(ActionListener al)
    {
        btnDeleteEmployee.addActionListener(al);
    }
    public void setBtnUpdateEmployeeListener(ActionListener al)
    {
        btnUpdateEmployee.addActionListener(al);
    }
}
