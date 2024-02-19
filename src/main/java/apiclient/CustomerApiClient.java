/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apiclient;

import Model.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class CustomerApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/customers";
    // Gửi GET request để lấy tất cả khách hàng
    private ObjectMapper objectMapper;

    public CustomerApiClient() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Customer> getAllCustomers() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                Customer[] customers = objectMapper.readValue(reader, Customer[].class);
                return Arrays.asList(customers);
            }
        } else {
            return Collections.emptyList();
        }
    }

    public Customer getCustomerByCode(String customerCode) throws IOException {
        URL url = new URL(BASE_URL + "/" + customerCode);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return objectMapper.readValue(reader, Customer.class);
            }
        } else {
            return null;
        }
    }
    public Customer getCustomerByPhoneNumber(String phoneNumber) throws IOException
    {
        URL url = new URL(BASE_URL+"/search/"+phoneNumber);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
                return objectMapper.readValue(reader, Customer.class);
            }
        }
            else
            {
                return null;
            }
    }
   

    // Gửi POST request để tạo mới một khách hàng
    public Customer createCustomer(Customer customer) throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(customer);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return objectMapper.readValue(reader, Customer.class);
            }
        } else {
            return null;
        }
    }

    public boolean updateCustomer(String customerCode, Customer updatedCustomer) throws IOException {
        URL url = new URL(BASE_URL + "/" + customerCode);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream();
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
            String jsonBody = objectMapper.writeValueAsString(updatedCustomer);
            writer.write(jsonBody);
            writer.flush();
        }

        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK;
    }

    public boolean deleteCustomer(String customerCode) throws IOException {
        URL url = new URL(BASE_URL + "/" + customerCode);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_NO_CONTENT;
    }

}
