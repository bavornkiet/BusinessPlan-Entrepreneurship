package cm40179g3.citywalker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class PremiumShopAdapter extends RecyclerView.Adapter<PremiumShopAdapter.PremiumPackageViewHolder> {
    private List<PremiumPackage> packages;
    private Consumer<PremiumPackage> onPackageClick;

    public PremiumShopAdapter(List<PremiumPackage> packages, Consumer<PremiumPackage> onPackageClick) {
        this.packages = packages;
        this.onPackageClick = onPackageClick;
    }

    @NonNull
    @Override
    public PremiumPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_premium_package, parent, false);
        return new PremiumPackageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PremiumPackageViewHolder holder, int position) {
        PremiumPackage packageItem = packages.get(position);
        holder.tvPackageName.setText(packageItem.getName());
        holder.tvPackagePrice.setText(String.format("$%.2f", packageItem.getPrice()));

        holder.itemView.setOnClickListener(v -> onPackageClick.accept(packageItem));
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    static class PremiumPackageViewHolder extends RecyclerView.ViewHolder {
        TextView tvPackageName;
        TextView tvPackagePrice;

        public PremiumPackageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPackageName = itemView.findViewById(R.id.tv_package_name);
            tvPackagePrice = itemView.findViewById(R.id.tv_package_price);
        }
    }
}