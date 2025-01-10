package com.example.amitappfit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.screens.EditItemActivity;

import java.util.List;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {

    private List<String> items;
    private Context context; // נדרש כדי להפעיל Intents

    public ClosetAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // יצירת ViewHolder
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_closet, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // הצגת פריט ב-ViewHolder
        String item = items.get(position);
        holder.itemName.setText(item);

        // אם יש לך תמונה או פרמטרים נוספים, ניתן להוסיף אותם כאן
        // holder.itemImage.setImageResource(R.drawable.some_image);

        // הגדרת Listener ללחיצה על פריט
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // פתיחת עמוד EditItemActivity עם המידע של הפריט שנבחר
                Intent intent = new Intent(context, EditItemActivity.class);
                intent.putExtra("itemData", item); // מעביר את המידע על הפריט לעמוד העריכה
                context.startActivity(intent);
            }
        });
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
