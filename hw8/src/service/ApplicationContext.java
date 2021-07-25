package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ApplicationContext {
    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }

    public ApplicationContext(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/hw8", "root",
                    "MohammadN@sr13804804");
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
        }
    }
}
