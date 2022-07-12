package uz.pdp.service;

import uz.pdp.model.Product;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public static Product getProuctById(Integer productId) {

        Connection connection = Base.getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product where id = '" + productId + "'");
            while (resultSet.next()){
                Product product = new Product();
                product.get(resultSet);
                return product;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Product> getProductListByCategoryId(int categoryId) {
        List<Product> productList = new ArrayList<>();

        Connection connection = Base.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product where category_id = '" + categoryId + "'");

            while (resultSet.next()){
                Product product = new Product();
                product.get(resultSet);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }
}
