package cm40179g3.citywalker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShopFragment extends Fragment {
    private int stepsCurrency = 0;
    private int premiumCurrency = 0;
    private TextView tvStepsCurrency;
    private TextView tvPremiumCurrency;
    private RecyclerView rvShopItems;
    private ShopAdapter shopAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // Initialize views
        tvStepsCurrency = view.findViewById(R.id.tv_steps_currency);
        tvPremiumCurrency = view.findViewById(R.id.tv_premium_currency);
        rvShopItems = view.findViewById(R.id.rv_shop_items);

        // Setup RecyclerView
        shopAdapter = new ShopAdapter(getShopItems(), this::showPurchaseDialog);
        rvShopItems.setAdapter(shopAdapter);

        // Load currencies
        loadCurrencies();

        return view;
    }

    private List<ShopItem> getShopItems() {
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem("BoatHouse", R.drawable.ic_boathouse, 50, false));
        items.add(new ShopItem("Church", R.drawable.ic_church, 100, false));
        items.add(new ShopItem("Post Office", R.drawable.ic_post_office, 200, false));
        items.add(new ShopItem("Bank", R.drawable.ic_bank, 50, true));
        return items;
    }

    private void showPurchaseDialog(ShopItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Purchase " + item.getName());

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_purchase, null);
        TextView tvDescription = dialogView.findViewById(R.id.tv_description);
        TextView tvPrice = dialogView.findViewById(R.id.tv_price);
        ImageView ivCurrency = dialogView.findViewById(R.id.iv_currency);

        tvDescription.setText("Do you want to buy this item?");
        tvPrice.setText(String.valueOf(item.getPrice()));
        ivCurrency.setImageResource(item.isPremium() ? R.drawable.ic_premium_currency : R.drawable.ic_steps_currency);

        builder.setView(dialogView)
                .setPositiveButton("Buy", (dialog, which) -> {
                    if (item.isPremium() && premiumCurrency >= item.getPrice()) {
                        premiumCurrency -= item.getPrice();
                        completePurchase(item);
                    } else if (!item.isPremium() && stepsCurrency >= item.getPrice()) {
                        stepsCurrency -= item.getPrice();
                        completePurchase(item);
                    } else {
                        Toast.makeText(getContext(), "Not enough currency", Toast.LENGTH_SHORT).show();
                    }
                    updateCurrencyDisplay();
                    saveCurrencies();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void completePurchase(ShopItem item) {
        Toast.makeText(getContext(), "Purchased: " + item.getName(), Toast.LENGTH_SHORT).show();
        // Here you would add the item to user's inventory
    }

    public void showPremiumShop(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Get Premium Currency");

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_premium_shop, null);
        RecyclerView rvPremiumPackages = dialogView.findViewById(R.id.rv_premium_packages);

        List<PremiumPackage> packages = new ArrayList<>();
        packages.add(new PremiumPackage("100 Coins", 100, 0.99));
        packages.add(new PremiumPackage("300 Coins", 300, 2.99));
        packages.add(new PremiumPackage("1000 Coins", 1000, 8.99));

        PremiumShopAdapter adapter = new PremiumShopAdapter(packages, this::purchasePremiumCurrency);
        rvPremiumPackages.setAdapter(adapter);
        rvPremiumPackages.setLayoutManager(new LinearLayoutManager(getContext()));

        builder.setView(dialogView)
                .setNegativeButton("Close", null)
                .show();
    }

    private void purchasePremiumCurrency(PremiumPackage premiumPackage) {
        premiumCurrency += premiumPackage.getAmount();
        updateCurrencyDisplay();
        saveCurrencies();
        Toast.makeText(getContext(), "Purchased " + premiumPackage.getAmount() + " coins", Toast.LENGTH_SHORT).show();
    }


    private void updateCurrencyDisplay() {
        tvStepsCurrency.setText(String.valueOf(stepsCurrency));
        tvPremiumCurrency.setText(String.valueOf(premiumCurrency));
    }

    private void loadCurrencies() {
        SharedPreferences prefs = requireContext().getSharedPreferences("GameData", Context.MODE_PRIVATE);
        stepsCurrency = prefs.getInt("steps_currency", 0);
        premiumCurrency = prefs.getInt("premium_currency", 0);
        updateCurrencyDisplay();
    }

    private void saveCurrencies() {
        SharedPreferences prefs = requireContext().getSharedPreferences("GameData", Context.MODE_PRIVATE);
        prefs.edit()
                .putInt("steps_currency", stepsCurrency)
                .putInt("premium_currency", premiumCurrency)
                .apply();
    }

    // Call this from HomeFragment when steps increase
    public void incrementStepsCurrency() {
        stepsCurrency++;
        updateCurrencyDisplay();
        saveCurrencies();
    }
}