package repository;

import com.sun.security.jgss.GSSUtil;
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

    private final String SIZE = "select count(*) as number  from orders";

    private final String ALL_ORDER = " select  od.*,orderDate,name from orders as o join orderdetails as od on od.order_id=o.id join product as p on p.id=od.product_id  where o.customer_id=? ";

    @Override
    public void createTable() {

        try (Statement statement = ApplicationContext.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_TABLE);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    public void showAllOrderedProduct(int customer_id) throws SQLException {

        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(ALL_ORDER, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        preparedStatement.setInt(1, customer_id);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.last()) {

            resultSet.beforeFirst();
            System.out.println("order_id    product_id    product_number     price          orderDate             productName");

            while (resultSet.next()) {
                System.out.printf("  %-15d %-13d %-12d %-11d %-27s %s\n", resultSet.getInt("order_id"),
                        resultSet.getInt("product_id"), resultSet.getInt("product_number"),
                        resultSet.getInt("price"), resultSet.getString("orderDate"),
                        resultSet.getString("name"));
            }
        } else {
            System.out.println("not exists any ordered ...");
        }
        System.out.println("\n");

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
        int counter = 0;
        try {

            Statement statement = ApplicationContext.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SIZE);
            while (resultSet.next())
                counter=resultSet.getInt("number");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return counter;
    }

    @Override
    public void delete(Order x) {
        // ... TODO im working after
    }
}
