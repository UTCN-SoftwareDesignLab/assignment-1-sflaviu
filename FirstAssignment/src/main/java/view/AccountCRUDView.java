package view;

import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AccountCRUDView extends JFrame {

	private JPanel contentPane;
	private JTextField txtType;
	private JTextField txtBalance;
	private JTextField txtDate;
	private JTextField txtClientId;

	private JScrollPane scrollPane;

	private JTable tableAccounts;

	private JButton btnUpdateAccount;
    private JButton btnDisplayOwner;
    private JButton btnDeleteAccount;
    private JButton btnAddAccount;

	public AccountCRUDView() {
		setTitle("Account");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 622, 398);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 418, 243);
		contentPane.add(scrollPane);

		txtType = new JTextField();
		txtType.setBounds(10, 294, 127, 20);
		contentPane.add(txtType);
		txtType.setColumns(10);

		txtBalance = new JTextField();
		txtBalance.setBounds(147, 294, 86, 20);
		contentPane.add(txtBalance);
		txtBalance.setColumns(10);

		txtDate = new JTextField();
		txtDate.setBounds(258, 294, 136, 20);
		contentPane.add(txtDate);
		txtDate.setColumns(10);

        txtClientId = new JTextField();
        txtClientId.setBounds(438, 120, 86, 20);
        contentPane.add(txtClientId);
        txtClientId.setColumns(10);

		btnUpdateAccount = new JButton("Update Account");
		btnUpdateAccount.setBounds(438, 306, 158, 22);
		contentPane.add(btnUpdateAccount);

		btnDisplayOwner = new JButton("Display Owner");
        btnDisplayOwner .setBounds(438, 40, 158, 23);
		contentPane.add(btnDisplayOwner );

		btnDeleteAccount = new JButton("Delete Account");
		btnDeleteAccount.setBounds(438, 203, 158, 23);
		contentPane.add(btnDeleteAccount);

		btnAddAccount = new JButton("Add Account");
		btnAddAccount.setBounds(438, 257, 158, 23);
		contentPane.add(btnAddAccount);

        JLabel lblType = new JLabel("Type");
		lblType.setBounds(20, 269, 75, 14);
		contentPane.add(lblType);

        JLabel lblBalance = new JLabel("Balance");
		lblBalance.setBounds(147, 269, 75, 14);
		contentPane.add(lblBalance);

        JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(258, 269, 93, 14);
		contentPane.add(lblDate);

        JLabel txtOwnerCnp = new JLabel("Owner CNP");
        txtOwnerCnp.setBounds(478, 121, 94, 14);
        contentPane.add(txtOwnerCnp);
	}

    public String getTxtType() {
        return txtType.getText();
    }

    public String getTxtBalance() {
        return txtBalance.getText();
    }

    public String getTxtDate() {
        return txtDate.getText();
    }

    public void setAccountsTable(JTable accounts)
    {
        this.tableAccounts=accounts;
        scrollPane.setViewportView(accounts);
        repaint();
        revalidate();
    }
    public void setBtnUpdateAccountListener(ActionListener al) {
        btnUpdateAccount.addActionListener(al);
    }

    public void setBtnDeleteAccountListener(ActionListener al) {
        btnDeleteAccount.addActionListener(al);
    }
    public void setBtnAddAccountListener(ActionListener al) {
        btnAddAccount.addActionListener(al);
    }
    public void setBtnDisplayOwnerListener(ActionListener al) {
        btnDisplayOwner.addActionListener(al);
    }

    public int getSelectedAccount()
    {
        return tableAccounts.getSelectedRow();
    }
    public void updateTextBoxes(int row)
    {
        txtType.setText((String)tableAccounts.getModel().getValueAt(row,3));
        txtBalance.setText((String)tableAccounts.getModel().getValueAt(row,2));
        txtDate.setText((String)tableAccounts.getModel().getValueAt(row,1));
    }

    public String getTxtClientCnp()
    {
        return txtClientId.getText();
    }

    public Long getSelectedAccountId()
    {
        return Long.parseLong((String)tableAccounts.getModel().getValueAt(tableAccounts.getSelectedRow(),0));
    }
}
