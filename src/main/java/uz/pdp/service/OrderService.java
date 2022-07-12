package uz.pdp.service;

import uz.pdp.enums.OrderStatus;
import uz.pdp.model.CartProduct;
import uz.pdp.model.Order;
import uz.pdp.model.OrderDetail;
import uz.pdp.model.Product;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    public static Integer addOrder(Long id) {

        List<CartProduct> cartProductList
                = CartProductService.getProductListByUserId(String.valueOf(id));

        int orderId = 1;
        if (!orderList().isEmpty()) {
            orderId = orderList().get(orderList().size() - 1).getId() + 1;
        }

        Order order = new Order(orderId, id, 0d, String.valueOf(LocalDate.now()), OrderStatus.NEW);
        add(order);

        Double totalPrice = 0d;

        for (CartProduct cartProduct : cartProductList) {

            Product product = ProductService.getProuctById(cartProduct.getProductId());

            int orderDetailId = 1;
            if (!OrderDetailService.getOrderList().isEmpty()) {
                orderDetailId =
                        OrderDetailService.getOrderList().get(OrderDetailService.getOrderList().size() - 1).getId() + 1;
            }

            OrderDetail orderDetail =
                    new OrderDetail(orderDetailId, orderId, product.getName(),
                            product.getPrice(), cartProduct.getAmount(), product.getPrice() * cartProduct.getAmount(),
                            OrderStatus.CONFIRM.name());

            OrderDetailService.add(orderDetail);

            totalPrice += product.getPrice() * cartProduct.getAmount();
        }

        order.setTotalPrice(totalPrice);

        return orderId;
    }

    public static List<Order> orderList() {

        List<Order> orderList = new ArrayList<>();
        Connection connection = Base.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from orders");
            while (resultSet.next()) {
                Order order = new Order();
                order.get(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public static void add(Order order) {
        Connection connection = Base.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("select * from add_order(?,?,?,?,?)");
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setLong(2, order.getUserId());
            preparedStatement.setDouble(3, order.getTotalPrice());
            preparedStatement.setString(4, order.getCreatedAt());
            preparedStatement.setString(5, String.valueOf(order.getStatus()));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
