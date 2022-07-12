package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.enums.OrderStatus;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order implements Base {
    private Integer id;
    private Long userId;
    private Double totalPrice;
    private String createdAt;
    private OrderStatus status;

    public void get(ResultSet resultSet) {
        try {
            this.id = resultSet.getInt(1);
            this.userId = resultSet.getLong(2);
            this.totalPrice = resultSet.getDouble(3);
            this.createdAt = resultSet.getString(4);
            this.status = OrderStatus.valueOf(resultSet.getString(5));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
