package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.enums.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetail implements Base {
    private Integer id;
    private Integer orderId;
    private String productName;
    private Double price;
    private Integer amount;
    private Double totalPrice;
    private String status;

    public void get(ResultSet resultSet) {

        try {
            this.id = resultSet.getInt(1);
            this.orderId = resultSet.getInt(2);
            this.productName = resultSet.getString(3);
            this.price = resultSet.getDouble(4);
            this.amount = resultSet.getInt(5);
            this.totalPrice = resultSet.getDouble(6);
            this.status = resultSet.getString(7);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
