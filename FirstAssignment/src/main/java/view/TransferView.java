package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TransferView extends JFrame {

    private JPanel contentPane;

    private JTextField txtSum;

    private JLabel lblSum;

    private JScrollPane receiverPane;
    private JScrollPane senderPane;

    private JTable tableReceiverAccounts;
    private JTable tableSenderAccounts;

    private JButton btnTransfer;

    public TransferView() {
        setTitle("Transfer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 543, 352);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        senderPane = new JScrollPane();
        senderPane.setBounds(10, 24, 232, 219);
        contentPane.add(senderPane);

        txtSum = new JTextField();
        txtSum.setColumns(10);
        txtSum.setBounds(137, 271, 121, 20);
        contentPane.add(txtSum);

        lblSum = new JLabel("Sum:");
        lblSum.setBounds(27, 274, 46, 14);
        contentPane.add(lblSum);

        receiverPane = new JScrollPane();
        receiverPane.setBounds(260, 24, 232, 219);
        contentPane.add(receiverPane);

        btnTransfer = new JButton("Transfer");
        btnTransfer.setBounds(320, 270, 144, 23);
        contentPane.add(btnTransfer);
    }
    public String getTxtSum() {
        return txtSum.getText();
    }

    public void setTableReceiverAccounts(JTable accounts)
    {
        this.tableReceiverAccounts=accounts;
        receiverPane.setViewportView(accounts);
        revalidate();
        repaint();
    }

    public void setTableSenderAccounts(JTable accounts)
    {
        this.tableSenderAccounts=accounts;
        senderPane.setViewportView(accounts);
        revalidate();
        repaint();
    }
    public Long getSelectedReceiverId()
    {
        return Long.parseLong((String)tableReceiverAccounts.getModel().getValueAt(tableReceiverAccounts.getSelectedRow(),0));
    }
    public Long getSelectedSenderId()
    {
        return Long.parseLong((String)tableSenderAccounts.getModel().getValueAt(tableSenderAccounts.getSelectedRow(),0));
    }

    public void setBtnTransferListener(ActionListener al)
    {
        btnTransfer.addActionListener(al);
    }

}

