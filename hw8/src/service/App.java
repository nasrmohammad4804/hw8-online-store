package service;

import domain.Customer;

import java.sql.SQLException;
import java.util.Scanner;

public class App {
    private Scanner scannerForString = new Scanner(System.in);
    private Scanner getScannerForInteger = new Scanner(System.in);

    public void start() {

        ApplicationContext str = new ApplicationContext();

        ApplicationContext.getCustomerRepo().createTable();
        ApplicationContext.getProductRepo().createTable();
        ApplicationContext.getOrderRepo().createTable();
        ApplicationContext.getOrderDetailsRepo().createTable();
        ApplicationContext.getBasketRepo().createTable();

        if (ApplicationContext.getProductRepo().getAllProduct().isEmpty())
            ApplicationContext.getProductRepo().addDefaultProductToMarket();
        try {

            menu();
        } catch (Exception e) {
            System.out.println("error occurred in operation ..");
        }
    }

    private void menu() {

        System.out.println("1.register");
        System.out.println("2.login");
        System.out.println("3.exit");

        int input = 0;
        while (true) {
            try {
                input = getScannerForInteger.nextInt();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        switch (input) {

            case 1 -> {
                Customer custom = CustomerService.register();
                if (custom == null) {
                    System.out.println("this user with data already exists !!!");
                    menu();
                } else {
                    ApplicationContext.getCustomerRepo().add(custom);
                    customerPanel(custom);
                }

            }
            case 2 -> {
                Customer customer = CustomerService.login();
                if (customer != null)
                    customerPanel(customer);
                else {
                    System.out.println("userName or password is wrong ...");
                }
            }
            case 3 -> {
                System.out.println("have nice day ##");
                System.exit(0);
            }
            default -> {
                System.out.println("your input invalid try again ");
                menu();
            }
        }
    }

    public void customerPanel(Customer customer) {

        System.out.println("welcome " + customer.getName() + "   " + customer.getFamily() + ")))\n\n");

        System.out.println("\n\n");
        System.out.println("1.add product  to basket");
        System.out.println("2.remove product from basket");
        System.out.println("3.show all product witch add to basket with number of their");
        System.out.println("4.show total price ");
        System.out.println("5.final confirm of customer ");
        System.out.println("6.all product");
        System.out.println("7.back to home");


        switch (resultOfInputOnCustomerPanel()) {

            case 1 -> {
                BasketService.addProduct(customer);
                customerPanel(customer);
            }
            case 2 -> {
                try {
                    BasketService.removeProduct(customer);
                    customerPanel(customer);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            case 3 ->{
                BasketService.showAllProductInBasket(customer);
                customerPanel(customer);
            }

            case 4 ->{
                BasketService.showTotalPrice(customer);
                customerPanel(customer);
            }

            case 5 ->{
                BasketService.showAllProductInBasket(customer);
                BasketService.confirmForAddToOrder(customer);
            }
            case 6 ->{
                BasketService.showAllProduct();
                customerPanel(customer);
            }

            case 7 -> menu();
        }
    }


    public int resultOfInputOnCustomerPanel() {
        int input = 0;
        while (true) {
            try {
                System.out.println("enter your choice ...");
                input = getScannerForInteger.nextInt();

                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return input;

    }

}
