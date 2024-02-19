/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//
package ui;

import Model.Product;
import apiclient.ProductApiClient;
import java.awt.Font;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class ProductTableFrame extends JFrame {

    private JTable productTable;
    private DefaultTableModel tableModel;
    private ProductApiClient productApiClient;

    public ProductTableFrame() {
        super("Danh sách Sản Phẩm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        
        productApiClient = new ProductApiClient();

        tableModel = new DefaultTableModel();
        productTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane);

        loadProductData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadProductData() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Tên Sản Phẩm");
        tableModel.addColumn("Mô Tả");
        tableModel.addColumn("Giá");
        tableModel.addColumn("Loại");
        tableModel.addColumn("URL Hình");

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
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductTableFrame());
    }
}

