/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Model.Customer;
import apiclient.CustomerApiClient;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class ManageCustomerFrame extends JFrame{
        private JButton addButton, editButton, deleteButton, clearButton;
        private JTextField customerCodeField, customerNameField, customerPhoneNumberField;
        private JTable customerTable;
        private DefaultTableModel tableModel;
        private CustomerApiClient customerApiClient;
        
        public ManageCustomerFrame()
        {
            super("Quản lý khách hàng");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(1500, 1000);
            this.customerApiClient = new CustomerApiClient();
            
            JPanel westPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            westPanel.add(new JLabel("Mã KH"), gbc);
            gbc.gridy++;
            westPanel.add(new JLabel("Tên KH"), gbc);
            gbc.gridy++;
            westPanel.add(new JLabel("SĐT KH"), gbc);
            gbc.gridy++;
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            customerCodeField = new JTextField(10);
            westPanel.add(customerCodeField, gbc);
            gbc.gridy++;
            customerNameField = new JTextField(10);
            westPanel.add(customerNameField, gbc);
            gbc.gridy++;
            customerPhoneNumberField = new JTextField(10);
            westPanel.add(customerPhoneNumberField, gbc);
            gbc.gridy++;
            
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            
            addButton = new JButton("Lưu");
        
            westPanel.add(addButton, gbc);

            editButton = new JButton("Sửa");
            editButton.setEnabled(false);
            gbc.gridy++;
            westPanel.add(editButton, gbc);

            deleteButton = new JButton("Xóa");
            deleteButton.setEnabled(false);
            gbc.gridy++;
            westPanel.add(deleteButton, gbc);

            clearButton = new JButton("Làm Sạch");
            gbc.gridy++;
            westPanel.add(clearButton, gbc);
            add(westPanel, BorderLayout.WEST);
            
            //Center
            tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tableModel.addColumn("Mã KH");
            tableModel.addColumn("Tên KH");
            tableModel.addColumn("SĐT KH");
            
            customerTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            JScrollPane scrollPane = new JScrollPane(customerTable);
            add(scrollPane, BorderLayout.CENTER);
            
            loadCustomerData();
            
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }
        
        private void loadCustomerData()
        {
            tableModel.setRowCount(0);
            try
            {
                List<Customer> customers = customerApiClient.getAllCustomers();
                if(customers != null)
                {
                    for(Customer customer : customers)
                    {
                        Object[] rowData = {
                            customer.getCustomerCode(),
                            customer.getName(),
                            customer.getPhoneNumber()
                        };
                        tableModel.addRow(rowData);
                    }
                }
                for(int i = 0; i<tableModel.getColumnCount(); i++)
                {
                    customerTable.getColumnModel().getColumn(i).setPreferredWidth(150);
                }
            }
            catch(IOException e)
                    {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this,"Không thể tải dữ liệu API");
                    }
        }

    public JButton getAddButton() {
        return addButton;
    }

    public void setAddButton(JButton addButton) {
        this.addButton = addButton;
    }

    public JButton getEditButton() {
        return editButton;
    }

    public void setEditButton(JButton editButton) {
        this.editButton = editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public void setClearButton(JButton clearButton) {
        this.clearButton = clearButton;
    }

    public JTextField getCustomerCodeField() {
        return customerCodeField;
    }

    public void setCustomerCodeField(JTextField customerCodeField) {
        this.customerCodeField = customerCodeField;
    }

    public JTextField getCustomerNameField() {
        return customerNameField;
    }

    public void setCustomerNameField(JTextField customerNameField) {
        this.customerNameField = customerNameField;
    }

    public JTextField getCustomerPhoneNumberField() {
        return customerPhoneNumberField;
    }

    public void setCustomerPhoneNumberField(JTextField customerPhoneNumberField) {
        this.customerPhoneNumberField = customerPhoneNumberField;
    }

    public JTable getCustomerTable() {
        return customerTable;
    }

    public void setCustomerTable(JTable customerTable) {
        this.customerTable = customerTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public CustomerApiClient getCustomerApiClient() {
        return customerApiClient;
    }

    public void setCustomerApiClient(CustomerApiClient customerApiClient) {
        this.customerApiClient = customerApiClient;
    }
        
}
