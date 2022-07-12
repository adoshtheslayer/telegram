package uz.pdp.service;

import uz.pdp.model.User;

import java.sql.*;

public class UserService {


    public static User getCustomerByUserId(Long id) {

        Connection connection = Base.getConnection();
        String query = "select * from get_user(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.get(resultSet);
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addUser(Long id, String firstName, String lastName, String userName, String contact) {

        Connection connection = Base.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from add_user(?,?,?,?,?)");
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, userName);
            preparedStatement.setString(5, String.valueOf(contact));
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
