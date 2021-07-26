package service;

import repository.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationContext {
    private static Connection connection;
    private static CustomerRepo customerRepo;
    private static OrderDetailsRepo orderDetailsRepo;
    private static OrderRepo orderRepo;
    private static ProductRepo productRepo;
    private static BasketRepo basketRepo;

    public static Connection getConnection() {
        return connection;
    }

    public static CustomerRepo getCustomerRepo() {
        return customerRepo;
    }

    public static OrderDetailsRepo getOrderDetailsRepo() {
        return orderDetailsRepo;
    }

    public static OrderRepo getOrderRepo() {
        return orderRepo;
    }

    public static ProductRepo getProductRepo() {
        return productRepo;
    }

    public static BasketRepo getBasketRepo() {
        return basketRepo;
    }

    public ApplicationContext(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hw8", "root",
                    "MohammadN@sr13804804");
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }

        customerRepo=new CustomerRepo();
        orderDetailsRepo=new OrderDetailsRepo();
        orderRepo=new OrderRepo();
        productRepo=new ProductRepo();
        basketRepo=new BasketRepo();
    }
}
