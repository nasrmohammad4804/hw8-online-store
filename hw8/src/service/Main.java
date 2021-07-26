package service;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.start();
        try {

            ApplicationContext.getConnection().close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
