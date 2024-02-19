/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import Model.Customer;
import Model.Order;
import Model.OrderItem;
import Model.Product;
import apiclient.OrderApiClient;
import apiclient.ProductApiClient;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import ui.ManageOrderFrame;

/**
 *
 * @author ADMIN
 */
public class OrderController extends JFrame{
    private ManageOrderFrame manageOrderFrame;
    private OrderApiClient orderApiClient;
    private ProductApiClient productApiClient;

    public OrderController(ManageOrderFrame manageOrderFrame, OrderApiClient orderApiClient, ProductApiClient productApiClient) {
        this.manageOrderFrame = manageOrderFrame;
        this.orderApiClient = orderApiClient;
        this.productApiClient = productApiClient;
    }
    
    public void initialize()
    {
        manageOrderFrame.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteOrder();
            }
        });
        manageOrderFrame.getOrderTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = manageOrderFrame.getOrderTable().getSelectedRow();
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn và hiển thị trong các trường
                    showOrderDetails(selectedRow);
                    manageOrderFrame.getDeleteButton().setEnabled(true);
                    manageOrderFrame.getDetailButton().setEnabled(true);
                    manageOrderFrame.getPrintPDFButton().setEnabled(true);
                   
                } else {
                    manageOrderFrame.getDeleteButton().setEnabled(false);
                    manageOrderFrame.getDetailButton().setEnabled(false);
                    manageOrderFrame.getPrintPDFButton().setEnabled(false);
                }
            }
        });
        manageOrderFrame.getDetailButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDetailDialog();
            }
        });
        manageOrderFrame.getPrintPDFButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printPDF();
            }
        });
    }
    public void deleteOrder()
    {
        Long orderId = Long.parseLong(manageOrderFrame.getIdField().getText());
        try{
            boolean success = orderApiClient.deleteOrder(orderId);
            if(success)
            {
                 JOptionPane.showMessageDialog(manageOrderFrame, "Xóa hóa đơn không thành công!");
                 updateTable();
                 clearFields();
            }
            else
            {
                JOptionPane.showMessageDialog(manageOrderFrame, "Hóa đơn đã được xóa thành công");
                updateTable();
                clearFields();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageOrderFrame, "Lỗi khi xóa hóa đơn: " + e.getMessage());
        }
    }
    public void updateTable()
    {
        manageOrderFrame.getTableModel().setRowCount(0);
        loadOrderData();
    }
    public void loadOrderData()
    {
        try {
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
                    manageOrderFrame.getTableModel().addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageOrderFrame, "Không thể tải dữ liệu từ API");
        }
    }
    private void showOrderDetails(int rowIndex)
    {
        Long id = (Long) manageOrderFrame.getOrderTable().getValueAt(rowIndex, 0);
        String customerCode = (String) manageOrderFrame.getOrderTable().getValueAt(rowIndex, 1);
        LocalDateTime orderDate = (LocalDateTime) manageOrderFrame.getOrderTable().getValueAt(rowIndex, 2);
        Double totalAmount = (Double) manageOrderFrame.getOrderTable().getValueAt(rowIndex, 3);
        
        manageOrderFrame.getIdField().setText(String.valueOf(id));
        manageOrderFrame.getCustomerCodeField().setText(customerCode);
        manageOrderFrame.getOrderDateField().setText(orderDate.toString());
        manageOrderFrame.getTotalAmountField().setText(String.valueOf(totalAmount));
    }
    private void clearFields()
    {
        manageOrderFrame.getIdField().setText("");
        manageOrderFrame.getCustomerCodeField().setText("");
        manageOrderFrame.getOrderDateField().setText("");
        manageOrderFrame.getTotalAmountField().setText("");
    }
    private void openDetailDialog()
    {
        JDialog dialog = new JDialog(this, "Chi tiết sản phẩm", true);
        dialog.setLayout(new BorderLayout());

        // Khu vực Border WEST
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        westPanel.add(new JLabel("Order ID:"), gbc);
        gbc.gridy++;
        

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idProductField = new JTextField(10);
        idProductField.setEnabled(false);
        westPanel.add(idProductField, gbc);
        gbc.gridy++;

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
        tableModel.addColumn("Số Lượng");
        tableModel.addColumn("Thành tiền");

        JTable tableProduct;
        tableProduct = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        tableProduct.setEnabled(false);

        dialog.add(new JScrollPane(tableProduct), BorderLayout.CENTER);

        try {
            Long idOrder = Long.parseLong(manageOrderFrame.getIdField().getText()) ;
            idProductField.setText(String.valueOf(idOrder));
            List<OrderItem> orderItems = orderApiClient.getAllOrderItems(idOrder);
            if (orderItems != null) {
                for (OrderItem orderItem : orderItems) {
                    Product product = productApiClient.getProductById(orderItem.getProduct().getId());
                    System.out.println(product.getItemName());
                    double amount = product.getPrice() * orderItem.getQuantity();
                    Object[] rowData = {
                        product.getId(),
                        product.getItemName(),
                        product.getPrice(),
                        orderItem.getQuantity(),
                        amount
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }

        // Tạo nút OK để đóng cửa sổ con
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                dialog.dispose();
            }
        });
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    private void printPDF() {
        Document document = new Document();
        LocalDateTime currentTime = LocalDateTime.now();
    
        // Định dạng thời gian theo yêu cầu (ngày_tháng_năm_giờ_phút_giây)
        String formattedDateTime = currentTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));

        // Tạo tên file PDF dựa trên thời gian hiện tại
        String fileName = "order_details_" + formattedDateTime + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Add order details to PDF
            addOrderDetailsToPDF(document);

            document.close();

            JOptionPane.showMessageDialog(this, "Xuất file pdf thành công: "+ fileName);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addOrderDetailsToPDF(Document document) throws DocumentException, IOException {
        String orderId = manageOrderFrame.getIdField().getText();
        String customerCode = manageOrderFrame.getCustomerCodeField().getText();
        String orderDate = manageOrderFrame.getOrderDateField().getText();
        String totalAmount = manageOrderFrame.getTotalAmountField().getText();
        
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        // Thêm thông tin chi tiết order vào document
        document.add(new Paragraph("Order ID: " + orderId, font));
        document.add(new Paragraph("Customer Code: " + customerCode, font));
        document.add(new Paragraph("Ngày Order: " + orderDate, font));
        document.add(new Paragraph("Tổng tiền: " + totalAmount, font));
        document.add(new Paragraph("\n"));

        // thêm vào pdf
        addOrderItemsToPDF(document, font);
    }

    private void addOrderItemsToPDF(Document document, Font font) throws DocumentException {
        try {
            
           /*
            Long idOrder = Long.parseLong(manageOrderFrame.getIdField().getText()) ;
            idProductField.setText(String.valueOf(idOrder));
            List<OrderItem> orderItems = orderApiClient.getAllOrderItems(idOrder);
            if (orderItems != null) {
                for (OrderItem orderItem : orderItems) {
                    Product product = productApiClient.getProductById(orderItem.getProduct().getId());
                    double amount = product.getPrice() * orderItem.getQuantity();
                    Object[] rowData = {
                        product.getId(),
                        product.getItemName(),
                        product.getPrice(),
                        orderItem.getQuantity(),
                        amount
                    };
                    tableModel.addRow(rowData);
                }
            */
            Long idOrder = Long.parseLong(manageOrderFrame.getIdField().getText());
            List<OrderItem> orderItems = orderApiClient.getAllOrderItems(idOrder);
            if (orderItems != null) {
                for (OrderItem orderItem : orderItems) {
                    Product product = productApiClient.getProductById(orderItem.getProduct().getId());
                    System.out.println(product.getItemName());
                    double amount = product.getPrice() * orderItem.getQuantity();
                    document.add(new Paragraph("Product ID: " + product.getId(), font));
                    document.add(new Paragraph("Tên sản phẩm: " + product.getItemName(), font));
                    document.add(new Paragraph("Giá: " + product.getPrice(), font));
                    document.add(new Paragraph("Số lượng: " + orderItem.getQuantity(), font));
                    document.add(new Paragraph("Thành tiền: " + amount, font));
                    document.add(new Paragraph("\n"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file pdf " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
