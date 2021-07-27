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

        System.out.println("\n\n");
    }

    public static void addProduct(Customer customer) {

        if (ApplicationContext.getBasketRepo().checkNumberOfProductInBasket(customer.getId()) >= 5) {
            System.out.println("$$capacity is full if you want add product at least remove one already product$$\n\n ");
            return;
        }
        showAllProduct();

        String productName = "";
        while (true) {
            try {
                System.out.println("enter productName ...");
                productName = scanner.nextLine();
                if (ApplicationContext.getProductRepo().getProduct(productName) == null) {
                    System.out.println("this name not exists for product ... try again\n");
                    continue;
                }
                break;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        try {

            if (ApplicationContext.getBasketRepo().NumberOfProductInBasketForCustomer(customer.getId(), ApplicationContext.getProductRepo().getProduct(productName).getId()) == 0) {
                ApplicationContext.getBasketRepo().add(new Basket(customer, ApplicationContext.getProductRepo().getProduct(productName)));
                ApplicationContext.getProductRepo().decreaseProductNumber(productName);
            } else {
                if (ApplicationContext.getProductRepo().getProduct(productName).getNumberOfProduct() == 0) {
                    System.out.println("sorry dont have any of this product");
                } else {
                    ApplicationContext.getBasketRepo().addNumberOfProduct(customer.getId(), ApplicationContext.getProductRepo().getProduct(productName).getId());
                    ApplicationContext.getProductRepo().decreaseProductNumber(productName);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            addProduct(customer);
        }
    }

    public static void removeProduct(Customer customer) throws SQLException {
        String name = "";
        while (true) {
            System.out.println("enter productName ...");
            name = scanner.nextLine();
            if (ApplicationContext.getProductRepo().getProduct(name) == null) {
                System.out.println("this name not exists for product ... try again\n");
                continue;
            }
            break;
        }


        if (ApplicationContext.getBasketRepo().getNumberOfProduct(customer.getId(), ApplicationContext.getProductRepo().getProduct(name).getId()) == 0) {
            System.out.println("!!!  impossible to removeProduct  !!!\n\n");
            return;

        } else if (ApplicationContext.getBasketRepo().getNumberOfProduct(customer.getId(), ApplicationContext.getProductRepo().getProduct(name).getId()) == 1) {

            ApplicationContext.getBasketRepo().delete(new Basket(customer, ApplicationContext.getProductRepo().getProduct(name)));
            ApplicationContext.getProductRepo().increaseProductNumber(name);
        } else {
            ApplicationContext.getBasketRepo().decreaseProductNumber(customer.getId(), ApplicationContext.getProductRepo().getProduct(name).getId());
            ApplicationContext.getProductRepo().increaseProductNumber(name);
        }
    }

    public static void showAllProductInBasket(Customer customer) {

        if (ApplicationContext.getBasketRepo().getAllProduct(customer.getId()).isEmpty()) {
            System.out.println("basket is empty  \n\n");
            return;
        }

        System.out.println("id     productName     number       price        status\n");
        for (Product p : ApplicationContext.getBasketRepo().getAllProduct(customer.getId())) {
            System.out.printf("%-10d %-14s %-10d %-10d   %s\n", p.getId(), p.getName(), p.getNumberOfProduct(), p.getPrice(), "payment");
        }
        System.out.println("\n\n");
    }

    public static void showTotalPrice(Customer customer) {

        long total=  ApplicationContext.getBasketRepo().getAllProduct(customer.getId()).stream().map(x -> x.getNumberOfProduct()*x.getPrice()).reduce(Integer::sum).get();

        ApplicationContext.getBasketRepo().getAllProduct(customer.getId()).stream().map(x ->
                x.getId() + "    " + x.getNumberOfProduct() + "    " + x.getPrice() + "    " + "total price on productName  " + x.getName() + " is : " + (x.getNumberOfProduct() * x.getPrice()))
                .forEach(System.out::println);

        System.out.println("--------------------------------------\n");
        System.out.println("total of all price is : " + total+"\n\n");
    }

    public static void confirmForAddToOrder(Customer customer) {

        if (ApplicationContext.getBasketRepo().getAllProduct(customer.getId()).isEmpty()) {
            System.out.println("basket is empty \n\n");
            return;
        }

        System.out.println("are you want to confirm basket ???\nif you want press yes ");
        String result = scanner.nextLine();

        if (result.equals("yes")) {
            try {
                ApplicationContext.getConnection().setAutoCommit(false);

                ApplicationContext.getOrderRepo().add(new Order(customer, Timestamp.valueOf(LocalDateTime.now())));

                ApplicationContext.getOrderDetailsRepo().add(new OrderDetails(customer, Timestamp.valueOf(LocalDateTime.now()),
                        ApplicationContext.getOrderRepo().size(), ApplicationContext.getBasketRepo().getAllProduct(customer.getId())));


                ApplicationContext.getBasketRepo().confirmBasket(customer.getId(), ApplicationContext.getBasketRepo().getAllProduct(customer.getId()));

                ApplicationContext.getConnection().commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
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
        } else {
            System.out.println("try again ....");
            confirmForAddToOrder(customer);
        }
    }

}
