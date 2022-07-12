package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category implements Base{
    private Integer id;
    private String name;

    public void get(ResultSet resultSet) {

        try {
            this.id = resultSet.getInt(1);
            this.name = resultSet.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
