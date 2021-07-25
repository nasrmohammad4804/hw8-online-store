package domain;

public class Customer {

    private String name;
    private String family;
    private String userName;
    private String password;
    private int id;

    public Customer (String name,String family,String userName,String password){
        this.family=family;
        this.name=name;
        this.userName=userName;
        this.password=password;
    }

    public Customer(String name, String family, String userName, String password, int id) {
        this.name = name;
        this.family = family;
        this.userName = userName;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                '}';
    }
}
