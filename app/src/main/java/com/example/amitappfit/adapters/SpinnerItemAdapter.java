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
    private final int resource;

    public SpinnerItemAdapter(@NonNull Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        Item item = getItem(position);
        if (item == null) {
            return convertView;
        }

        // שימוש בשם השדה הנכון
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(item.getTitle());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
        }

        Item item = getItem(position);
        if (item == null) return convertView;

        TextView textView = convertView.findViewById(R.id.tvItem);
        textView.setText(item.getTitle());

        ImageView imageView = convertView.findViewById(R.id.item_image_view);
        imageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));

        return convertView;
    }
}
