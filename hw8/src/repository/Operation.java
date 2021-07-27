package repository;

import java.sql.SQLException;

public interface Operation<T> {

    void createTable();

    void add(T t) throws SQLException;

    int size() throws SQLException;


}