package mappper;

import domain.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper {

    public static Product mapToObjectOfProduct(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getInt("id"),resultSet.getString("name"),
                resultSet.getInt("number"),resultSet.getInt("price"),
                resultSet.getString("category_name"));
    }
}
