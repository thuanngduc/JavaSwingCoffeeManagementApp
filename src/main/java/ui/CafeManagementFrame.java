package ui;
import apiclient.CustomerApiClient;
import apiclient.EmployeeApiClient;
import apiclient.OrderApiClient;
import apiclient.ProductApiClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.CustomerController;
import logic.EmployeeController;
import logic.OrderController;
import logic.ProductController;
import logic.SaleController;

public class CafeManagementFrame extends JFrame {

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    public CafeManagementFrame() {
        super("Quản lý quán cà phê");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Tạo SplitPane để chia cửa sổ thành hai phần
        splitPane.setDividerLocation(150); // Đặt kích thước của phần menu
        // Tạo panel cho phần menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        JMenuItem manageProductItem = createStyledMenuItem("Quản lý Sản Phẩm");
        JMenuItem manageEmployeeItem = createStyledMenuItem("Quản lý Nhân Viên");
        JMenuItem manageCustomerItem = createStyledMenuItem("Quản lý Khách Hàng");
        JMenuItem sellItem = createStyledMenuItem("Bán Hàng");
        JMenuItem invoiceItem = createStyledMenuItem("Hóa Đơn");
        JMenuItem statItem = createStyledMenuItem("Thống Kê");

        menuPanel.add(manageProductItem);
        menuPanel.add(manageEmployeeItem);
        menuPanel.add(manageCustomerItem);
        menuPanel.add(sellItem);
        menuPanel.add(invoiceItem);
        menuPanel.add(statItem);

        splitPane.setLeftComponent(menuPanel);

        // Tạo panel cho phần nội dung chính
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("ỨNG DỤNG QUẢN LÝ QUÁN CÀ PHÊ");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(JLabel.CENTER);

        contentPanel.add(label, BorderLayout.CENTER);
        splitPane.setRightComponent(contentPanel);

        // Gán sự kiện cho menu item
        manageProductItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManageProductPanel();
            }
        });

        manageEmployeeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManageEmployeePanel();
            }
        });

        manageCustomerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openManageCustomerPanel();
            }
        });

        sellItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSellPanel();
            }
        });

        invoiceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openInvoicePanel();
            }
        });

        statItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStatPanel();
            }
        });

        // Đặt cửa sổ chính giữa màn hình
        setLocationRelativeTo(null);
        setVisible(true);
        add(splitPane);
    }

    private JMenuItem createStyledMenuItem(String label) {
        JMenuItem menuItem = new JMenuItem(label);
        menuItem.setBorder(new EmptyBorder(5, 15, 5, 15)); // Thêm border cho JMenuItem
        menuItem.setHorizontalAlignment(SwingConstants.CENTER);
        menuItem.setBackground(new Color(145, 108, 85)); // Màu nền 
        menuItem.setForeground(Color.white); // Màu chữ trắng để đối contrast
        menuItem.setOpaque(true);
        return menuItem;
    }

    private void openManageProductPanel() {
        ManageProductFrame manageProductFrame = new ManageProductFrame();
        ProductApiClient productApiClient = new ProductApiClient();
        ProductController productController = new ProductController(manageProductFrame, productApiClient);
        productController.initialize();
        
        manageProductFrame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            manageProductFrame.setVisible(false);        }
    });
    }

    private void openManageEmployeePanel() {
        ManageEmployeeFrame manageEmployeeFrame = new ManageEmployeeFrame();
        EmployeeApiClient employeeApiClient = new EmployeeApiClient();
        EmployeeController employeeController = new EmployeeController(manageEmployeeFrame, employeeApiClient);
        employeeController.initialize();
    }

    private void openManageCustomerPanel() {
        ManageCustomerFrame manageCustomerFrame = new ManageCustomerFrame();
        CustomerApiClient customerApiClient = new CustomerApiClient();
        CustomerController customerController = new CustomerController (manageCustomerFrame, customerApiClient);
        customerController.initialize();
        
    }

    private void openSellPanel() {
        SaleFrame saleFrame = new SaleFrame();
        CustomerApiClient customerApiClient = new CustomerApiClient();
        ProductApiClient productApiClient = new ProductApiClient();
        OrderApiClient orderApiClient = new OrderApiClient();
        SaleController saleController = new SaleController(saleFrame, orderApiClient, productApiClient, customerApiClient);
        saleController.initialize();
    }

    private void openInvoicePanel() {
        ManageOrderFrame manageOrderFrame = new ManageOrderFrame();
        OrderApiClient orderApiClient = new OrderApiClient();
        ProductApiClient productApiClient = new ProductApiClient();
        OrderController orderController = new OrderController(manageOrderFrame, orderApiClient, productApiClient);
        orderController.initialize();
    }

    private void openStatPanel() {
        // Gọi hàm để hiển thị giao diện Thống Kê
        StaticFrame staticFrame;
        try {
            staticFrame = new StaticFrame();
            staticFrame.setVisible(true);

        } catch (IOException ex) {
            Logger.getLogger(CafeManagementFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CafeManagementFrame();
            }
        });
    }
}
