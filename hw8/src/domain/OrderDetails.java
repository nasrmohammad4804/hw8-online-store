package domain;

import java.sql.Timestamp;
import java.util.List;

public class OrderDetails extends Order {

    private int order_id;
    private List<Product> list;

    public OrderDetails(int id, Customer customer, Timestamp orderDate, int order_id, List<Product> list) {
        super(order_id, customer, orderDate);
        this.id = id;
        this.list=list;
    }
    public OrderDetails(Customer customer, Timestamp orderDate ,int order_id ,List<Product> list){
        super(order_id,customer,orderDate);
        this.list=list;
    }



    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "id=" + id +
                ", customer=" + customer +
                ", orderDate=" + orderDate +
                ", allProduct=" + list +
                '}';
    }
}
