package com.mybank.tui;

import com.mybank.data.DataSource;
import com.mybank.domain.Account;
import com.mybank.domain.Bank;
import com.mybank.domain.CheckingAccount;
import com.mybank.domain.Customer;
import com.mybank.domain.SavingsAccount;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MatisseDemo extends JFrame {

    private JComboBox<String> clientComboBox;
    private JButton showButton;
    private JButton reportButton;
    private JButton aboutButton;
    private JTextPane logTextPane;
    private JLabel instructionLabel;
    private JScrollPane logPane;

    public MatisseDemo() {
        initComponents();
        loadCustomersFromFile("test.dat");
        populateComboBox();
    }

    private void initComponents() {
        clientComboBox = new JComboBox<>();
        showButton = new JButton();
        reportButton = new JButton();
        aboutButton = new JButton();
        logTextPane = new JTextPane();
        logPane = new JScrollPane();
        instructionLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("MyBank Clients");
        setResizable(false);

        showButton.setText("Show");
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                showButtonActionPerformed(evt);
            }
        });

        reportButton.setText("Report");
        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });

        aboutButton.setText("About");
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aboutButtonActionPerformed(evt);
            }
        });

        logTextPane.setEditable(false);
        logPane.setViewportView(logTextPane);

        instructionLabel.setText("Choose a client name and press 'Show' button");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(logPane)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(clientComboBox, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(showButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(reportButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(aboutButton)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(instructionLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(clientComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(showButton)
                                        .addComponent(reportButton)
                                        .addComponent(aboutButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(logPane, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(instructionLabel)
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void showButtonActionPerformed(ActionEvent evt) {
        displayCustomerInfo();
    }

    private void reportButtonActionPerformed(ActionEvent evt) {
        generateReport();
    }

    private void aboutButtonActionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "MyBank Client Application\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadCustomersFromFile(String filename) {
        DataSource dataSource = new DataSource(filename);
        try {
            dataSource.loadData();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void populateComboBox() {
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer customer = Bank.getCustomer(i);
            clientComboBox.addItem(customer.getLastName() + ", " + customer.getFirstName());
        }
    }

    private void displayCustomerInfo() {
        int index = clientComboBox.getSelectedIndex();
        if (index < 0) return;

        Customer customer = Bank.getCustomer(index);
        StringBuilder info = new StringBuilder();
        info.append(customer.getFirstName()).append(" ").append(customer.getLastName())
                .append(", customer #").append(index + 1).append("\n")
                .append("-------------------------\n")
                .append("Accounts:\n");

        for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
            Account account = customer.getAccount(i);
            if (account instanceof CheckingAccount) {
                CheckingAccount ca = (CheckingAccount) account;
                info.append("#").append(i).append(" - Checking: $").append(ca.getBalance())
                        .append(", overdraft: $").append("Not available").append("\n");
            } else if (account instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) account;
                info.append("#").append(i).append(" - Savings: $").append(sa.getBalance())
                        .append(", interest rate: ").append("Not available").append("\n");
            }
        }

        logTextPane.setText(info.toString());
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Bank Clients Report\n")
                .append("====================\n")
                .append("Total number of clients: ").append(Bank.getNumberOfCustomers()).append("\n");

        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer customer = Bank.getCustomer(i);
            report.append("\nCustomer: ").append(customer.getLastName()).append(", ").append(customer.getFirstName()).append("\n");

            for (int j = 0; j < customer.getNumberOfAccounts(); j++) {
                Account account = customer.getAccount(j);
                if (account instanceof CheckingAccount) {
                    report.append("    Checking Account: current balance is ").append(account.getBalance()).append("\n");
                } else if (account instanceof SavingsAccount) {
                    report.append("    Savings Account: current balance is ").append(account.getBalance()).append("\n");
                }
            }
        }

        logTextPane.setText(report.toString());
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MatisseDemo().setVisible(true);
            }
        });
    }
}
