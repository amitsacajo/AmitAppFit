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
import com.example.amitappfit.model.Item;
import com.example.amitappfit.screens.EditItemActivity;
import com.example.amitappfit.util.ImageUtil;

import java.util.List;

// מתאם לרשימת פריטים (בגדים) בתצוגת Closet
public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.ViewHolder> {

    private List<Item> items; // רשימת פריטים שמוצגת ברשימה
    private final Context context; // נדרש לצורך פתיחת Activities דרך Intents

    // בנאי שמקבל את רשימת הפריטים והקונטקסט של הפעילות
    public ClosetAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // יצירת View חדש מכל שורה ברשימה לפי תבנית XML (item_closet.xml)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_closet, parent, false);
        return new ViewHolder(itemView); // מחזיר את ה-ViewHolder עם הקשרים לתצוגות הפנימיות
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // קישור הנתונים של כל פריט (Item) לאלמנטים הגרפיים בתצוגה
        Item item = items.get(position);

        // קובע את שם הפריט ב-TextView
        holder.itemName.setText(item.getTitle());

        // מציג את התמונה בעזרת פונקציית עזר שהופכת Base64 לתמונה
        holder.itemImage.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));

        // מאזין ללחיצה על כל הפריט ברשימה
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // יצירת Intent לפתיחת מסך עריכת הפריט (EditItemActivity)
                Intent intent = new Intent(context, EditItemActivity.class);

                // מעביר את מזהה הפריט כ-extra
                intent.putExtra("item_id", item.getId());

                // מעביר גם את מזהה המשתמש (למקרה שצריך לבדוק הרשאות או התאמה)
                intent.putExtra("item_user_id", item.getUserId());

                // הפעלת המסך החדש
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // מחזיר את מספר הפריטים ברשימה — נדרש ל-RecyclerView
        return items.size();
    }

    // מאפשר לעדכן את רשימת הפריטים מבחוץ (למשל אחרי שינוי או חיפוש)
    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // מודיע לרשימה להתעדכן ולהתרענן
    }

    // מחלקת ViewHolder פנימית שמחזיקה את התצוגות של כל פריט ברשימה
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;     // שם הפריט
        public ImageView itemImage;   // תמונת הפריט

        public ViewHolder(View itemView) {
            super(itemView);
            // קישור בין המשתנים לתצוגות בקובץ ה-XML (item_closet.xml)
            itemName = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
