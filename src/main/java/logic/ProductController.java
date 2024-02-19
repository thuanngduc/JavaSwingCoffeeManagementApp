/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import Model.Product;
import Model.ProductType;
import apiclient.ProductApiClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Action;
import javax.swing.JOptionPane;
import ui.ManageProductFrame;

/**
 *
 * @author ADMIN
 */
public class ProductController {
    private ManageProductFrame manageProductFrame;
    private ProductApiClient productApiClient;

    public ProductController(ManageProductFrame manageProductFrame, ProductApiClient productApiClient) {
        this.manageProductFrame = manageProductFrame;
        this.productApiClient = productApiClient;
    }
    
    public void initialize() {
        manageProductFrame.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProduct();
            }
        });

        manageProductFrame.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProduct();
            }
        });

        manageProductFrame.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProduct();
            }
        });
        manageProductFrame.getProductTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = manageProductFrame.getProductTable().getSelectedRow();
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn và hiển thị trong các trường
                    showProductDetails(selectedRow);
                    // Enable nút Edit và Delete
                    manageProductFrame.getEditButton().setEnabled(true);
                    manageProductFrame.getDeleteButton().setEnabled(true);
                } else {
                    // Nếu không có hàng nào được chọn, disable nút Edit và Delete
                    manageProductFrame.getEditButton().setEnabled(false);
                    manageProductFrame.getDeleteButton().setEnabled(false);
                }
            }
        });
        manageProductFrame.getClearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }
    public void saveProduct()
    {
        String itemName = manageProductFrame.getNameField().getText();
        String description = manageProductFrame.getDescriptionField().getText();
        Double price = Double.parseDouble(manageProductFrame.getPriceField().getText());
        String imageUrl = manageProductFrame.getImageUrlField().getText();

        ProductType productType = ProductType.valueOf((String) manageProductFrame.getTypeField().getSelectedItem());

        Product newProduct = new Product(null, itemName, description, price, productType, imageUrl);
        
        if(itemName.isEmpty() || description.isEmpty() || String.valueOf(price).isEmpty() || imageUrl.isEmpty())
        {
            JOptionPane.showMessageDialog(manageProductFrame, "Vui lòng điền đủ thông tin sản phẩm");
            return;
        }

        try {
            // Gửi yêu cầu lưu sản phẩm thông qua API
            Product savedProduct = productApiClient.saveProduct(newProduct);

            // Hiển thị thông báo và cập nhật bảng
            JOptionPane.showMessageDialog(manageProductFrame, "Sản phẩm đã được lưu thành công!");
            updateTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(manageProductFrame, "Lỗi khi lưu sản phẩm: " + ex.getMessage());
        }
    }
    public void editProduct()
    {
        Long productId = Long.parseLong(manageProductFrame.getIdField().getText());
        String itemName = manageProductFrame.getNameField().getText();
        String description = manageProductFrame.getDescriptionField().getText();
        double price = Double.parseDouble(manageProductFrame.getPriceField().getText());
        ProductType productType = ProductType.valueOf((String) manageProductFrame.getTypeField().getSelectedItem());
        String imageUrl = manageProductFrame.getImageUrlField().getText();

        // Tạo đối tượng Product để cập nhật
        Product updatedProduct = new Product(productId, itemName, description, price, productType, imageUrl);

        try {
            // Gửi yêu cầu cập nhật thông tin sản phẩm thông qua API
            boolean success = productApiClient.updateProduct(productId, updatedProduct);

            // Hiển thị thông báo và cập nhật bảng
            if (success) {
                JOptionPane.showMessageDialog(manageProductFrame, "Sản phẩm đã được cập nhật thành công!");
                updateTable();
            } else {
                JOptionPane.showMessageDialog(manageProductFrame, "Cập nhật sản phẩm không thành công!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(manageProductFrame, "Lỗi khi cập nhật sản phẩm: " + ex.getMessage());
        }
    }
    public void deleteProduct()
    {
        // Lấy ID của sản phẩm được chọn
        Long productId = Long.parseLong(manageProductFrame.getIdField().getText());

        try {
            // Gửi yêu cầu xóa sản phẩm thông qua API
            boolean success = productApiClient.deleteProduct(productId);

            // Hiển thị thông báo và cập nhật bảng
            if (success) {
                JOptionPane.showMessageDialog(manageProductFrame, "Sản phẩm đã được xóa thành công!");
                updateTable();
            } else {
                JOptionPane.showMessageDialog(manageProductFrame, "Xóa sản phẩm không thành công!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(manageProductFrame, "Lỗi khi xóa sản phẩm: " + ex.getMessage());
        }
    }
    public void updateTable() {
        // Cập nhật dữ liệu trong bảng
        manageProductFrame.getTableModel().setRowCount(0);
        loadProductData();
    }

    private void loadProductData() {
        // Tải dữ liệu từ API và cập nhật bảng
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
                    manageProductFrame.getTableModel().addRow(rowData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageProductFrame, "Không thể tải dữ liệu từ API");
        }
    }
    private void showProductDetails(int rowIndex) {
        // Lấy thông tin từ hàng được chọn và hiển thị trong các trường
        Long id = (Long) manageProductFrame.getProductTable().getValueAt(rowIndex, 0);
        String itemName = (String) manageProductFrame.getProductTable().getValueAt(rowIndex, 1);
        String description = (String) manageProductFrame.getProductTable().getValueAt(rowIndex, 2);
        Double price = (Double) manageProductFrame.getProductTable().getValueAt(rowIndex, 3);
        String imageUrl = (String) manageProductFrame.getProductTable().getValueAt(rowIndex, 5);

        // Lấy giá trị từ cột "Loại" và chuyển đổi sang chuỗi
        Object typeObj = manageProductFrame.getProductTable().getValueAt(rowIndex, 4);
        String productTypeStr;

        if (typeObj instanceof ProductType) {
            // Nếu giá trị đã là ProductType, lấy tên hiển thị
            productTypeStr = ((ProductType) typeObj).getDisplayName();
        } else if (typeObj instanceof String) {
            // Nếu giá trị là String, sử dụng trực tiếp
            productTypeStr = (String) typeObj;
        } else {
            // Xử lý theo trường hợp khác nếu cần
            productTypeStr = ""; // Hoặc giá trị mặc định khác
        }

        // Hiển thị thông tin trong các trường
        manageProductFrame.getIdField().setText(String.valueOf(id));
        manageProductFrame.getNameField().setText(itemName);
        manageProductFrame.getDescriptionField().setText(description);
        manageProductFrame.getPriceField().setText(String.valueOf(price));

        // Chọn item tương ứng trong JComboBox
        manageProductFrame.getTypeField().setSelectedItem(productTypeStr);
        manageProductFrame.getImageUrlField().setText(imageUrl);
    }
    private void clearFields()
    {
        manageProductFrame.getIdField().setText("");
        manageProductFrame.getNameField().setText("");
        manageProductFrame.getDescriptionField().setText("");
        manageProductFrame.getPriceField().setText("");
        manageProductFrame.getTypeField().setSelectedIndex(0); // Chọn giá trị đầu tiên trong JComboBox
        manageProductFrame.getImageUrlField().setText("");
    }
}
