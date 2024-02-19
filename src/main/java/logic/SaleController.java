/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import Model.Customer;
import Model.Order;
import Model.Product;
import apiclient.CustomerApiClient;
import apiclient.OrderApiClient;
import apiclient.ProductApiClient;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import ui.SaleFrame;

/**
 *
 * @author ADMIN
 */
public class SaleController extends JFrame {
    private SaleFrame saleFrame;
    private OrderApiClient orderApiClient;
    private ProductApiClient productApiClient;
    private CustomerApiClient customerApiClient;

    public SaleController(SaleFrame saleFrame, OrderApiClient orderApiClient, ProductApiClient productApiClient, CustomerApiClient customerApiClient) {
        this.saleFrame = saleFrame;
        this.orderApiClient = orderApiClient;
        this.productApiClient = productApiClient;
        this.customerApiClient = customerApiClient;
    }
    public void initialize()
    {
        saleFrame.getConfirmButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOrder();
            }
        });
        saleFrame.getAddCodeCustomerButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCustomerDialog();
            }
        });
        saleFrame.getAddProductIdButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openProductDialog();
            }
        });
        saleFrame.getAddProductToTableButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToTable();
            }
        });
        saleFrame.getSelectedProductTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = saleFrame.getSelectedProductTable().getSelectedRow();
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn và hiển thị trong các trường
                    showProductDetails(selectedRow);
                    // Enable nút Edit và Delete
                    saleFrame.getEditProductButton().setEnabled(true);
                    saleFrame.getDeleteProductButton().setEnabled(true);
                } else {
                    // Nếu không có hàng nào được chọn, disable nút Edit và Delete
                    saleFrame.getEditProductButton().setEnabled(false);
                    saleFrame.getDeleteProductButton().setEnabled(false);
                }
            }
        });
        saleFrame.getEditProductButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSeletectedProductTable();
            }
        });
        saleFrame.getDeleteProductButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedProductTable();
            }
        });
        
    }
    private void saveOrder()
    {
        String customerCode = saleFrame.getCustomerCodeField().getText();

        DefaultTableModel model = (DefaultTableModel) saleFrame.getSelectedProductTable().getModel();
        int rowCount = model.getRowCount();
        
//        if (rowCount > 0) {
//        int confirmResult = JOptionPane.showConfirmDialog(this,
//                "Do you want to save the order?" + "?? " + rowCount + "??",
//                "Confirmation",
//                JOptionPane.YES_NO_OPTION);
        if (rowCount > 0) {
            Map<String, Object> requestData = new HashMap<>();
            if (!customerCode.equals("") && !customerCode.isEmpty()) {
                requestData.put("customerCode", customerCode);
            }
            Map<Long, Integer> productQuantities = new HashMap<>();
            for (int i = 0; i < rowCount; i++) {
                Long productId = (Long) saleFrame.getSelectedProductTable().getValueAt(i, 0);
                int quantity = (Integer) saleFrame.getSelectedProductTable().getValueAt(i, 3);
                productQuantities.put(productId, quantity);
            }
            requestData.put("productQuantities", productQuantities);

            try {
                Order createdOrder = orderApiClient.createOrder(requestData);
                if (createdOrder != null) {
                    JOptionPane.showMessageDialog(this, "Order created successfully. Order ID: " + createdOrder.getId());
                    // Xóa thông tin trong table sau khi tạo đơn hàng thành công
                    model.setRowCount(0);
                } else {
                    JOptionPane.showMessageDialog(this, "Tạo Order thất bại.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi không lấy được dữ liệu qua API");
            }
        }

    }
    private void openCustomerDialog() {
        JDialog dialog = new JDialog(this, "Customer Dialog", true);
        dialog.setLayout(new BorderLayout());

        // Khu vực Border WEST
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        westPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Tên KH"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("SĐT"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField customerCodeField = new JTextField(10);
        customerCodeField.setEnabled(false);
        westPanel.add(customerCodeField, gbc);
        gbc.gridy++;

        JTextField nameField = new JTextField(10);
        nameField.setEnabled(false);
        westPanel.add(nameField, gbc);
        gbc.gridy++;

        JTextField phoneNumberField = new JTextField(10);
        phoneNumberField.setEnabled(false);
        westPanel.add(phoneNumberField, gbc);

        dialog.add(westPanel, BorderLayout.WEST);

        // Khu vực Border CENTER
        DefaultTableModel tableModel;
        tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        tableModel.addColumn("Mã KH");
        tableModel.addColumn("Tên KH");
        tableModel.addColumn("Sdt KH");
        JTable tableCustomer;
        tableCustomer = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

        dialog.add(new JScrollPane(tableCustomer), BorderLayout.CENTER);

        try {
            List<Customer> customers = customerApiClient.getAllCustomers();
            if (customers != null) {
                for (Customer customer : customers) {
                    Object[] rowData = {
                        customer.getCustomerCode(),
                        customer.getName(),
                        customer.getPhoneNumber()
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
        tableCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lấy dòng được chọn
                int selectedRow = tableCustomer.getSelectedRow();

                // Lấy thông tin từ dòng được chọn
                String selectedCustomerCode = tableCustomer.getValueAt(selectedRow, 0).toString();
                String selectedName = tableCustomer.getValueAt(selectedRow, 1).toString();
                String selectedPhoneNumber = tableCustomer.getValueAt(selectedRow, 2).toString();

                // Đặt thông tin vào các JTextField
                customerCodeField.setText(selectedCustomerCode);
                nameField.setText(selectedName);
                phoneNumberField.setText(selectedPhoneNumber);
            }
        });

        // Tạo nút OK để đóng cửa sổ con
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saleFrame.getCustomerCodeField().setText(customerCodeField.getText());
                dialog.dispose();
            }
        });
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void openProductDialog()
    {
        JDialog dialog = new JDialog(this, "Product Dialog", true);
        dialog.setLayout(new BorderLayout());

        // Khu vực Border WEST
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        westPanel.add(new JLabel("ID:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Tên"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Giá"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Link ảnh"), gbc);
        gbc.gridy++;

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idProductField = new JTextField(10);
        idProductField.setEnabled(false);
        westPanel.add(idProductField, gbc);
        gbc.gridy++;

        JTextField nameField = new JTextField(10);
        nameField.setEnabled(false);
        westPanel.add(nameField, gbc);
        gbc.gridy++;

        JTextField priceProductField = new JTextField(10);
        priceProductField.setEnabled(false);
        westPanel.add(priceProductField, gbc);
        
        JTextField linkUrlImage = new JTextField(10);
        linkUrlImage.setEnabled(false);
        westPanel.add(linkUrlImage, gbc);

        dialog.add(westPanel, BorderLayout.WEST);

        // Khu vực Border CENTER
        DefaultTableModel tableModel;
        tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        tableModel.addColumn("ID");
        tableModel.addColumn("Tên");
        tableModel.addColumn("Giá");
        tableModel.addColumn("Url ảnh");

        JTable tableProduct;
        tableProduct = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        
        

        dialog.add(new JScrollPane(tableProduct), BorderLayout.CENTER);

        try {
            List<Product> products = productApiClient.getAllProducts();
            if (products != null) {
                for (Product product : products) {
                    Object[] rowData = {
                        product.getId(),
                        product.getItemName(),
                        product.getPrice(),
                        product.getImageUrl()
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
        tableProduct.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Lấy dòng được chọn
                int selectedRow = tableProduct.getSelectedRow();

                // Lấy thông tin từ dòng được chọn
                Long selectedId = (Long) tableProduct.getValueAt(selectedRow, 0);
                String selectedName = tableProduct.getValueAt(selectedRow, 1).toString();
                Double selectedPrice =(Double) tableProduct.getValueAt(selectedRow, 2);
                String selectedUrl = tableProduct.getValueAt(selectedRow, 3).toString();


                // Đặt thông tin vào các JTextField
                idProductField.setText(String.valueOf(selectedId));
                nameField.setText(selectedName);
                priceProductField.setText(String.valueOf(selectedPrice));
                linkUrlImage.setText(selectedUrl);
            }
        });

        // Tạo nút OK để đóng cửa sổ con
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saleFrame.getIdProductField().setText(idProductField.getText());
                saleFrame.getNameProductField().setText(nameField.getText());
                saleFrame.getItemPriceField().setText(priceProductField.getText());
                updateImage(linkUrlImage.getText().toString());
                dialog.dispose();
            }
        });
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void updateImage(String imageURL) {
        try {
            // Kiểm tra nếu imageURL không trống
            if (!imageURL.isEmpty()) {
                // Tạo ImageIcon từ URL mới
                ImageIcon newImageIcon = new ImageIcon(new URL(imageURL));
                Image image = newImageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT);
                ImageIcon imageIcon = new ImageIcon(image);
                // Cập nhật hình ảnh trên JLabel
                saleFrame.getShowImage().setIcon(imageIcon);
                // Cập nhật UI
                revalidate();
                repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void addToTable()
    {
        String quantityText = saleFrame.getQuantityField().getText();
        if ((saleFrame.getIdProductField().getText().equals("")) || (saleFrame.getQuantityField().getText().equals(""))) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đủ thông tin");
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity > 0 && quantity < 100) {
                // Số nguyên dương lớn hơn 0 và nhỏ hơn 100
                // Thực hiện các thao tác cần thiết sau khi kiểm tra thành công
            } else {
                JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương lớn hơn 0 và nhỏ hơn 100");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên");
        }
        Long idItem = (Long) Long.parseLong(saleFrame.getIdProductField().getText());
        String nameItem = (String) saleFrame.getNameProductField().getText();
        Double priceItem = (Double) Double.parseDouble(saleFrame.getItemPriceField().getText());
        int quantity = (Integer) Integer.parseInt(saleFrame.getQuantityField().getText());
        
        double amount = priceItem * quantity;
        
        Object[] rowData = {
          idItem,
          nameItem,
          priceItem,
          quantity,
          amount
        };
        saleFrame.getTableModel().addRow(rowData);
        
        saleFrame.getIdProductField().setText("");
        saleFrame.getNameProductField().setText("");
        saleFrame.getItemPriceField().setText("");
        saleFrame.getQuantityField().setText("");
        
        saleFrame.getTotalAmountField().setText(String.valueOf(calculateTotalAmount()));
        
        if(saleFrame.getSelectedProductTable().getModel().getRowCount() >0)
        {
             saleFrame.getConfirmButton().setEnabled(true);
        }
    }
    private void showProductDetails(int rowIndex)
    {
        Long id = (Long) saleFrame.getSelectedProductTable().getValueAt(rowIndex, 0);
        String itemName = (String) saleFrame.getSelectedProductTable().getValueAt(rowIndex, 1);
        Double itemPrice = (Double) saleFrame.getSelectedProductTable().getValueAt(rowIndex, 2);
        int quantity = (Integer) saleFrame.getSelectedProductTable().getValueAt(rowIndex, 3);
        
        Product product;
        try {
            product = productApiClient.getProductById(id);
             updateImage(product.getImageUrl());

        } catch (IOException ex) {
            Logger.getLogger(SaleController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Lấy giá trị từ cột "Loại" và chuyển đổi sang chuỗi
        

        // Hiển thị thông tin trong các trường
        saleFrame.getIdProductField().setText(String.valueOf(id));
        saleFrame.getNameProductField().setText(itemName);
        saleFrame.getItemPriceField().setText(String.valueOf(itemPrice));
        saleFrame.getQuantityField().setText(String.valueOf(quantity));

    }
    private void editSeletectedProductTable()
    {
        int selectedRowIndex = saleFrame.getSelectedProductTable().getSelectedRow();
        String quantityText = saleFrame.getQuantityField().getText();
        if ((saleFrame.getIdProductField().getText().equals("")) || (saleFrame.getQuantityField().getText().equals(""))) {
            JOptionPane.showMessageDialog(this, "Hãy nhập đủ thông tin");
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityText);
            if (quantity > 0 && quantity < 100) {
                // Số nguyên dương lớn hơn 0 và nhỏ hơn 100
                // Thực hiện các thao tác cần thiết sau khi kiểm tra thành công
            } else {
                JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương lớn hơn 0 và nhỏ hơn 100");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên");
        }
        Long idItem = (Long) Long.parseLong(saleFrame.getIdProductField().getText());
        String nameItem = (String) saleFrame.getNameProductField().getText();
        Double priceItem = (Double) Double.parseDouble(saleFrame.getItemPriceField().getText());
        int quantity = (Integer) Integer.parseInt(saleFrame.getQuantityField().getText());
        
        double amount = priceItem * quantity;
        saleFrame.getSelectedProductTable().setValueAt(idItem, selectedRowIndex, 0);
        saleFrame.getSelectedProductTable().setValueAt(nameItem, selectedRowIndex, 1);
        saleFrame.getSelectedProductTable().setValueAt(priceItem, selectedRowIndex, 2);
        saleFrame.getSelectedProductTable().setValueAt(quantity, selectedRowIndex, 3);
        saleFrame.getSelectedProductTable().setValueAt(amount, selectedRowIndex, 4);

        saleFrame.getIdProductField().setText("");
        saleFrame.getNameProductField().setText("");
        saleFrame.getItemPriceField().setText("");
        saleFrame.getQuantityField().setText("");
        
    }
    private void deleteSelectedProductTable() {
        int selectedRowIndex = saleFrame.getSelectedProductTable().getSelectedRow();
        showProductDetails(selectedRowIndex);
        
        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sản phẩm để xóa");
            return;
        }

        // Lấy thông tin sản phẩm được chọn từ bảng
        Long idToDelete = (Long) saleFrame.getSelectedProductTable().getValueAt(selectedRowIndex, 0);
        String itemNameToDelete = (String) saleFrame.getSelectedProductTable().getValueAt(selectedRowIndex, 1);

        // Hiển thị một hộp thoại xác nhận trước khi xóa
        int result = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa sản phẩm '" + itemNameToDelete + "'?", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            // Xóa dữ liệu từ bảng
            DefaultTableModel model = (DefaultTableModel) saleFrame.getSelectedProductTable().getModel();
            model.removeRow(selectedRowIndex);

            // Thực hiện các thao tác cần thiết sau khi xóa
            // Xóa dữ liệu trong các trường
            saleFrame.getIdProductField().setText("");
            saleFrame.getNameProductField().setText("");
            saleFrame.getItemPriceField().setText("");
            saleFrame.getQuantityField().setText("");
        }
        if(saleFrame.getSelectedProductTable().getModel().getRowCount() > 0)
        {
            saleFrame.getTotalAmountField().setText(String.valueOf(calculateTotalAmount()));
        }
        if(saleFrame.getSelectedProductTable().getModel().getRowCount() == 0)
        {
             saleFrame.getConfirmButton().setEnabled(false);
             saleFrame.getTotalAmountField().setText("");
        }

    }
    private double calculateTotalAmount() {
        DefaultTableModel model = (DefaultTableModel) saleFrame.getSelectedProductTable().getModel();
        int rowCount = model.getRowCount();
        double totalAmount = 0.0;

        for (int i = 0; i < rowCount; i++) {
            // Lấy giá trị từ cột "amount"
            double amount = (Double) model.getValueAt(i, 4);
            totalAmount += amount;
        }

        return totalAmount;
    }
    
    
}
