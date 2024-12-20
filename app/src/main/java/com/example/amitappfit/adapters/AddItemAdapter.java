package com.example.amitappfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amitappfit.R;
import java.util.List;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ItemViewHolder> {

    private List<String> items;

    public AddItemAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tvItem.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // פונקציה לעדכון הפריטים
    public void updateItems(List<String> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // עדכון ה-RecyclerView
    }

    // מחלקה פנימית לתפוס את הפריטים ב-RecyclerView
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem); // ודא ש-id תואם ל-XML שלך
        }
    }
}
