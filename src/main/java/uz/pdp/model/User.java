package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Base {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String phoneNumber;

    @Override
    public void get(ResultSet resultSet) {

        try {
            this.id = resultSet.getLong(1);
            this.firstName = resultSet.getString(2);
            this.lastName = resultSet.getString(3);
            this.userName = resultSet.getString(4);
            this.phoneNumber = resultSet.getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
