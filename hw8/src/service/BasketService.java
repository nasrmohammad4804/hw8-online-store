package service;

import domain.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class BasketService {

    private final static Scanner scanner = new Scanner(System.in);

    public static void showAllProduct() {
        for (Product p : ApplicationContext.getProductRepo().getAllProduct())
            System.out.println(p);
    }

    public static void addProduct(Customer customer) {

        if (ApplicationContext.getBasketRepo().checkNumberOfProductInBasket(customer.getId()) > 5) {
            System.out.println("capacity is full if you want add product at least remove one already product ");
            return;
        }
        showAllProduct();
        System.out.println("enter productName");
        String name = scanner.nextLine();

        try {

            if (ApplicationContext.getBasketRepo().NumberOfProductInBasketForCustomer(customer, ApplicationContext.getProductRepo().getProduct(name).getId()) == 0) {
                ApplicationContext.getBasketRepo().add(new Basket(customer, ApplicationContext.getProductRepo().getProduct(name)));
                ApplicationContext.getProductRepo().removeNumberOfProduct(name);
            } else {
                if (ApplicationContext.getProductRepo().getProduct(name).getNumberOfProduct() == 0) {
                    System.out.println("sorry dont have any of this product");
                } else {
                    ApplicationContext.getBasketRepo().addNumberOfProduct(customer.getId(), ApplicationContext.getProductRepo().getProduct(name).getId());
                    ApplicationContext.getProductRepo().removeNumberOfProduct(name);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            addProduct(customer);
        }
    }

    public static void removeProduct(Customer customer) throws SQLException {

        System.out.println("enter productName ...");
        String name = scanner.nextLine();
        if (ApplicationContext.getBasketRepo().getNumberOfProduct(customer.getId(), ApplicationContext.getProductRepo().getProduct(name).getId()) == 0) {
            System.out.println("impossible to removeProduct");
            return;

        } else if (ApplicationContext.getBasketRepo().getNumberOfProduct(customer.getId(), ApplicationContext.getProductRepo().getProduct(name).getId()) == 1) {

            ApplicationContext.getBasketRepo().delete(new Basket(customer, ApplicationContext.getProductRepo().getProduct(name)));
            ApplicationContext.getProductRepo().addNumberOfProduct(name);
        } else {
            ApplicationContext.getBasketRepo().decreaseProductNumber(customer, ApplicationContext.getProductRepo().getProduct(name).getId());
            ApplicationContext.getProductRepo().addNumberOfProduct(name);
        }
    }

    public static void showAllProductInBasket(Customer customer) {
        System.out.println("id     productName     number       price        status\n");
        for (Product p : ApplicationContext.getBasketRepo().getAllProductInBasket(customer.getId())) {
            System.out.printf("%-10d %-14s %-10d %-10d   %s\n", p.getId(), p.getName(), p.getNumberOfProduct(), p.getPrice(), "payment");
        }
    }

    public static void showTotalPrice(Customer customer) {

        double total = 0;

        for (Product p : ApplicationContext.getBasketRepo().getAllProductInBasket(customer.getId())) {
            total += (p.getPrice() * p.getNumberOfProduct());
            System.out.println(p.getId() + "    " + p.getNumberOfProduct() + "    " + p.getPrice() + "    " + "total price on productName  " + p.getName() + " is : " + (p.getNumberOfProduct() * p.getPrice()));
        }
        System.out.println("--------------------------------------\n");
        System.out.println("total of all price is : " + total);
    }

    public static void confirmForAddToOrder(Customer customer) {
        System.out.println("are you want to confirm basket ???\nif you want press yes ");
        String result = scanner.nextLine();

        if (result.equals("yes")) {
            try {
                ApplicationContext.getConnection().setAutoCommit(false);


                ApplicationContext.getBasketRepo().confirmBasket(customer.getId(), ApplicationContext.getBasketRepo().getAllProductInBasket(customer.getId()));

                ApplicationContext.getOrderRepo().add(new Order(customer, Timestamp.valueOf(LocalDateTime.now())));

                ApplicationContext.getOrderDetailsRepo().add(new OrderDetails(customer, Timestamp.valueOf(LocalDateTime.now()),
                        ApplicationContext.getOrderRepo().size(), ApplicationContext.getBasketRepo().getAllProductInBasket(customer.getId())));

                ApplicationContext.getConnection().commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                try {
                    ApplicationContext.getConnection().rollback();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }

            try {
                ApplicationContext.getConnection().setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else {
            System.out.println("try again ....");
            confirmForAddToOrder(customer);
        }
    }

}
