/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Model.Customer;
import Model.Order;
import apiclient.OrderApiClient;
import java.io.IOException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class OrderTableFrameDraw extends JFrame{
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private OrderApiClient orderApiClient;

    public OrderTableFrameDraw() {
        super("Danh sách Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        
        orderApiClient = new OrderApiClient();

        tableModel = new DefaultTableModel();
        orderTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane);

        loadOrderData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadOrderData() {
        tableModel.addColumn("ID");
        tableModel.addColumn("Mã KH");
        tableModel.addColumn("Ngày Lập HĐ");
        tableModel.addColumn("Tổng tiền");


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
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderTableFrameDraw());
    }
}
