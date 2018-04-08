package view;

import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AccountCRUDView extends JFrame {

	private JPanel contentPane;
	private JTextField txtType;
	private JTextField txtBalance;
	private JTextField txtDate;
	private JTextField txtClientCnp;

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
		txtType.setBounds(10, 294, 160, 20);
		contentPane.add(txtType);
		txtType.setColumns(10);

		txtBalance = new JTextField();
		txtBalance.setBounds(180, 294, 101, 20);
		contentPane.add(txtBalance);
		txtBalance.setColumns(10);

		txtDate = new JTextField();
        txtDate.setBounds(291, 294, 137, 20);
		contentPane.add(txtDate);
		txtDate.setColumns(10);

        txtClientCnp = new JTextField();
        txtClientCnp.setBounds(438, 147, 158, 20);
        contentPane.add(txtClientCnp);
        txtClientCnp.setColumns(10);

		btnUpdateAccount = new JButton("Update Account");
		btnUpdateAccount.setBounds(438, 306, 158, 22);
		contentPane.add(btnUpdateAccount);

		btnDisplayOwner = new JButton("Display Owner");
        btnDisplayOwner .setBounds(438, 40, 158, 23);
		contentPane.add(btnDisplayOwner );

		btnDeleteAccount = new JButton("Delete Account");
		btnDeleteAccount.setBounds(438, 272, 158, 23);
		contentPane.add(btnDeleteAccount);

		btnAddAccount = new JButton("Add Account");
		btnAddAccount.setBounds(438, 178, 158, 23);
		contentPane.add(btnAddAccount);

        JLabel lblType = new JLabel("Type");
		lblType.setBounds(20, 269, 75, 14);
		contentPane.add(lblType);

        JLabel lblBalance = new JLabel("Balance");
        lblBalance.setBounds(180, 269, 75, 14);
		contentPane.add(lblBalance);

        JLabel lblDate = new JLabel("Date");
        lblDate.setBounds(281, 269, 93, 14);
		contentPane.add(lblDate);

        JLabel lblOwnerCnp = new JLabel("Owner CNP");
        lblOwnerCnp.setBounds(485, 122, 94, 14);
        contentPane.add(lblOwnerCnp);
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
        return txtClientCnp.getText();
    }

    public Long getSelectedAccountId()
    {
        return Long.parseLong((String)tableAccounts.getModel().getValueAt(tableAccounts.getSelectedRow(),0));
    }
}
