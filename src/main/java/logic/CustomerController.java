/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import Model.Customer;
import apiclient.CustomerApiClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ui.ManageCustomerFrame;

/**
 *
 * @author ADMIN
 */
public class CustomerController {
    private ManageCustomerFrame manageCustomerFrame;
    private CustomerApiClient customerApiClient;
    
    public CustomerController(ManageCustomerFrame manageCustomerFrame, CustomerApiClient customerApiClient)
    {
        this.manageCustomerFrame = manageCustomerFrame;
        this.customerApiClient = customerApiClient;
    }
    public void initialize()
    {
        manageCustomerFrame.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCustomer();
            }
        });

        manageCustomerFrame.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });

        manageCustomerFrame.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCustomer();
            }
        });
        manageCustomerFrame.getCustomerTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = manageCustomerFrame.getCustomerTable().getSelectedRow();
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn và hiển thị trong các trường
                    showCustomerDetails(selectedRow);
                    // Enable nút Edit và Delete
                    manageCustomerFrame.getEditButton().setEnabled(true);
                    manageCustomerFrame.getDeleteButton().setEnabled(true);
                } else {
                    // Nếu không có hàng nào được chọn, disable nút Edit và Delete
                    manageCustomerFrame.getEditButton().setEnabled(false);
                    manageCustomerFrame.getDeleteButton().setEnabled(false);
                }
            }
        });
        manageCustomerFrame.getClearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }
    public void saveCustomer()
    {
        String code = manageCustomerFrame.getCustomerCodeField().getText();
        String name = manageCustomerFrame.getCustomerNameField().getText();
        String phoneNumber = manageCustomerFrame.getCustomerPhoneNumberField().getText();

        if (code.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(manageCustomerFrame, "Vui lòng điền đầy đủ thông tin khách hàng.");
            return;
        }
         try {
            Customer existingCustomer = customerApiClient.getCustomerByCode(code);
            if (existingCustomer != null) {
                JOptionPane.showMessageDialog(manageCustomerFrame, "Mã khách hàng đã tồn tại trong hệ thống.");
                return;
            }
            Customer validatePhoneNumber = customerApiClient.getCustomerByPhoneNumber(existingCustomer.getPhoneNumber());
            if(validatePhoneNumber != null)
            {
                JOptionPane.showMessageDialog(manageCustomerFrame, "Sdt đã tồn tại trong hệ thống");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageCustomerFrame, "Không thể kiểm tra mã khách hàng từ API.");
            return;
        }

        Customer newCustomer = new Customer(code, name, phoneNumber);

        try
        {
            Customer createdCustomer = customerApiClient.createCustomer(newCustomer);
            JOptionPane.showMessageDialog(manageCustomerFrame, "Đã thêm khách hàng mới thành công.");
            updateTable();
            clearFields();
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(manageCustomerFrame, "Đã xảy ra lỗi khi thêm khách hàng mới.");

        }
        
    }
    public void editCustomer() {
        String code = manageCustomerFrame.getCustomerCodeField().getText();
        String name = manageCustomerFrame.getCustomerNameField().getText();
        String phoneNumber = manageCustomerFrame.getCustomerPhoneNumberField().getText();

        if (code.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
            JOptionPane.showMessageDialog(manageCustomerFrame, "Vui lòng chọn khách hàng cần chỉnh sửa.");
            return;
        }

        Customer updatedCustomer = new Customer(code, name, phoneNumber);
        try {
            boolean success = customerApiClient.updateCustomer(code, updatedCustomer);
            if (success) {
                JOptionPane.showMessageDialog(manageCustomerFrame, "Đã cập nhật thông tin khách hàng thành công.");
                updateTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(manageCustomerFrame, "Đã xảy ra lỗi khi cập nhật thông tin khách hàng.");
            }
        } catch (IOException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void deleteCustomer()
    {
        String customerCode = (String) manageCustomerFrame.getCustomerCodeField().getText();
        
        try{
            boolean success = customerApiClient.deleteCustomer(customerCode);
            if(success)
            {
                JOptionPane.showMessageDialog(manageCustomerFrame,"Xóa thành công");
                updateTable();
            }
            else
            {
                JOptionPane.showMessageDialog(manageCustomerFrame, "Xóa không thành công");
            }
        }catch(Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(manageCustomerFrame, "Lỗi khi xóa: " + e.getMessage());
                }
    }
    public void updateTable()
    {
        manageCustomerFrame.getTableModel().setRowCount(0);
        loadCustomerData();
    }
    public void loadCustomerData()
    {
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
                   manageCustomerFrame.getTableModel().addRow(rowData);
               }
           }
       }catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageCustomerFrame, "Không thể tải dữ liệu từ API");
        }
    }
    public void showCustomerDetails(int rowIndex)
    {
                String code = (String) manageCustomerFrame.getCustomerTable().getValueAt(rowIndex, 0);
                String name = (String) manageCustomerFrame.getCustomerTable().getValueAt(rowIndex, 1);
                String phoneNumber = (String) manageCustomerFrame.getCustomerTable().getValueAt(rowIndex, 2);
                
                manageCustomerFrame.getCustomerCodeField().setText(code);
                manageCustomerFrame.getCustomerNameField().setText(name);
                manageCustomerFrame.getCustomerPhoneNumberField().setText(phoneNumber);

    }
    public void clearFields()
    {
        manageCustomerFrame.getCustomerCodeField().setText("");
        manageCustomerFrame.getCustomerNameField().setText("");
        manageCustomerFrame.getCustomerPhoneNumberField().setText("");

    }
}
