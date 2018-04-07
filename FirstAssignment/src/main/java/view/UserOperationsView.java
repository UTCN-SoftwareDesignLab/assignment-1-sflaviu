package view;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Operations.*;


public class UserOperationsView extends JFrame {

    private JPanel contentPane;

    private List<JButton> btnsUserOperations;

    public UserOperationsView() {
        setTitle("User operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 624, 228);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnsUserOperations=new ArrayList<>();

        JButton btnCrudClients = new JButton("CRUD clients");
        btnCrudClients.setBounds(17, 52, 185, 63);
        btnCrudClients.setActionCommand(CRUD_CLIENT);
        btnsUserOperations.add(btnCrudClients);
        contentPane.add(btnCrudClients);

        JButton btnTransfer = new JButton("Transfer money");
        btnTransfer.setBounds(212, 52, 188, 63);
        btnTransfer.setActionCommand(TRANSFER_MONEY);
        btnsUserOperations.add(btnTransfer);
        contentPane.add(btnTransfer);

        JButton btnPayBills = new JButton("Pay bills");
        btnPayBills.setBounds(410, 52, 188, 63);
        btnPayBills.setActionCommand(PAY_BILLS);
        btnsUserOperations.add(btnPayBills);
        contentPane.add(btnPayBills);
    }

    public void setBtnListeners(ActionListener al)
    {
        for(JButton button:btnsUserOperations)
            button.addActionListener(al);
    }

}
