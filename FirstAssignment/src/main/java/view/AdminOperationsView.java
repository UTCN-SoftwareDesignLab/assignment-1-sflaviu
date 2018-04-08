package view;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import static database.Constants.Operations.*;

public class AdminOperationsView extends JFrame {

    private JPanel contentPane;

    private List<JButton> btnsAdminOperations;

    public AdminOperationsView() {
        setTitle("Admin Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 620, 319);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnsAdminOperations=new ArrayList<>();

        JButton btnCrudClients= new JButton("CRUD clients");
        btnCrudClients.setBounds(10, 26, 174, 63);
        btnCrudClients.setActionCommand(CRUD_CLIENT);
        btnsAdminOperations.add(btnCrudClients);
        contentPane.add(btnCrudClients);

        JButton btnTransfer = new JButton("Transfer money");
        btnTransfer.setBounds(209, 26, 174, 63);
        btnTransfer.setActionCommand(TRANSFER_MONEY);
        btnsAdminOperations.add(btnTransfer);
        contentPane.add(btnTransfer);

        JButton btnPayBills= new JButton("Pay bills");
        btnPayBills.setBounds(407, 26, 174, 63);
        btnPayBills.setActionCommand(PAY_BILLS);
        btnsAdminOperations.add(btnPayBills);
        contentPane.add(btnPayBills);

        JButton btnManageEmployees = new JButton("Manage Employees");
        btnManageEmployees.setBounds(319, 138, 213, 63);
        btnManageEmployees.setActionCommand(CRUD_EMPLOYEE);
        btnsAdminOperations.add(btnManageEmployees);
        contentPane.add(btnManageEmployees);

        JButton btnCrudAccounts = new JButton("CRUD accounts");
        btnCrudAccounts.setBounds(62, 138, 213, 63);
        btnCrudAccounts.setActionCommand(CRUD_ACCOUNTS);
        btnsAdminOperations.add(btnCrudAccounts);
        contentPane.add(btnCrudAccounts);

    }
    public void setBtnListeners(ActionListener al)
    {
        for(JButton button:btnsAdminOperations)
            button.addActionListener(al);
    }
}
