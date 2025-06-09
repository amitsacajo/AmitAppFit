package com.example.amitappfit.adapters;

// ייבוא מחלקות דרושות
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

// מתאם לרשימת פריטים, עבור RecyclerView
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    // רשימת הפריטים להצגה
    private List<Item> items;

    // בנאי שמקבל את רשימת הפריטים
    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    // פעולה שיוצרת תצוגה חדשה (ViewHolder) כאשר אין עוד אחת לשימוש חוזר
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // טוען את קובץ הפריסה של הפריט (item_layout.xml) לתוך View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // מחזיר מופע חדש של ViewHolder עם התצוגה
        return new ItemViewHolder(view);
    }

    // פעולה שממלאת את התצוגה (ViewHolder) עם נתוני פריט מהרשימה
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        // מקבל את הפריט במיקום הרצוי
        Item item = items.get(position);
        // מגדיר את הטקסט בשם הפריט
        holder.tvItem.setText(item.getTitle());
        // ממיר את התמונה מפורמט Base64 ל-Bitmap ומציג אותה
        holder.itemImageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));
    }

    // מחזיר את מספר הפריטים ברשימה
    @Override
    public int getItemCount() {
        return items.size();
    }

    // פונקציה חיצונית לעדכון רשימת הפריטים (למשל, אחרי סינון או שינוי)
    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // מעדכן את הרשימה ב-RecyclerView
    }

    // מחלקת ViewHolder פנימית שמחזיקה את הרפרנסים לתצוגות של כל פריט
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem; // טקסט שמציג את שם הפריט
        ImageView itemImageView; // תצוגת תמונה של הפריט

        public ItemViewHolder(View itemView) {
            super(itemView);
            // קישור בין משתנים לבין רכיבי העיצוב (XML)
            tvItem = itemView.findViewById(R.id.tvItem); // שדה טקסט לפריט
            itemImageView = itemView.findViewById(R.id.item_image_view); // תמונת הפריט
        }
    }
}
