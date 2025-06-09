package com.example.amitappfit.adapters;

// ייבוא ספריות חיוניות
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.model.Look;

import java.util.List;

// מתאם (Adapter) עבור RecyclerView להצגת רשימת לוקים
public class LookAdapter extends RecyclerView.Adapter<LookAdapter.LookViewHolder> {

    // רשימת הלוקים להצגה
    private final List<Look> lookList;

    // ממשק שמאפשר להגיב ללחיצה על פריט
    private final OnItemClickListener listener;

    // בנאי של המתאם – מקבל רשימת לוקים ומאזין ללחיצות
    public LookAdapter(List<Look> lookList, OnItemClickListener listener) {
        this.lookList = lookList;
        this.listener = listener;
    }

    // יוצרת תצוגת פריט חדש (ViewHolder) כאשר אין אחד מוכן לשימוש חוזר
    @NonNull
    @Override
    public LookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // טוען את תבנית העיצוב של פריט לוק (item_look.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look, parent, false);
        return new LookViewHolder(view);
    }

    // קושרת בין הנתונים לתצוגה של הפריט שנבחר
    @Override
    public void onBindViewHolder(@NonNull LookViewHolder holder, int position) {
        // מקבל את הלוק הספציפי מתוך הרשימה
        Look look = lookList.get(position);

        // מציג את שם הלוק
        holder.tvLookName.setText(look.getName());

        // בודק אם יש פריט עליון (top) ומציג אותו, אם לא – מסתיר
        if (look.getTop() != null) {
            holder.tvTop.setVisibility(View.VISIBLE);
            holder.tvTop.setText("Top: " + look.getTop().getTitle());
        } else {
            holder.tvTop.setVisibility(View.GONE);
        }

        // בודק אם יש פריט תחתון (bottom) ומציג אותו, אם לא – מסתיר
        if (look.getBottom() != null) {
            holder.tvBottom.setVisibility(View.VISIBLE);
            holder.tvBottom.setText("Bottom: " + look.getBottom().getTitle());
        } else {
            holder.tvBottom.setVisibility(View.GONE);
        }

        // בודק אם יש נעליים (shoes) ומציג אותן, אם לא – מסתיר
        if (look.getShoes() != null) {
            holder.tvShoes.setVisibility(View.VISIBLE);
            holder.tvShoes.setText("Shoes: " + look.getShoes().getTitle());
        } else {
            holder.tvShoes.setVisibility(View.GONE);
        }

        // מקשיב ללחיצה על הפריט ושולח אותו למאזין שהוגדר
        holder.itemView.setOnClickListener(v -> listener.onItemClick(look));
    }

    // מחזיר את מספר הפריטים ברשימה (כדי ש-RecyclerView ידע כמה פריטים להציג)
    @Override
    public int getItemCount() {
        return lookList.size();
    }

    // מחלקת ViewHolder שמחזיקה את הרכיבים הגרפיים של כל פריט ברשימה
    static class LookViewHolder extends RecyclerView.ViewHolder {
        TextView tvLookName, tvTop, tvBottom, tvShoes;

        public LookViewHolder(@NonNull View itemView) {
            super(itemView);
            // קישור בין משתנים לבין רכיבי העיצוב בקובץ item_look.xml
            tvLookName = itemView.findViewById(R.id.tvLookName);
            tvTop = itemView.findViewById(R.id.tvTop);
            tvBottom = itemView.findViewById(R.id.tvBottom);
            tvShoes = itemView.findViewById(R.id.tvShoes);
        }
    }

    // ממשק שמאפשר לתצוגה החיצונית להגיב ללחיצה על פריט ברשימה
    public interface OnItemClickListener {
        void onItemClick(Look look); // פעולה שתתבצע כשלוחצים על לוק מסוים
    }
}
