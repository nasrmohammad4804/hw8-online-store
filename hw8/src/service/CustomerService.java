package service;

import domain.Customer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CustomerService {
    private static Scanner scannerForString = new Scanner(System.in);



    public static Customer register() {

        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", "");
        map.put("family", "");
        map.put("userName", "");
        map.put("password", "");


        for (Map.Entry<String, String> str : map.entrySet()) {
            while (true) {
                try {
                    System.out.println("enter " + str.getKey());
                    map.put(str.getKey(), scannerForString.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        Customer c = new Customer(map.get("name"), map.get("family"), map.get("userName"), map.get("password"));
        return ApplicationContext.getCustomerRepo().registerCheck(c);
    }
    public static Customer login(){

        Map<String,String> map=new LinkedHashMap<>();
        map.put("userName","");
        map.put("password","");
        for (Map.Entry<String, String> ptr  : map.entrySet()) {
            while (true) {
                try {
                    System.out.println("enter " + ptr.getKey());
                    map.put(ptr.getKey(), scannerForString.nextLine());
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return ApplicationContext.getCustomerRepo().loginCustomer(map.get("userName"),map.get("password"));
    }
}
