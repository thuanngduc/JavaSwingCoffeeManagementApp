/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package apiclient;

import Model.Order;
import Model.OrderItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public class OrderApiClient {
    private static final String BASE_URL = "http://localhost:8080/api/orders";
    private ObjectMapper objectMapper;
    
    public OrderApiClient()
    {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public List<Order> getAllOrders() throws IOException
    {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                Order[] orders = objectMapper.readValue(reader, Order[].class);
                return Arrays.asList(orders);
            }
        }
        else
        {
            return null;
        }
    }
    public Order getOrderById(Long id) throws IOException
    {
        URL url = new URL(BASE_URL + "/" +id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                return objectMapper.readValue(reader, Order.class);
            }
        }
        else
        {
            return null;
        }
    }
    public Order createOrder(Map<String, Object> requestData) throws IOException {
        URL url = new URL(BASE_URL + "/create");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            String jsonRequest = objectMapper.writeValueAsString(requestData);
            outputStream.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return objectMapper.readValue(reader, Order.class);
            }
        } else {
            return null;
        }
    }
    public Order updateOrder(Long orderId, Map<String, Object> requestData) throws IOException {
        URL url = new URL(BASE_URL + "/" + orderId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            String jsonRequest = objectMapper.writeValueAsString(requestData);
            outputStream.write(jsonRequest.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                return objectMapper.readValue(reader, Order.class);
            }
        } else {
            return null;
        }
    }
    public boolean deleteOrder(Long id) throws IOException
    {
        URL url = new URL(BASE_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_NO_CONTENT;
    }
    public List<OrderItem> getAllOrderItems(Long id) throws IOException
    {
        URL url = new URL("http://localhost:8080/api/order-items/by-order/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK)
        {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)))
            {
                OrderItem[] orderItems = objectMapper.readValue(reader, OrderItem[].class);
                return Arrays.asList(orderItems);
            }
        }
        else
        {
            return null;
        }
    }
}
