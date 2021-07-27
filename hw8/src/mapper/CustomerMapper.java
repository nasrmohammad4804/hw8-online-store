package mapper;

import domain.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper {

    public static Customer mapToObjectOfCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(resultSet.getString("name"),
                resultSet.getString("family"),resultSet.getString("username"),
                resultSet.getString("password"),resultSet.getInt("id"));
    }
}
