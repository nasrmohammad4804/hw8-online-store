package repository;

import domain.Basket;
import domain.Product;
import service.ApplicationContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class BasketRepo implements SpecificOperation<Basket,Integer>{
    private final String CREATE_TABLE="create table if not exists basket(id int primary key auto_increment ," +
            "product_id int , customer_id int , number_product int , price int , status varchar (50) ," +
            "foreign key(product_id) references product(id) , foreign key(customer_id) references customer(id)   )";

    private final String ADD_BASKET="insert into basket(product_id,number_product,customer_id ,price,status) values(?,?,?,?,'payment')";
    private final String SIZE="select count(*) from basket";

    private final String CHANGE_NUMBER_OF_PRODUCT_IN_BASKET="update basket set number_product =? where customer_id=? and product_id=? and status='payment' ";

    private final String DELETE_FROM_BASKET="delete from basket where customer_id=? and product_id=? and status='payment'";

    private final String SHOW_ALL_PRODUCT_IN_BASKET_OF_CUSTOMER="select b.*, p.name as name from basket as b join product as p on p.id=b.product_id where customer_id=? and status='payment' ";

    private final String   CHANGE_STATUS="update basket set status='complete' where customer_id=? and product_id=? ";


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



    @Override
    public void increaseProductNumber(Integer... element) throws SQLException {

        int customerID=element[0];
        int productID=element[1];

        int count=getNumberOfProduct(customerID,productID)+1;

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(CHANGE_NUMBER_OF_PRODUCT_IN_BASKET);

        preparedStatement.setInt(1,count);
        preparedStatement.setInt(2,customerID);
        preparedStatement.setInt(3,productID);
        preparedStatement.executeUpdate();

    }

    @Override
    public void decreaseProductNumber(Integer... element) throws SQLException {

        int customerID=element[0];
        int productID=element[1];
        int result=NumberOfProductInBasketForCustomer(customerID,productID);

        --result;
        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(CHANGE_NUMBER_OF_PRODUCT_IN_BASKET);
        preparedStatement.setInt(1,result);
        preparedStatement.setInt(2,customerID);
        preparedStatement.setInt(3,productID);
        preparedStatement.executeUpdate();
    }



    @Override
    public List<Product> getAllProduct(Integer ... element) {

        int customerId=element[0];


        List<Product> list=new LinkedList<>();

        try(PreparedStatement statement=ApplicationContext.getConnection().prepareStatement(SHOW_ALL_PRODUCT_IN_BASKET_OF_CUSTOMER)) {

            statement.setInt(1,customerId);
            ResultSet resultSet=statement.executeQuery();

            while (resultSet.next()){

                list.add(new Product(resultSet.getInt("product_id"),resultSet.getString("name"),
                        resultSet.getInt("number_product"),resultSet.getInt("price")));
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return list;

    }

    public int NumberOfProductInBasketForCustomer(int customer_id, int product_id) throws SQLException {

        int counter=0;

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement("select  number_product from basket where customer_id=? and product_id=?  and  status='payment' ");

        preparedStatement.setInt(1,customer_id);
        preparedStatement.setInt(2,product_id);

        ResultSet resultSet=preparedStatement.executeQuery();

        while (resultSet.next()){
            counter=resultSet.getInt("number_product");
        }
        return counter;




    }

    public int checkNumberOfProductInBasket(int customer_id){
        int counter=0;
        try(PreparedStatement statement=ApplicationContext.getConnection().prepareStatement("select  * from basket where customer_id=? and status='payment'  ")) {

            statement.setInt(1,customer_id);
            ResultSet resultSet =statement.executeQuery();
            while (resultSet.next())
                counter++;

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return counter;

    }

    public void addNumberOfProduct(int customer_id , int product_id) throws SQLException {
        int count=getNumberOfProduct(customer_id,product_id)+1;

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(CHANGE_NUMBER_OF_PRODUCT_IN_BASKET);

        preparedStatement.setInt(1,count);
        preparedStatement.setInt(2,customer_id);
        preparedStatement.setInt(3,product_id);
        preparedStatement.executeUpdate();

    }

    @Override
    public void add(Basket basket) throws SQLException {
        //product_id,number_product,customer_id ,price,status) values(?,?,?,?,'payment')";

        System.out.println(basket.getCustomer().getId());
        System.out.println(basket.getCustomer().getId());
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


    public void delete(Basket x) {

        try(PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(DELETE_FROM_BASKET)) {

            preparedStatement.setInt(1,x.getCustomer().getId());
            preparedStatement.setInt(2,x.getProduct().getId());
            preparedStatement.executeUpdate();
            System.out.println("successfully delete from basket");

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void confirmBasket(int customer_id, List<Product> list) throws SQLException {

        PreparedStatement preparedStatement=ApplicationContext.getConnection().prepareStatement(CHANGE_STATUS);
        for(Product p : list){

            preparedStatement.setInt(1,customer_id);
            preparedStatement.setInt(2,p.getId());
            preparedStatement.executeUpdate();
        }

    }
}