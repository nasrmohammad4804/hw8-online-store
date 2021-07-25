package repository;

import domain.Product;
import mappper.ProductMapper;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo {
    private final String CREATE_TABLE="create table if not exists product(id int primary key auto_increment ," +
            "name varchar(50) not null , number int not null , price int not null,category_name varchar(50) not null)";

    private final String  GET_ALL_PRODUCT="select * from product";

    private final String ADD_PRODUCT="insert into product(name,number,price,category_name) values(?,?,?,?)";

    public void createTable(){
        try(Statement statement= ApplicationContext.getConnection().createStatement()) {
            statement.executeQuery(CREATE_TABLE);

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }

    }
    public List<Product> getAllProduct(){
        List<Product> myList=new ArrayList<>();

        try(Statement statement = ApplicationContext.getConnection().createStatement()) {

            ResultSet resultSet =statement.executeQuery(GET_ALL_PRODUCT);

            while (resultSet.next()){
                myList.add(ProductMapper.mapToObjectOfProduct(resultSet));
            }
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }

        return myList;
    }
    public void addDefaultProductToMarket(){

        try(PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(ADD_PRODUCT)) {
            //insert into product(name,number,price,category_name) values(?,?,?,?)
            preparedStatement.setString(1,"radio");
            preparedStatement.setInt(2,10);
            preparedStatement.setInt(3,5000);
            preparedStatement.setString(4,"electronic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1,"television");
            preparedStatement.setInt(2,10);
            preparedStatement.setInt(3,7000);
            preparedStatement.setString(4,"electronic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1,"sport shoes");
            preparedStatement.setInt(2,10);
            preparedStatement.setInt(3,2000);
            preparedStatement.setString(4,"shoes");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1,"official");
            preparedStatement.setInt(2,10);
            preparedStatement.setInt(3,2500);
            preparedStatement.setString(4,"shoes");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1,"book");
            preparedStatement.setInt(2,10);
            preparedStatement.setInt(3,1000);
            preparedStatement.setString(4,"readable");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1,"magazine");
            preparedStatement.setInt(2,10);
            preparedStatement.setInt(3,700);
            preparedStatement.setString(4,"readable");
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
    }

}
