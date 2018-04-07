package view;

import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BillView extends JFrame {

    private JPanel contentPane;
    private JTextField txtSum;

    private JLabel lblSum;
    private JScrollPane clientScrollPane;
    private JScrollPane accountScrollPane;

    private JRadioButton rdbtnElectricBill;
    private JRadioButton rdbtnInternetBill;
    private JButton btnPayBill;


    private JTable tableClients;
    private JTable tableAccounts;

    private ButtonGroup radioGroupBills;

	public BillView() {
		setTitle("Pay bill");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 616, 368);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		clientScrollPane = new JScrollPane();
		clientScrollPane.setBounds(24, 32, 251, 219);
		contentPane.add(clientScrollPane);
		
		accountScrollPane = new JScrollPane();
		accountScrollPane.setBounds(307, 32, 251, 219);
		contentPane.add(accountScrollPane);
		
		txtSum = new JTextField();
		txtSum.setBounds(89, 279, 136, 20);
		contentPane.add(txtSum);
		txtSum.setColumns(10);
		
		lblSum = new JLabel("Sum:");
		lblSum.setBounds(24, 282, 68, 14);
		contentPane.add(lblSum);
		
		rdbtnElectricBill = new JRadioButton("Electric Bill");
		rdbtnElectricBill.setBounds(286, 258, 143, 23);
        rdbtnElectricBill.setActionCommand("electricity");
		contentPane.add(rdbtnElectricBill);
		
		rdbtnInternetBill = new JRadioButton("Internet Bill");
		rdbtnInternetBill.setBounds(286, 299, 143, 23);
        rdbtnInternetBill.setActionCommand("internet");
		contentPane.add(rdbtnInternetBill);

        radioGroupBills=new ButtonGroup();
        radioGroupBills.add(rdbtnElectricBill);
        radioGroupBills.add(rdbtnInternetBill);
        rdbtnElectricBill.setSelected(true);
		
		btnPayBill = new JButton("Pay Bill");
		btnPayBill.setBounds(435, 278, 125, 23);
		contentPane.add(btnPayBill);
	}
	public String getTxtSum() {
		return txtSum.getText();
	}

	public String getSelectedBill()
	{
		return radioGroupBills.getSelection().getActionCommand();
	}
	public void setBtnPayBillListener(ActionListener al) {
		btnPayBill.addActionListener(al);
	}

    public void setTableClients(JTable clients)
    {
        this.tableClients=clients;
        clientScrollPane.setViewportView(clients);
        revalidate();
        repaint();
    }

    public void setTableAccounts(JTable accounts)
    {
        this.tableAccounts=accounts;
        accountScrollPane.setViewportView(accounts);
        revalidate();
        repaint();
    }

    public Long getSelectedAccountId()
    {
        return Long.parseLong((String)tableAccounts.getModel().getValueAt(tableAccounts.getSelectedRow(),0));
    }
    public String getSelectedClientName()
    {
        return (String)tableClients.getModel().getValueAt(tableClients.getSelectedRow(),1);
    }
    public String getSelectedClientId()
    {
        return (String)tableClients.getModel().getValueAt(tableClients.getSelectedRow(),0);
    }
}


