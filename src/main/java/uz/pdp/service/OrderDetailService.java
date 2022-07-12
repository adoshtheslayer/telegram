package uz.pdp.service;

import org.springframework.stereotype.Service;
import uz.pdp.model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService {

    public static List<OrderDetail> getOrderList() {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        Connection connection = Base.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from order_detail");
            while (resultSet.next()) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.get(resultSet);
                orderDetailList.add(orderDetail);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetailList;
    }

    public static void add(OrderDetail orderDetail) {
        Connection connection = Base.getConnection();

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select * from add_order_detail(?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, orderDetail.getId());
            preparedStatement.setInt(2, orderDetail.getOrderId());
            preparedStatement.setString(3, orderDetail.getProductName());
            preparedStatement.setDouble(4, orderDetail.getPrice());
            preparedStatement.setInt(5, orderDetail.getAmount());
            preparedStatement.setDouble(6, orderDetail.getTotalPrice());
            preparedStatement.setString(7, String.valueOf(orderDetail.getStatus()));
            preparedStatement.execute();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void setStatus(Integer orderDetailId){
        Connection connection = Base.getConnection();
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from set_status(?)");
            preparedStatement.setInt(1, orderDetailId);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
