package service;

import domain.Basket;
import domain.Customer;
import domain.Product;

import java.util.Scanner;

public class BasketService {

    private final static Scanner scanner=new Scanner(System.in);

    public static void showAllProduct() {
        for (Product p : ApplicationContext.getProductRepo().getAllProduct())
            System.out.println(p);
    }
    public static void addProduct(Customer customer){
        showAllProduct();
        System.out.println("enter productName");
        String name=scanner.nextLine();

        try {

            if (ApplicationContext.getBasketRepo().NumberOfProductInBasketForCustomer(customer,ApplicationContext.getProductRepo().getProduct(name).getId()) ==0){
                ApplicationContext.getBasketRepo().add(new Basket(customer,ApplicationContext.getProductRepo().getProduct(name)));
                ApplicationContext.getProductRepo().removeNumberOfProduct(name);
            }


            else {
                if(ApplicationContext.getProductRepo().getProduct(name).getNumberOfProduct()==0){
                    System.out.println("sorry dont have any of this product");
                }
                else{
                    ApplicationContext.getBasketRepo().addNumberOfProduct(customer.getId(),ApplicationContext.getProductRepo().getProduct(name).getId());
                    ApplicationContext.getProductRepo().removeNumberOfProduct(name);
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            addProduct(customer);
        }
    }

}
