/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class SaleFrame extends JFrame{
    private JTextField idOrderField, customerCodeField, totalAmountField, idProductField,
            quantityField, nameProductField, itemPriceField;
    private JButton confirmButton, addCodeCustomerButton, addProductIdButton, editProductButton, deleteProductButton,addProductToTableButton;
    private JLabel showImage;
    JTable selectedProductTable;
    DefaultTableModel tableModel;
    
    public SaleFrame(){
        super("Tạo Hóa Đơn Bán Hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 1000);
        
        
        JPanel northPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        northPanel.add(new JLabel("Mã HD:"), gbc);
        gbc.gridx++;
        idOrderField = new JTextField(10);
        idOrderField.setEnabled(false);
        northPanel.add(idOrderField, gbc);
        gbc.gridx++;
        gbc.gridx++;
        northPanel.add(new JLabel("Mã KH"));
        customerCodeField = new JTextField(10);
        customerCodeField.setEnabled(false);
        northPanel.add(customerCodeField, gbc);
        gbc.gridx++;
        addCodeCustomerButton = new JButton("...");
        northPanel.add(addCodeCustomerButton, gbc);
        gbc.gridx++;
        gbc.gridx++;
        gbc.gridy++;
        northPanel.add(new JLabel("Tổng tiền"), gbc);
        gbc.gridx++;
        totalAmountField = new JTextField(10);
        totalAmountField.setEnabled(false);
        northPanel.add(totalAmountField, gbc);
        gbc.gridx++;
        confirmButton = new JButton("Xác nhận");
        confirmButton.setEnabled(false);
        northPanel.add(confirmButton, gbc);
        
        add(northPanel, BorderLayout.NORTH);
        
        
        // west
        JPanel westPanel = new JPanel(new GridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        showImage = new JLabel("");
        westPanel.add(showImage, gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Mã SP"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Tên sản phẩm"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Đơn giá"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Số lượng"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idProductField = new JTextField(10);
        idProductField.setEnabled(false);
        westPanel.add(idProductField, gbc);
        gbc.gridy++;
        
        nameProductField = new JTextField(10);
        nameProductField.setEnabled(false);
        westPanel.add(nameProductField, gbc);
        gbc.gridy++;
        
        itemPriceField = new JTextField(10);
        itemPriceField.setEnabled(false);
        westPanel.add(itemPriceField, gbc);
        gbc.gridy++;
        
        quantityField = new JTextField(10);
        westPanel.add(quantityField, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        addProductIdButton = new JButton("..");
        westPanel.add(addProductIdButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        
        addProductToTableButton = new JButton("Thêm");
//        addProductToTableButton.setEnabled(false);
        westPanel.add(addProductToTableButton, gbc);

        editProductButton = new JButton("Sửa");
        editProductButton.setEnabled(false);
        gbc.gridy++;
        westPanel.add(editProductButton, gbc);
        
        deleteProductButton = new JButton("Xóa");
        deleteProductButton.setEnabled(false);
        gbc.gridy++;
        westPanel.add(deleteProductButton, gbc);
        
        add(westPanel, BorderLayout.WEST);

        // center
        
        tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        tableModel.addColumn("Mã SP");
        tableModel.addColumn("Tên");
        tableModel.addColumn("Đơn giá");
        tableModel.addColumn("Số lượng");
        tableModel.addColumn("Thành tiền");
        
        selectedProductTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        JScrollPane scrollPane = new JScrollPane(selectedProductTable);
        add(scrollPane, BorderLayout.CENTER);
        
        
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

    public JTextField getIdOrderField() {
        return idOrderField;
    }

    public void setIdOrderField(JTextField idOrderField) {
        this.idOrderField = idOrderField;
    }

    public JTextField getCustomerCodeField() {
        return customerCodeField;
    }

    public void setCustomerCodeField(JTextField customerCodeField) {
        this.customerCodeField = customerCodeField;
    }

    public JTextField getTotalAmountField() {
        return totalAmountField;
    }

    public void setTotalAmountField(JTextField totalAmountField) {
        this.totalAmountField = totalAmountField;
    }

    public JTextField getIdProductField() {
        return idProductField;
    }

    public void setIdProductField(JTextField idProductField) {
        this.idProductField = idProductField;
    }

    public JTextField getQuantityField() {
        return quantityField;
    }

    public void setQuantityField(JTextField quantityField) {
        this.quantityField = quantityField;
    }

    public JTextField getNameProductField() {
        return nameProductField;
    }

    public void setNameProductField(JTextField nameProductField) {
        this.nameProductField = nameProductField;
    }

    public JTextField getItemPriceField() {
        return itemPriceField;
    }

    public void setItemPriceField(JTextField itemPriceField) {
        this.itemPriceField = itemPriceField;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public void setConfirmButton(JButton confirmButton) {
        this.confirmButton = confirmButton;
    }

    public JButton getAddCodeCustomerButton() {
        return addCodeCustomerButton;
    }

    public void setAddCodeCustomerButton(JButton addCodeCustomerButton) {
        this.addCodeCustomerButton = addCodeCustomerButton;
    }

    public JButton getAddProductIdButton() {
        return addProductIdButton;
    }

    public void setAddProductIdButton(JButton addProductIdButton) {
        this.addProductIdButton = addProductIdButton;
    }

    public JButton getEditProductButton() {
        return editProductButton;
    }

    public void setEditProductButton(JButton editProductButton) {
        this.editProductButton = editProductButton;
    }

    public JButton getDeleteProductButton() {
        return deleteProductButton;
    }

    public void setDeleteProductButton(JButton deleteProductButton) {
        this.deleteProductButton = deleteProductButton;
    }

    public JLabel getShowImage() {
        return showImage;
    }

    public void setShowImage(JLabel showImage) {
        this.showImage = showImage;
    }

    public JTable getSelectedProductTable() {
        return selectedProductTable;
    }

    public void setSelectedProductTable(JTable selectedProductTable) {
        this.selectedProductTable = selectedProductTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public JButton getAddProductToTableButton() {
        return addProductToTableButton;
    }

    public void setAddProductToTableButton(JButton addProductToTableButton) {
        this.addProductToTableButton = addProductToTableButton;
    }
    
}
