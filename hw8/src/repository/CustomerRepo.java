package repository;

import domain.Customer;
import mappper.CustomerMapper;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerRepo implements Operation<Customer> {

    private final String LOGIN_CUSTOMER = "select * from customer where username=? and password=? ";

    private final String REGISTER = "select * from customer where username=?";

    private final String CREATE_TABLE = "create table if not exists customer(id int primary key auto_increment," +
            "name varchar(50) not null , family varchar(50) not null , username varchar(50) not null unique ," +
            "password varchar(50) not null )";

    private final String ADD_CUSTOMER = "insert into customer(name,family,username,password) values(?,?,?,?)";

    private final String SIZE = "select * from customer";

    private final String LAST_CUSTOMER="select * from customer where id=? ";


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
    public void add(Customer customer) {


        try {
            PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(ADD_CUSTOMER);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getFamily());
            preparedStatement.setString(3, customer.getUserName());
            preparedStatement.setString(4, customer.getPassword());
            preparedStatement.executeUpdate();

            preparedStatement.close();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
    }

    @Override
    public int size() {
        try (Statement statement = ApplicationContext.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

            ResultSet resultSet = statement.executeQuery(SIZE);
            int counter = 0;

            if (resultSet.last())
                counter = resultSet.getRow();

            return counter;

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            return 0;
        }
    }

    @Override
    public void delete(Customer x) {
        // ....
    }

    public Customer registerCheck(Customer customer) {

        return getCustomerForRegister(customer);
    }
    public Customer getLastCustomer(){
        try(PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(LAST_CUSTOMER)) {

            preparedStatement.setInt(1,size());
            ResultSet resultSet=preparedStatement.executeQuery();
            return CustomerMapper.mapToObjectOfCustomer(resultSet);

        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }

        return null;
    }
    public Customer getCustomerForRegister(Customer cus) {

        int counter=0;
        try (PreparedStatement statement = ApplicationContext.getConnection().prepareStatement(REGISTER)) {
            statement.setString(1,cus.getUserName());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               counter++;
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        if(counter>0)
        return null;

        return cus;


    }

    public Customer loginCustomer(String userName, String password) {

        return getCustomerForLogin(userName, password); //TODO mybe return null if customer not exists
    }

    public Customer getCustomerForLogin(String userName, String password) {

        Customer customer = null;
        try (PreparedStatement preparedStatement = ApplicationContext.getConnection().prepareStatement(LOGIN_CUSTOMER);) {

            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customer = CustomerMapper.mapToObjectOfCustomer(resultSet);
            }

        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return customer;
    }
}
