package com.example.amitappfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.amitappfit.R;
import com.example.amitappfit.model.Item;
import com.example.amitappfit.util.ImageUtil;

import java.util.List;

public class SpinnerItemAdapter extends ArrayAdapter<Item> {

    private final LayoutInflater inflater;
    private static final int resource = R.layout.spinner_item;

    public SpinnerItemAdapter(@NonNull Context context, List<Item> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        Item item = getItem(position);
        if (item == null) return convertView;

        // Set item title
        TextView textView = convertView.findViewById(R.id.textView_item_spinner);
        textView.setText(item.getTitle());

        // Set item image
        ImageView imageView = convertView.findViewById(R.id.imageView_item_spinner);
        imageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400,400));

        return convertView;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
        }

        Item item = getItem(position);
        if (item == null) return convertView;

        // הצגת שם הפריט
        TextView textView = convertView.findViewById(R.id.tvItem);
        textView.setText(item.getTitle());

        // הצגת התמונה באמצעות Bitmap מ-Base64
        ImageView imageView = convertView.findViewById(R.id.item_image_view);
        imageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));

        return convertView;
    }

}
