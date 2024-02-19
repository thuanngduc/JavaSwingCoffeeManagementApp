package ui;

import Model.Product;
import apiclient.ProductApiClient;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class ManageProductFrame extends JFrame {
    private JButton addButton, editButton, deleteButton, clearButton;
    private JTextField idField, nameField, descriptionField, priceField, imageUrlField;
    private JComboBox<String> typeField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductApiClient productApiClient;

    

    public ManageProductFrame() {
        super("Quản lý Sản Phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 1000);
        this.productApiClient = new ProductApiClient();

        // Tạo panel chứa các components
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        westPanel.add(new JLabel("ID:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Tên Sản Phẩm:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Mô Tả Sản Phẩm:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Giá sản phẩm:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Loại Sản phẩm"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Link ảnh:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idField = new JTextField(10);
        westPanel.add(idField, gbc);
        gbc.gridy++;
        idField.setEditable(false);
        nameField = new JTextField(10);
        westPanel.add(nameField, gbc);
        gbc.gridy++;
        descriptionField = new JTextField(10);
        westPanel.add(descriptionField, gbc);
        gbc.gridy++;
        priceField = new JTextField(10);
        westPanel.add(priceField, gbc);
        gbc.gridy++;
        
        String[] categories = {"DRINKS", "DESSERTS"};
        typeField = new JComboBox<>(categories);
        westPanel.add(typeField, gbc);
        gbc.gridy++;
        imageUrlField = new JTextField(10);
        westPanel.add(imageUrlField, gbc);
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
        
        // Phần Center
        tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        tableModel.addColumn("ID");
        tableModel.addColumn("Tên");
        tableModel.addColumn("Mô Tả");
        tableModel.addColumn("Giá");
        tableModel.addColumn("Loại");
        tableModel.addColumn("Link ảnh");
        
        productTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        loadProductData();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void loadProductData() {
        // Xóa các cột cũ trong tableModel
        tableModel.setRowCount(0);

        try {
            List<Product> products = productApiClient.getAllProducts();
            if (products != null) {
                for (Product product : products) {
                    Object[] rowData = {
                            product.getId(),
                            product.getItemName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getProductType(),
                            product.getImageUrl()
                    };
                    tableModel.addRow(rowData);
                }
            }

            // Tự động điều chỉnh kích thước cột
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                productTable.getColumnModel().getColumn(i).setPreferredWidth(150); // Đặt giá trị tùy chỉnh theo nhu cầu
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
    }   

    public JButton getClearButton() {
        return clearButton;
    }

    public void setClearButton(JButton clearButton) {
        this.clearButton = clearButton;
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

    public JTextField getIdField() {
        return idField;
    }

    public void setIdField(JTextField idField) {
        this.idField = idField;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public void setNameField(JTextField nameField) {
        this.nameField = nameField;
    }

    public JTextField getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(JTextField descriptionField) {
        this.descriptionField = descriptionField;
    }

    public JTextField getPriceField() {
        return priceField;
    }

    public void setPriceField(JTextField priceField) {
        this.priceField = priceField;
    }

    public JTextField getImageUrlField() {
        return imageUrlField;
    }

    public void setImageUrlField(JTextField imageUrlField) {
        this.imageUrlField = imageUrlField;
    }

    public JComboBox<String> getTypeField() {
        return typeField;
    }

    public void setTypeField(JComboBox<String> typeField) {
        this.typeField = typeField;
    }

    public JTable getProductTable() {
        return productTable;
    }

    public void setProductTable(JTable productTable) {
        this.productTable = productTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

}
