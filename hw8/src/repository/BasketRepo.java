package repository;


import com.mysql.cj.protocol.x.XProtocolError;
import domain.Basket;
import domain.Customer;
import jdk.jshell.spi.SPIResolutionException;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BasketRepo implements Operation<Basket>{
    private final String CREATE_TABLE="create table if not exists basket(id int primary key auto_increment ," +
            "product_id int , customer_id int , number_product int , price int , status varchar (50) ," +
            "foreign key(product_id) references product(id) , foreign key(customer_id) references customer(id)   )";

    private final String ADD_BASKET="insert into basket(product_id,number_product,customer_id ,price,status) values(?,?,?,?,'payment')";
    private final String SIZE="select count(*) from basket";

    private final String ADD_NUMBER_OF_PRODUCT_IN_BASKET="update basket set number_product =? where customer_id=? and product_id=?";


    @Override
    public void createTable() {
        try (Statement statement= ApplicationContext.getConnection().createStatement()) {
            statement.executeUpdate(CREATE_TABLE);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public int NumberOfProductInBasketForCustomer(Customer customer,int product_id) throws SQLException {

        int counter=0;

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("select  * from basket where customer_id=? and product_id=?  and  status='payment' ");

        preparedStatement.setInt(1,customer.getId());
        preparedStatement.setInt(2,product_id);

        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
           counter++;
        }
        return counter;




    }

    public void addNumberOfProduct(int customer_id , int product_id) throws SQLException {
        int count=getNumberOfProduct(customer_id,product_id)+1;

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(ADD_NUMBER_OF_PRODUCT_IN_BASKET);

        preparedStatement.setInt(1,count);
        preparedStatement.setInt(2,customer_id);
        preparedStatement.setInt(3,product_id);
        preparedStatement.executeUpdate();

    }

    @Override
    public void add(Basket basket) throws SQLException {
        //product_id,number_product,customer_id ,price,status) values(?,?,?,?,'payment')";
        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(ADD_BASKET);
        preparedStatement.setInt(1,basket.getProduct().getId());
        preparedStatement.setInt(2,1); //number_of product when add to table basket is : 1
        preparedStatement.setInt(3,basket.getCustomer().getId());
        preparedStatement.setInt(4,basket.getProduct().getPrice());

        preparedStatement.executeUpdate();
    }

    public int getNumberOfProduct(int customer_id ,int product_id) throws SQLException {
        PreparedStatement statement=ApplicationContext.getConnection().prepareStatement("select number_product from basket where customer_id=? and product_id=?");
        statement.setInt(1,customer_id);
        statement.setInt(2,product_id);

        ResultSet resultSet =statement.executeQuery();

        int result=0;

        while (resultSet.next()){
            result=resultSet.getInt("number_product");
        }
        return result;


    }

    @Override
    public int size()   {
       int counter=0;
       try(Statement statement =ApplicationContext.getConnection().createStatement()) {

           ResultSet resultSet =statement.executeQuery(SIZE);
           while (resultSet.next())
               counter++;

       }catch (SQLException e){
           System.out.println(e.getErrorCode());
       }
       return counter;
    }

    @Override
    public void delete(Basket x) {

    }
}
