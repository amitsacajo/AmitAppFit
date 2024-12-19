package com.example.amitappfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amitappfit.R;
import com.example.amitappfit.model.ClosetItem;
import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ClosetViewHolder> {

    private List<ClosetItem> closetItems;

    // קונסטרוקטור
    public ClosetAdapter(List<ClosetItem> closetItems) {
        this.closetItems = closetItems;
    }

    // יצירת ViewHolder חדש
    @NonNull
    @Override
    public ClosetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_closet, parent, false); // הקובץ item_closet.xml
        return new ClosetViewHolder(itemView);
    }

    // ממלאים את ה-ViewHolder עם נתונים
    @Override
    public void onBindViewHolder(@NonNull ClosetViewHolder holder, int position) {
        ClosetItem item = closetItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemImage.setImageResource(item.getImageResId());
    }

    @Override
    public int getItemCount() {
        return closetItems.size();
    }

    public static class ClosetViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public ImageView itemImage;

        public ClosetViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
