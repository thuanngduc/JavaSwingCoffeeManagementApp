package apiclient;

import Model.Customer;
import Model.Order;
import Model.OrderItem;
import Model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class OrderApiClientDraw {
    private static final String BASE_URL = "http://localhost:8080/api/orders";

    public List<Order> getAllOrders() {
        try {
            URL url = new URL(BASE_URL);

            // Mở kết nối HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Đặt phương thức HTTP GET
            connection.setRequestMethod("GET");

            // Lấy mã trạng thái HTTP từ response
            int responseCode = connection.getResponseCode();

            // Kiểm tra xem request có thành công không
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Sử dụng try-with-resources để đảm bảo đóng resource
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Chuyển đổi JSON response thành danh sách Order
                    List<Order> orders = parseJsonResponse(response.toString());

                    // Đóng kết nối
                    connection.disconnect();

                    return orders;
                }
            } else {
                // Xử lý trường hợp lỗi nếu mã trạng thái không phải là HTTP_OK
                System.out.println("Failed to retrieve orders. HTTP Error Code: " + responseCode);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức này để chuyển đổi JSON thành danh sách Order
   private List<Order> parseJsonResponse(String jsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();

    // Cấu hình ObjectMapper để hỗ trợ Java 8 date/time types
    objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    try {
        return objectMapper.readValue(jsonResponse, new TypeReference<List<Order>>() {});
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

    public void printAllOrders() {
    List<Order> orders = getAllOrders();

    if (orders != null && !orders.isEmpty()) {
        System.out.println("List of Orders:");
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId());
            printCustomerDetails(order.getCustomer());
            printOrderItemsDetails(order.getOrderItems());
            System.out.println("Total Amount: " + order.getTotalAmount());
            // In thêm thông tin khác nếu cần
            System.out.println("-------------------------");
        }
    } else {
        System.out.println("No orders found.");
    }
}

private void printCustomerDetails(Customer customer) {
    if (customer != null) {
        System.out.println("Customer Code: " + customer.getCustomerCode());
        System.out.println("Customer Name: " + customer.getName());
        System.out.println("Customer Phone Number: " + customer.getPhoneNumber());
    } else {
        System.out.println("No customer details available.");
    }
}


private void printOrderItemsDetails(Set<OrderItem> orderItems) {
    if (orderItems != null && !orderItems.isEmpty()) {
        System.out.println("Order Items:");
        for (OrderItem orderItem : orderItems) {
            System.out.println("  Order Item ID: " + orderItem.getId());
            printProductDetails(orderItem.getProduct());
            System.out.println("  Quantity: " + orderItem.getQuantity());
            // In thêm thông tin khác nếu cần
            System.out.println("  -------------------------");
        }
    } else {
        System.out.println("No order items available.");
    }
}

private void printProductDetails(Product product) {
    if (product != null) {
        System.out.println("    Product ID: " + product.getId());
        System.out.println("    Item Name: " + product.getItemName());
        System.out.println("    Description: " + product.getDescription());
        System.out.println("    Price: " + product.getPrice());
        System.out.println("    Product Type: " + product.getProductType());
        System.out.println("    Image URL: " + product.getImageUrl());
    } else {
        System.out.println("    No product details available.");
    }
}

    public static void main(String[] args) {
        OrderApiClientDraw orderApiClient = new OrderApiClientDraw();
        orderApiClient.printAllOrders();
    }
}
