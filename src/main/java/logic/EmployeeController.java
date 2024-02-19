/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import Model.Employee;
import apiclient.EmployeeApiClient;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import ui.ManageEmployeeFrame;

/**
 *
 * @author ADMIN
 */
public class EmployeeController {
    private ManageEmployeeFrame manageEmployeeFrame;
    private EmployeeApiClient employeeApiClient;

    public EmployeeController(ManageEmployeeFrame manageEmployeeFrame, EmployeeApiClient employeeApiClient) {
        this.manageEmployeeFrame = manageEmployeeFrame;
        this.employeeApiClient = employeeApiClient;
    }
    public void initialize() {
        manageEmployeeFrame.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });

        manageEmployeeFrame.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editEmployee();
            }
        });

        manageEmployeeFrame.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteEmployee();
            }
        });
        manageEmployeeFrame.getEmployeeTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = manageEmployeeFrame.getEmployeeTable().getSelectedRow();
                if (selectedRow >= 0) {
                    // Lấy thông tin từ hàng được chọn và hiển thị trong các trường
                    showEmployeeDetails(selectedRow);
                    // Enable nút Edit và Delete
                    manageEmployeeFrame.getEditButton().setEnabled(true);
                    manageEmployeeFrame.getDeleteButton().setEnabled(true);
                } else {
                    // Nếu không có hàng nào được chọn, disable nút Edit và Delete
                    manageEmployeeFrame.getEditButton().setEnabled(false);
                    manageEmployeeFrame.getDeleteButton().setEnabled(false);
                }
            }
        });
        manageEmployeeFrame.getClearButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }
    public void saveEmployee()
    {
        
            String employeeCode = manageEmployeeFrame.getEmployeeCodeField().getText();
            String employeeName = manageEmployeeFrame.getEmployeeNameField().getText();
            String gender = (String) manageEmployeeFrame.getGenderField().getSelectedItem();
            LocalDate dateOfBirth = LocalDate.parse(manageEmployeeFrame.getEmployeeDOBField().getText());
            String employeeAddress = manageEmployeeFrame.getEmployeeAddressField().getText();
            String employeePhoneNumber = manageEmployeeFrame.getEmployeePhoneNumberField().getText();
            String employeeEmail = manageEmployeeFrame.getEmployeeEmailField().getText();
            String employeeRole = manageEmployeeFrame.getEmployeeRoleField().getText();
            
            if (employeeCode.isEmpty() || employeeName.isEmpty() || employeeAddress.isEmpty() ||employeePhoneNumber.isEmpty() || employeeEmail.isEmpty() || employeeRole.isEmpty() ) {
            JOptionPane.showMessageDialog(manageEmployeeFrame, "Vui lòng điền đầy đủ thông tin nhân viên.");
            return;
        }
            
        try {
            Employee newEmployee = new Employee(null, employeeCode, employeeName, gender, dateOfBirth, employeeAddress, employeePhoneNumber, employeeEmail, employeeRole);
            Employee addedEmployee = employeeApiClient.addEmployee(newEmployee);
            if (addedEmployee != null) {
                JOptionPane.showMessageDialog(manageEmployeeFrame, "Thêm nhân viên thành công");
                updateTable(); // Cập nhật bảng sau khi thêm
                clearFields(); // Xóa các trường nhập liệu
            } else {
                JOptionPane.showMessageDialog(manageEmployeeFrame, "Thêm nhân viên thất bại");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageEmployeeFrame, "Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập");
        }
    }
        
        
        
    public void editEmployee()
    {
        try {
            Long id = Long.parseLong(manageEmployeeFrame.getIdField().getText());
            String employeeCode = manageEmployeeFrame.getEmployeeCodeField().getText();
            String employeeName = manageEmployeeFrame.getEmployeeNameField().getText();
            String gender = (String) manageEmployeeFrame.getGenderField().getSelectedItem();
            LocalDate dateOfBirth = LocalDate.parse(manageEmployeeFrame.getEmployeeDOBField().getText());
            String address = manageEmployeeFrame.getEmployeeAddressField().getText();
            String phoneNumber = manageEmployeeFrame.getEmployeePhoneNumberField().getText();
            String email = manageEmployeeFrame.getEmployeeEmailField().getText();
            String role = manageEmployeeFrame.getEmployeeRoleField().getText();

            Employee updatedEmployee = new Employee(id, employeeCode, employeeName, gender, dateOfBirth, address, phoneNumber, email, role);
            Employee editedEmployee = employeeApiClient.updateEmployee(employeeCode, updatedEmployee);

            if (editedEmployee != null) {
                JOptionPane.showMessageDialog(manageEmployeeFrame, "Sửa thông tin nhân viên thành công");
                updateTable(); // Cập nhật bảng sau khi sửa
                clearFields(); // Xóa các trường nhập liệu
            } else {
                JOptionPane.showMessageDialog(manageEmployeeFrame, "Sửa thông tin nhân viên thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageEmployeeFrame, "Đã xảy ra lỗi. Vui lòng kiểm tra lại dữ liệu nhập");
        }
    }
    public void deleteEmployee()
    {
        try {
        String employeeCode = manageEmployeeFrame.getEmployeeCodeField().getText();
        boolean success = employeeApiClient.deleteEmployee(employeeCode);

        if (success) {
            JOptionPane.showMessageDialog(manageEmployeeFrame, "Xóa nhân viên thành công");
            updateTable(); // Cập nhật bảng sau khi xóa
            clearFields(); // Xóa các trường nhập liệu
        } else {
            JOptionPane.showMessageDialog(manageEmployeeFrame, "Xóa nhân viên thất bại");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(manageEmployeeFrame, "Đã xảy ra lỗi. Vui lòng kiểm tra lại");
    }
    }
    public void updateTable()
    {
        manageEmployeeFrame.getTableModel().setRowCount(0);
        loadEmployeeData();
    }
    public void loadEmployeeData()
    {
        try{
            List<Employee> employees = employeeApiClient.getAllEmployees();
            if(employees != null)
            {
                for(Employee employee : employees)
                {
                    Object[] rowData =
                    {
                        employee.getId(),
                        employee.getEmployeeCode(),
                        employee.getName(),
                        employee.getGender(),
                        employee.getDateOfBirth(),
                        employee.getAddress(),
                        employee.getPhoneNumber(),
                        employee.getEmail(),
                        employee.getRole()
                    };
                    manageEmployeeFrame.getTableModel().addRow(rowData);
                }
            }
            
        }catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(manageEmployeeFrame, "Không thể tải dữ liệu từ API");
        }
    }
    public void showEmployeeDetails(int rowIndex)
    {
        Long id = (Long) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 0);
        String employeeCode = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 1);
        String employeeName = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 2);
        String employeeGender = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 3);
        LocalDate dateOfBirth = (LocalDate) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 4);
        String employeeAddress = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 5);
        String employeePhoneNumber = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 6);
        String employeeEmail = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex, 7);
        String employeeRole = (String) manageEmployeeFrame.getEmployeeTable().getValueAt(rowIndex,8);

        // Hiển thị thông tin trong các trường
        manageEmployeeFrame.getIdField().setText(String.valueOf(id));
        manageEmployeeFrame.getEmployeeCodeField().setText(employeeCode);
        manageEmployeeFrame.getEmployeeNameField().setText(employeeName);
        manageEmployeeFrame.getGenderField().setSelectedItem(employeeGender);
        manageEmployeeFrame.getEmployeeDOBField().setText(dateOfBirth.toString());
        manageEmployeeFrame.getEmployeeAddressField().setText(employeeAddress);
        manageEmployeeFrame.getEmployeePhoneNumberField().setText(employeePhoneNumber);
        manageEmployeeFrame.getEmployeeEmailField().setText(employeeEmail);
        manageEmployeeFrame.getEmployeeRoleField().setText(employeeRole);

    }
    private void clearFields()
    {
        manageEmployeeFrame.getIdField().setText("");
        manageEmployeeFrame.getEmployeeCodeField().setText("");
        manageEmployeeFrame.getEmployeeNameField().setText("");
        manageEmployeeFrame.getGenderField().setSelectedIndex(0);
        manageEmployeeFrame.getEmployeeDOBField().setText("");
        manageEmployeeFrame.getEmployeeAddressField().setText("");
        manageEmployeeFrame.getEmployeePhoneNumberField().setText("");
        manageEmployeeFrame.getEmployeeEmailField().setText("");
        manageEmployeeFrame.getEmployeeRoleField().setText("");

        // Disable nút Edit và Delete
        manageEmployeeFrame.getEditButton().setEnabled(false);
        manageEmployeeFrame.getDeleteButton().setEnabled(false);
    }
}
