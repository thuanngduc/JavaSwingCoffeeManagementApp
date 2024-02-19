/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Model.Customer;
import Model.Order;
import apiclient.OrderApiClient;
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
public class ManageOrderFrame extends JFrame{
    private JButton deleteButton, printPDFButton, detailButton;
    private JTextField idField, customerCodeField, orderDateField, totalAmountField;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private OrderApiClient orderApiClient;
    
    public ManageOrderFrame()
    {
        super("Quản lý hóa đơn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 1000);
        this.orderApiClient = new OrderApiClient();
        
        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        deleteButton = new JButton("Xóa hóa đơn");
        deleteButton.setEnabled(false);
        northPanel.add(deleteButton, gbc);
        gbc.gridx++;
        
        detailButton = new JButton("Chi tiết hóa đơn");
        detailButton.setEnabled(false);
        northPanel.add(detailButton, gbc);
        gbc.gridx++;
        
        printPDFButton = new JButton("In hóa đơn ra PDF");
        printPDFButton.setEnabled(false);
        northPanel.add(printPDFButton, gbc);
        gbc.gridx++;
        
        add(northPanel, BorderLayout.NORTH);
        
        //West
        JPanel westPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        westPanel.add(new JLabel("ID:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Ngày lập HĐ:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Tổng Tiền:"), gbc);
        gbc.gridy++;
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idField = new JTextField(10);
        westPanel.add(idField, gbc);
        gbc.gridy++;
        idField.setEditable(false);
        customerCodeField = new JTextField(10);
        westPanel.add(customerCodeField, gbc);
        customerCodeField.setEditable(false);
        gbc.gridy++;
        orderDateField = new JTextField(10);
        westPanel.add(orderDateField, gbc);
        orderDateField.setEditable(false);
        gbc.gridy++;
        totalAmountField = new JTextField(10);
        westPanel.add(totalAmountField, gbc);
        totalAmountField.setEditable(false);
        gbc.gridy++;
        
        add(westPanel, BorderLayout.WEST);
        // Center
        tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        tableModel.addColumn("ID");
        tableModel.addColumn("Mã KH");
        tableModel.addColumn("Ngày Lập HĐ");
        tableModel.addColumn("Tổng tiền");
        
        orderTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        
        loadOrderData();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void loadOrderData()
    {
        tableModel.setRowCount(0);
        try
        {
            List<Order> orders = orderApiClient.getAllOrders();
            if (orders != null) {
                for (Order order : orders) {
                    Customer customer = order.getCustomer();
                    String customerCode = "null";
                    if(customer != null)
                    {
                      customerCode = customer.getCustomerCode();  
                    }
                     
                    Object[] rowData = {
                            order.getId(),
                            customerCode,
                            order.getOrderDate(),
                            order.getTotalAmount()
                            
                    };
                    tableModel.addRow(rowData);
                }
            }
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                orderTable.getColumnModel().getColumn(i).setPreferredWidth(150); // Đặt giá trị tùy chỉnh theo nhu cầu
            }
        }catch(IOException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(JButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public JButton getPrintPDFButton() {
        return printPDFButton;
    }

    public void setPrintPDFButton(JButton printPDFButton) {
        this.printPDFButton = printPDFButton;
    }

    public JTable getOrderTable() {
        return orderTable;
    }

    public void setOrderTable(JTable orderTable) {
        this.orderTable = orderTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public OrderApiClient getOrderApiClient() {
        return orderApiClient;
    }

    public void setOrderApiClient(OrderApiClient orderApiClient) {
        this.orderApiClient = orderApiClient;
    }

    public JTextField getIdField() {
        return idField;
    }

    public void setIdField(JTextField idField) {
        this.idField = idField;
    }

    public JTextField getCustomerCodeField() {
        return customerCodeField;
    }

    public void setCustomerCodeField(JTextField customerCodeField) {
        this.customerCodeField = customerCodeField;
    }

    public JTextField getOrderDateField() {
        return orderDateField;
    }

    public void setOrderDateField(JTextField orderDateField) {
        this.orderDateField = orderDateField;
    }

    public JTextField getTotalAmountField() {
        return totalAmountField;
    }

    public void setTotalAmountField(JTextField totalAmountField) {
        this.totalAmountField = totalAmountField;
    }

    public JButton getDetailButton() {
        return detailButton;
    }

    public void setDetailButton(JButton detailButton) {
        this.detailButton = detailButton;
    }
    
    
    
}
