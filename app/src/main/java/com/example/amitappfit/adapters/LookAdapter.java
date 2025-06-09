package com.example.amitappfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amitappfit.R;
import com.example.amitappfit.model.Look;

import java.util.List;

public class LookAdapter extends RecyclerView.Adapter<LookAdapter.LookViewHolder> {

    private final List<Look> lookList;
    private final OnItemClickListener listener;

    // קונסטרוקטור עם OnItemClickListener
    public LookAdapter(List<Look> lookList, OnItemClickListener listener) {
        this.lookList = lookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // יצירת תצוגת פריט
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look, parent, false);
        return new LookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LookViewHolder holder, int position) {
        // חיבור נתוני הלוק לפריט בתצוגה
        Look look = lookList.get(position);
        holder.tvLookName.setText(look.getName());
        if (look.getTop() != null) {
            holder.tvTop.setVisibility(View.VISIBLE);
            holder.tvTop.setText("Top: " + look.getTop().getTitle());
        } else {
            holder.tvTop.setVisibility(View.GONE);
        }
        if (look.getBottom() != null) {
            holder.tvBottom.setVisibility(View.VISIBLE);
            holder.tvBottom.setText("Bottom: " + look.getBottom().getTitle());
        } else {
            holder.tvBottom.setVisibility(View.GONE);
        }
        if (look.getShoes() != null) {
            holder.tvShoes.setVisibility(View.VISIBLE);
            holder.tvShoes.setText("Shoes: " + look.getShoes().getTitle());
        } else {
            holder.tvShoes.setVisibility(View.GONE);
        }
        // הגדרת לחיצה על פריט
        holder.itemView.setOnClickListener(v -> listener.onItemClick(look));
    }

    @Override
    public int getItemCount() {
        return lookList.size();
    }

    // מחלקת ViewHolder לטיפול ברכיבי פריט
    static class LookViewHolder extends RecyclerView.ViewHolder {
        TextView tvLookName, tvTop, tvBottom, tvShoes;

        public LookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLookName = itemView.findViewById(R.id.tvLookName);
            tvTop = itemView.findViewById(R.id.tvTop);
            tvBottom = itemView.findViewById(R.id.tvBottom);
            tvShoes = itemView.findViewById(R.id.tvShoes);
        }
    }

    // ממשק ללחיצה על פריט
    public interface OnItemClickListener {
        void onItemClick(Look look);
    }
}
