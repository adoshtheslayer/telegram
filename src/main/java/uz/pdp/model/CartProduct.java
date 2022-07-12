package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartProduct implements Base {
    private Long userId;
    private Integer productId;
    private Integer amount;


    @Override
    public void get(ResultSet resultSet) {
        try {
            this.userId = Long.valueOf(resultSet.getString(1));
            this.productId = resultSet.getInt(2);
            this.amount = resultSet.getInt(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
