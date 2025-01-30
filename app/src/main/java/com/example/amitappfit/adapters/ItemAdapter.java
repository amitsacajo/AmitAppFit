package com.example.amitappfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amitappfit.R;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.util.ImageUtil;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.tvItem.setText(item.getTitle());
        holder.itemImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // פונקציה לעדכון הפריטים
    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // עדכון ה-RecyclerView
    }

    // מחלקה פנימית לתפוס את הפריטים ב-RecyclerView
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        ImageView itemImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem); // ודא ש-id תואם ל-XML שלך
            itemImageView = itemView.findViewById(R.id.item_image_view);
        }
    }
}
