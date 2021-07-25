package domain;

import java.sql.Timestamp;

public class Order {
    protected int id;
    protected Customer customer;
    protected Timestamp orderDate;

    public Order(int id, Customer customer, Timestamp orderDate) {
        this.id = id;
        this.customer = customer;
        this.orderDate = orderDate;
    }

    public Order(Customer customer, Timestamp orderDate) {
        this.customer = customer;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", orderDate=" + orderDate +
                '}';
    }
}
