package repository;

import domain.Order;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderRepo implements Operation<Order> {

    private final String CREATE_TABLE = "create table if not exists orders(id int primary key auto_increment ," +
            "customer_id int,orderDate timestamp, foreign key(customer_id) references customer(id) )";

    private final String ADD_ORDER = "insert into orders(customer_id , orderDate ) values(?,?)";

    private final String SIZE="select count(*) from orders";

    @Override
    public void createTable() {

        try (Statement statement = ApplicationContext.getConnection().createStatement()) {
            statement.executeQuery(CREATE_TABLE);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }

    }

    @Override
    public void add(Order order) throws SQLException {
        // throw rather than try and catch because use transaction ...
        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(ADD_ORDER);
        preparedStatement.setInt(1, order.getCustomer().getId());
        preparedStatement.setTimestamp(2, order.getOrderDate());

        preparedStatement.executeUpdate();

    }

    @Override
    public int size() {
        int counter=0;
        try {

            Statement statement = ApplicationContext.getConnection().createStatement();
            ResultSet resultSet=statement.executeQuery(SIZE);
            while (resultSet.next())
                counter++;
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
        return counter;
    }

    @Override
    public void delete(Order x) {
        // ... TODO im working after
    }
}
