package cm40179g3.citywalker;

public class PremiumPackage {
    private String name;
    private int amount;
    private double price;

    public PremiumPackage(String name, int amount, double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    // Add getters here
    public String getName() { return name; }
    public int getAmount() { return amount; }
    public double getPrice() { return price; }
}
