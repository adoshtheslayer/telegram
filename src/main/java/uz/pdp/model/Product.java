package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product implements Base{
    private Integer id;
    private Integer categoryId;
    private String name;
    private Double price = 0d;
    private String image;

    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt(1);
            this.categoryId = resultSet.getInt(2);
            this.name = resultSet.getString(3);
            this.price = resultSet.getDouble(4);
            this.image = resultSet.getString(5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
