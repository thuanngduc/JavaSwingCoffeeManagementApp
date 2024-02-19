/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Model.Employee;
import apiclient.EmployeeApiClient;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class ManageEmployeeFrame extends JFrame{
    private JButton addButton, editButton, deleteButton, clearButton;
    private JTextField idField, employeeCodeField, employeeNameField, employeeDOBField, employeeAddressField, employeePhoneNumberField, employeeEmailField, employeeRoleField;
    private JComboBox<String> genderField;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeApiClient employeeApiClient;
    
    public ManageEmployeeFrame()
    {
        super("Quản lý Nhân Viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1500, 1000);
        
        this.employeeApiClient = new EmployeeApiClient();

        // Tạo panel chứa các components
        JPanel westPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        westPanel.add(new JLabel("ID:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Mã NV:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Tên NV:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Ngày Sinh:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        westPanel.add(new JLabel("Role"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        idField = new JTextField(10);
        westPanel.add(idField, gbc);
        gbc.gridy++;
        idField.setEditable(false);
        
        employeeCodeField = new JTextField(10);
        westPanel.add(employeeCodeField, gbc);
        gbc.gridy++;
        employeeNameField = new JTextField(10);
        westPanel.add(employeeNameField, gbc);
        gbc.gridy++;
        
        String[] categories = {"Nam", "Nữ", "Khác"};
        genderField = new JComboBox<>(categories);
        westPanel.add(genderField, gbc);
        gbc.gridy++;
        
        employeeDOBField = new JTextField(10);
        westPanel.add(employeeDOBField, gbc);
        gbc.gridy++;
        
        employeeAddressField = new JTextField(10);
        westPanel.add(employeeAddressField, gbc);
        gbc.gridy++;
        
        employeePhoneNumberField = new JTextField(10);
        westPanel.add(employeePhoneNumberField, gbc);
        gbc.gridy++; 
        
        employeeEmailField = new JTextField(10);
        westPanel.add(employeeEmailField, gbc);
        gbc.gridy++; 
        
        employeeRoleField = new JTextField(10);
        westPanel.add(employeeRoleField, gbc);
        gbc.gridy++; 

        
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
        tableModel.addColumn("Mã NV");
        tableModel.addColumn("Tên NV");
        tableModel.addColumn("Giới tính");
        tableModel.addColumn("Ngày sinh");
        tableModel.addColumn("Địa chỉ");
        tableModel.addColumn("Số điện thoại");
        tableModel.addColumn("Email");
        tableModel.addColumn("Role");

        employeeTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        loadEmployeeData();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true); 
    }
    private void loadEmployeeData()
    {
        tableModel.setRowCount(0);
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
                    tableModel.addRow(rowData);
                }
            }
            for(int i = 0; i<tableModel.getColumnCount(); i++)
            {
                employeeTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            }
        }catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu từ API");
        }
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

    public JButton getClearButton() {
        return clearButton;
    }

    public void setClearButton(JButton clearButton) {
        this.clearButton = clearButton;
    }

    public JTextField getIdField() {
        return idField;
    }

    public void setIdField(JTextField idField) {
        this.idField = idField;
    }

    public JTextField getEmployeeCodeField() {
        return employeeCodeField;
    }

    public void setEmployeeCodeField(JTextField employeeCodeField) {
        this.employeeCodeField = employeeCodeField;
    }

    public JTextField getEmployeeNameField() {
        return employeeNameField;
    }

    public void setEmployeeNameField(JTextField employeeNameField) {
        this.employeeNameField = employeeNameField;
    }

    public JTextField getEmployeeDOBField() {
        return employeeDOBField;
    }

    public void setEmployeeDOBField(JTextField employeeDOBField) {
        this.employeeDOBField = employeeDOBField;
    }

    public JTextField getEmployeeAddressField() {
        return employeeAddressField;
    }

    public void setEmployeeAddressField(JTextField employeeAddressField) {
        this.employeeAddressField = employeeAddressField;
    }

    public JTextField getEmployeePhoneNumberField() {
        return employeePhoneNumberField;
    }

    public void setEmployeePhoneNumberField(JTextField employeePhoneNumberField) {
        this.employeePhoneNumberField = employeePhoneNumberField;
    }

    public JTextField getEmployeeEmailField() {
        return employeeEmailField;
    }

    public void setEmployeeEmailField(JTextField employeeEmailField) {
        this.employeeEmailField = employeeEmailField;
    }

    public JTextField getEmployeeRoleField() {
        return employeeRoleField;
    }

    public void setEmployeeRoleField(JTextField employeeRoleField) {
        this.employeeRoleField = employeeRoleField;
    }

    public JComboBox<String> getGenderField() {
        return genderField;
    }

    public void setGenderField(JComboBox<String> genderField) {
        this.genderField = genderField;
    }

    public JTable getEmployeeTable() {
        return employeeTable;
    }

    public void setEmployeeTable(JTable employeeTable) {
        this.employeeTable = employeeTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public EmployeeApiClient getEmployeeApiClient() {
        return employeeApiClient;
    }

    public void setEmployeeApiClient(EmployeeApiClient employeeApiClient) {
        this.employeeApiClient = employeeApiClient;
    }
    
}
