package view;

import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientCRUDView extends JFrame {

	private JPanel contentPane;

	private JTextField txtName;
	private JTextField txtCnp;
	private JTextField txtCardId;
	private JTextField txtAddress;

	private JScrollPane scrollPane;

    private JButton btnAddClient;
    private JButton btnUpdateClient;
    private JButton btnFindAccountsFor;

    private JTable tableClients;

    /**
	 * Create the frame.
	 */
	public ClientCRUDView() {
		setTitle("Client");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 632, 401);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane(tableClients);
        scrollPane.setBounds(21, 24, 428, 204);
		contentPane.add(scrollPane);
		
		txtName = new JTextField();
        txtName.setBounds(21, 275, 86, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtCnp = new JTextField();
        txtCnp.setBounds(117, 275, 111, 20);
		contentPane.add(txtCnp);
		txtCnp.setColumns(10);
		
		txtCardId = new JTextField();
        txtCardId.setBounds(238, 275, 115, 20);
		contentPane.add(txtCardId);
		txtCardId.setColumns(10);
		
		txtAddress = new JTextField();
        txtAddress.setBounds(357, 275, 92, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);

		btnAddClient = new JButton("Add Client");
        btnAddClient.setBounds(459, 254, 147, 23);
		contentPane.add(btnAddClient);
		
		btnUpdateClient = new JButton("Update Client");
        btnUpdateClient.setBounds(459, 299, 147, 22);
		contentPane.add(btnUpdateClient);
		
		btnFindAccountsFor = new JButton("Find Accounts");
        btnFindAccountsFor.setBounds(459, 35, 147, 23);
		contentPane.add(btnFindAccountsFor);

        JLabel lblName = new JLabel("Name");
        lblName.setBounds(21, 250, 68, 14);
        contentPane.add(lblName);

        JLabel lblCnp = new JLabel("Cnp");
        lblCnp.setBounds(117, 250, 78, 14);
        contentPane.add(lblCnp);

        JLabel lblCardId = new JLabel("Card ID");
        lblCardId.setBounds(238, 250, 68, 14);
        contentPane.add(lblCardId);

        JLabel lblAddress = new JLabel("Address");
        lblAddress.setBounds(357, 250, 57, 14);
        contentPane.add(lblAddress);
	}


    public String getTxtName() {
        return txtName.getText();
    }

    public String getTxtCnp() {
        return txtCnp.getText();
    }

    public String getTxtCardId() {
        return txtCardId.getText();
    }

    public String getTxtAddress() {
        return txtAddress.getText();
    }

    public void setClientsTable(JTable clients)
    {
        tableClients=clients;
        scrollPane.setViewportView(tableClients);
		revalidate();
        repaint();
    }

    public void setBtnUpdateClientListener(ActionListener al) {
        btnUpdateClient.addActionListener(al);
    }
    public void setBtnAddClientListener(ActionListener al) {
        btnAddClient.addActionListener(al);
    }
    public void setBtnFindAccountsForListener(ActionListener al) {
        btnFindAccountsFor.addActionListener(al);
    }

    public int getSelectedClient()
    {
        return tableClients.getSelectedRow();
    }
    public void updateTextBoxes(int row)
    {
        txtName.setText((String)tableClients.getModel().getValueAt(row,1));
        txtCnp.setText((String)tableClients.getModel().getValueAt(row,2));
        txtCardId.setText((String)tableClients.getModel().getValueAt(row,3));
        txtAddress.setText((String)tableClients.getModel().getValueAt(row,4));
    }

    public Long getSelectedClientId()
    {
        return Long.parseLong((String)tableClients.getModel().getValueAt(tableClients.getSelectedRow(),0));
    }

}
