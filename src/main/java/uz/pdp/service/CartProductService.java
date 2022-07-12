package uz.pdp.service;

import uz.pdp.model.CartProduct;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartProductService {
    public static List<CartProduct> getProductListByUserId(String userId) {

        List<CartProduct> cartProductList = new ArrayList<>();

        Connection connection = Base.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from cart_product where user_id = '" + userId + "'");

            while (resultSet.next()) {
                CartProduct cartProduct = new CartProduct();
                cartProduct.get(resultSet);
                cartProductList.add(cartProduct);
            }

            connection.close();
            return cartProductList;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void add(CartProduct cartProduct) {
        Connection connection = Base.getConnection();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("select * from add_cart_product(?,?,?)");

            preparedStatement.setLong(1, cartProduct.getUserId());
            preparedStatement.setInt(2, cartProduct.getProductId());
            preparedStatement.setInt(3, cartProduct.getAmount());
            preparedStatement.execute();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deleteProduct(int productId, Long userId) {
        Connection connection = Base.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(
                    "delete from cart_product where product_id = '"+productId+"'and user_id ='"+userId+"'");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Long id) {
        Connection connection = Base.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(
                    "delete from cart_product where user_id ='"+id+"'");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
