/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

import Model.Customer;
import Model.Order;
import apiclient.OrderApiClient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author ADMIN
 */
public class StaticFrame extends JFrame{
    
    private OrderApiClient orderApiClient;
    public StaticFrame() throws IOException {
        this.orderApiClient = new OrderApiClient();
        List<Order> orders = orderApiClient.getAllOrders();
        initUI(orders);
    }

    public void initUI(List<Order> orders) {
        setTitle("Thống kê theo biểu đồ");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        Map<LocalDateTime, Double> dailyRevenueMap = calculateDailyRevenue(orders);

        XYSeries series = new XYSeries("Biểu đồ doanh thu theo ngày");

        for (Map.Entry<LocalDateTime, Double> entry : dailyRevenueMap.entrySet()) {
            LocalDateTime date = entry.getKey();
            Double totalAmount = entry.getValue();

            // Convert LocalDateTime to Date
            Date dateConverted = java.util.Date.from(date.atZone(java.time.ZoneId.systemDefault()).toInstant());

            // Add data to series
            series.add(dateConverted.getTime(), totalAmount);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Doanh Thu Theo Ngày",
                "Ngày",
                "Tổng tiền",
                dataset
        );

        XYPlot plot = chart.getXYPlot();

        // Set DateAxis for the x-axis
        DateAxis xAxis = new DateAxis("Order Date");
        xAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        plot.setDomainAxis(xAxis);

        // Set NumberAxis for the y-axis
        NumberAxis yAxis = new NumberAxis("Total Amount");
        plot.setRangeAxis(yAxis);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private Map<LocalDateTime, Double> calculateDailyRevenue(List<Order> orders) {
        Map<LocalDateTime, Double> dailyRevenueMap = new HashMap<>();

        for (Order order : orders) {
            LocalDateTime orderDate = order.getOrderDate();
            Double totalAmount = order.getTotalAmount();

            // Truncate to only consider the date part
            LocalDateTime truncatedDate = orderDate.withHour(0).withMinute(0).withSecond(0).withNano(0);

            // Accumulate total amount for each day
            dailyRevenueMap.merge(truncatedDate, totalAmount, Double::sum);
        }

        return dailyRevenueMap;
    }
}
