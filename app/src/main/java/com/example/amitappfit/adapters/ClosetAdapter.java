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

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {

    private List<String> items;

    public ClosetAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // יצירת ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_closet, parent, false);  // הכנס את ה-XML שלך (כמו item_closet.xml)
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // הצגת פריט ב-ViewHolder
        String item = items.get(position);
        holder.itemName.setText(item);
        // אם יש לך תמונה או פרמטרים נוספים, תוכל להוסיף אותם כאן
        // holder.itemImage.setImageResource(R.drawable.some_image); // תמונה דוגמה
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // פונקציה לעדכון רשימת הפריטים
    public void updateItems(List<String> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // עדכון ה-RecyclerView
    }

    // הגדרת ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public ImageView itemImage; // רק אם אתה מציג גם תמונות

        public ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName); // הגדרת ה-TextView
            itemImage = itemView.findViewById(R.id.itemImage); // הגדרת ה-ImageView (אם יש תמונה)
        }
    }
}

