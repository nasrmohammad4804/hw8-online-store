package repository;

import domain.OrderDetails;
import domain.Product;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDetailsRepo implements Operation<OrderDetails> {

    private final String CREATE_TABLE = "create table if not exists orderDetails(order_id int,product_id int,product_number int ,price int,foreign key" +
            "(order_id) references orders(id) , foreign_key(product_id ) references product(id)  , primary key (order_id,product_id)  )";

    private final String ADD_ORDER_OF_DETAILS = "insert into orderDetails(order_id,product_id, product_number,price) values(?,?,?,?) ";

    @Override
    public void createTable() {
        try (Statement statement = ApplicationContext.getConnection().createStatement()) {
            statement.executeQuery(CREATE_TABLE);

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }

    @Override
    public void add(OrderDetails orderDetails) throws SQLException {

        for (Product p : orderDetails.getList()) {  //TODO for transaction with Order table  witch customer add order
            try (PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(ADD_ORDER_OF_DETAILS)) {
                preparedStatement.setInt(1, orderDetails.getOrder_id());
                preparedStatement.setInt(2, p.getId());
                preparedStatement.setInt(3, p.getNumberOfProduct());
                preparedStatement.setInt(4, p.getPrice());
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                System.out.println(e.getErrorCode());
            }
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

    @Override
    public void delete(OrderDetails x) {
        // TODO  after program design this
    }
}
