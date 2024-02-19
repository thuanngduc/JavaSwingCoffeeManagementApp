/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apiclient;

import Model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class EmployeeApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/employees";
    private ObjectMapper objectMapper;


    public EmployeeApiClient() {
         this.objectMapper = new ObjectMapper();
         this.objectMapper.registerModule(new JavaTimeModule());
         this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public List<Employee> getAllEmployees() throws IOException
    {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                Employee[] employees = objectMapper.readValue(reader, Employee[].class);
                return Arrays.asList(employees);
            }
        }
        else
        {
            return Collections.emptyList();
        }
    }
    
    public Employee getEmployeeById(Long id) throws IOException{
       try {
        URL url = new URL(BASE_URL+ "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return objectMapper.readValue(reader, Employee.class);
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Employee addEmployee(Employee employee)
    {
        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream(); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                String jsonBody = objectMapper.writeValueAsString(employee);
                writer.write(jsonBody);
                writer.flush();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return objectMapper.readValue(reader, Employee.class);
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public Employee updateEmployee(String employeeCode, Employee updatedEmployee) {
        try {
            URL url = new URL(BASE_URL + "/" + employeeCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream(); BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                String jsonBody = objectMapper.writeValueAsString(updatedEmployee);
                writer.write(jsonBody);
                writer.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return objectMapper.readValue(reader, Employee.class);
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean deleteEmployee(String employeeCode) {
        try {
            URL url = new URL(BASE_URL + "/" + employeeCode);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Employee> searchEmployees(String query) {
        try {
            URL url = new URL(BASE_URL + "/" + URLEncoder.encode(query, "UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    Employee[] employees = objectMapper.readValue(reader, Employee[].class);
                    return Arrays.asList(employees);
                }
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public Employee getEmployeeByEmployeeCode(String employeeCode) {
        try {
            URL url = new URL(BASE_URL + "/" + URLEncoder.encode(employeeCode, "UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    return objectMapper.readValue(reader, Employee.class);
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    
    
}
