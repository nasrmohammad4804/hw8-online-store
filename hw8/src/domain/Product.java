package domain;

public class Product {
    private int id;
    private String name;
    private int numberOfProduct;
    private int price;
    private String category_name;

    public Product(String name, int numberOfProduct, int price, String category_name) {
        this.name = name;
        this.numberOfProduct = numberOfProduct;
        this.price = price;
        this.category_name = category_name;
    }

    public Product(int id, String name, int numberOfProduct, int price, String category_name) {
        this.id = id;
        this.name = name;
        this.numberOfProduct = numberOfProduct;
        this.price = price;
        this.category_name = category_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfProduct() {
        return numberOfProduct;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory_name() {
        return category_name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfProduct=" + numberOfProduct +
                ", price=" + price +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
