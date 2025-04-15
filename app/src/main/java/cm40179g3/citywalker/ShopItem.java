package cm40179g3.citywalker;

public class ShopItem {
    private String name;
    private int iconRes;
    private int price;
    private boolean isPremium;

    public ShopItem(String name, int iconRes, int price, boolean isPremium) {
        this.name = name;
        this.iconRes = iconRes;
        this.price = price;
        this.isPremium = isPremium;
    }

    // Add getters here
    public String getName() { return name; }
    public int getIconRes() { return iconRes; }
    public int getPrice() { return price; }
    public boolean isPremium() { return isPremium; }
}
