package cm40179g3.citywalker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopItemViewHolder> {
    private List<ShopItem> items;
    private Consumer<ShopItem> onItemClick;

    public ShopAdapter(List<ShopItem> items, Consumer<ShopItem> onItemClick) {
        this.items = items;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ShopItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop_product, parent, false);
        return new ShopItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopItemViewHolder holder, int position) {
        ShopItem item = items.get(position);
        holder.ivItemIcon.setImageResource(item.getIconRes());
        holder.tvItemName.setText(item.getName());
        holder.tvItemPrice.setText(String.valueOf(item.getPrice()));
        holder.ivCurrencyIcon.setImageResource(
                item.isPremium() ? R.drawable.ic_premium_currency : R.drawable.ic_steps_currency);

        holder.itemView.setOnClickListener(v -> onItemClick.accept(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ShopItemViewHolder extends RecyclerView.ViewHolder {
        ImageView ivItemIcon;
        TextView tvItemName;
        TextView tvItemPrice;
        ImageView ivCurrencyIcon;

        public ShopItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivItemIcon = itemView.findViewById(R.id.iv_item_icon);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            ivCurrencyIcon = itemView.findViewById(R.id.iv_currency_icon);
        }
    }
}
