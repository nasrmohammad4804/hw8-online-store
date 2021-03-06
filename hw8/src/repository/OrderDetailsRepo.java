package repository;

import domain.OrderDetails;
import domain.Product;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDetailsRepo implements Operation<OrderDetails> {

    private final String CREATE_TABLE = "create table if not exists orderDetails(order_id int,product_id int,product_number int ,price int," +
            "foreign key(order_id) references orders(id) , foreign key(product_id ) references product(id)   )"; //

    private final String ADD_ORDER_OF_DETAILS = "insert into orderDetails(order_id,product_id, product_number,price) values(?,?,?,?) ";

    @Override
    public void createTable() {
        try (Statement statement = ApplicationContext.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_TABLE);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void add(OrderDetails orderDetails) throws SQLException {

        PreparedStatement preparedStatement;

        for (Product p : orderDetails.getList()) {

            preparedStatement = ApplicationContext.getConnection().prepareStatement(ADD_ORDER_OF_DETAILS);

            preparedStatement.setInt(1, orderDetails.getId());
            preparedStatement.setInt(2, p.getId());
            preparedStatement.setInt(3, p.getNumberOfProduct());
            preparedStatement.setInt(4, p.getPrice());
            preparedStatement.executeUpdate();


        }
    }

    @Override
    public int size() throws SQLException {
        int counter = 0;

        Statement statement = ApplicationContext.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        ResultSet resultSet = statement.executeQuery("select count(*) from orderDetails ");
        if (resultSet.last())
            counter = resultSet.getRow();

        return counter;
    }


}