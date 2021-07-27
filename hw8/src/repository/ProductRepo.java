package repository;

import domain.Product;
import mapper.ProductMapper;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo implements SpecificOperation<Product,String> {
    private final String CREATE_TABLE = "create table if not exists product(id int primary key auto_increment ," +
            "name varchar(50) not null unique , number int not null , price int not null,category_name varchar(50) not null)";

    private final String GET_ALL_PRODUCT = "select * from product";

    private final String ADD_PRODUCT = "insert into product(name,number,price,category_name) values(?,?,?,?)";

    private final String GET_PRODUCT_WITH_NAME = "select * from product where name=?";

    private final String CHANGE_OF_PRODUCT_NUMBER = "update product set number=? where name=? ";

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
    public void add(Product product)  {
        System.out.println("product now not able to add");
    }

    @Override
    public int size() throws SQLException {

        Statement statement=ApplicationContext.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("select * from product");
        int counter=0;

        while (resultSet.next())
            counter++;

        return counter;
    }

    @Override
    public void increaseProductNumber(String... element) {

        String name=element[0];
        Product product = null;
        try {
            product= getProduct(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            increaseProductNumber(name);
        }

        assert product != null;
        int number = product.getNumberOfProduct()+ 1;

        try (PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(CHANGE_OF_PRODUCT_NUMBER)) {

            preparedStatement.setInt(1, number);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void decreaseProductNumber(String... element) {

        String name=element[0];

        Product p = null;
        try {
            p= getProduct(name);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            decreaseProductNumber(name);
        }

        assert p != null;
        int number = p.getNumberOfProduct() - 1;

        try (PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(CHANGE_OF_PRODUCT_NUMBER)) {

            preparedStatement.setInt(1, number);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public List<Product> getAllProduct(String... element) {
        List<Product> myList = new ArrayList<>();

        try (Statement statement = ApplicationContext.getConnection().createStatement()) {

            ResultSet resultSet = statement.executeQuery(GET_ALL_PRODUCT);

            while (resultSet.next()) {
                myList.add(ProductMapper.mapToObjectOfProduct(resultSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return myList;
    }

    public Product getProduct(String name) throws SQLException {

        PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(GET_PRODUCT_WITH_NAME);
        preparedStatement.setString(1, name);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            return ProductMapper.mapToObjectOfProduct(resultSet);
        }
        return null;
    }


    public void addDefaultProductToMarket() {

        try (PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(ADD_PRODUCT)) {
            //insert into product(name,number,price,category_name) values(?,?,?,?)
            preparedStatement.setString(1, "radio");
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, 5000);
            preparedStatement.setString(4, "electronic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "television");
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, 7000);
            preparedStatement.setString(4, "electronic");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "sport shoes");
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, 2000);
            preparedStatement.setString(4, "shoes");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "official");
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, 2500);
            preparedStatement.setString(4, "shoes");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "book");
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, 1000);
            preparedStatement.setString(4, "readable");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "magazine");
            preparedStatement.setInt(2, 10);
            preparedStatement.setInt(3, 700);
            preparedStatement.setString(4, "readable");
            preparedStatement.executeUpdate();

            System.out.println("add default product to market !!!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

}
