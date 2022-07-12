package uz.pdp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface Base {

    String url = "jdbc:postgresql://localhost:5432/mvctelegrambot";
    String dbUser = "postgres";
    String dbPassword = "12345";

    static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(
                    url,
                    dbUser,
                    dbPassword
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
