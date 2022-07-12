package uz.pdp.service;

import org.checkerframework.checker.units.qual.C;
import uz.pdp.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    public static List<Category> getCategoryList() {
        Connection connection = Base.getConnection();
        List<Category> categories = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from category");
            while (resultSet.next()) {
                Category category = new Category();
                category.get(resultSet);
                categories.add(category);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static Category getCategoryBuId(int categoryId) {

        Connection connection = Base.getConnection();
        try {
            CallableStatement callableStatement = connection.prepareCall("select * from get_category(?)");
            callableStatement.setInt(1, categoryId);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()){
                Category category = new Category();
                category.get(resultSet);
                connection.close();
                return category;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
