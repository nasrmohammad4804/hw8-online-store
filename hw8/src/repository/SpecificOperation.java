package repository;

import domain.Product;

import java.sql.SQLException;
import java.util.List;

public interface SpecificOperation <T,E> extends Operation<T>{

    void increaseProductNumber(E ... element) throws SQLException;

    void decreaseProductNumber(E ... element) throws SQLException;

    List<Product> getAllProduct(E ... element);
}
