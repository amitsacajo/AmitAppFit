package com.example.amitappfit.adapters;

// ייבוא מחלקות נדרשות
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

// מתאם (Adapter) מותאם אישית עבור Spinner שמציג פריטים עם טקסט ותמונה
public class SpinnerItemAdapter extends ArrayAdapter<Item> {

    // LayoutInflater ליצירת תצוגות מתוך קבצי XML
    private final LayoutInflater inflater;

    // מזהה משאב של תצוגת הפריט הראשי של הספינר
    private static final int resource = R.layout.spinner_item;

    // בנאי – מקבל הקשר (Context) ורשימת פריטים (Item)
    public SpinnerItemAdapter(@NonNull Context context, List<Item> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
    }

    // מחזיר את התצוגה שתופיע בספינר כשהוא לא פתוח
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // אם אין תצוגה ממוחזרת, ננפח תצוגה חדשה מתבנית spinner_item.xml
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        // מקבל את הפריט המתאים למיקום הנוכחי
        Item item = getItem(position);
        if (item == null) return convertView;

        // הצגת שם הפריט בטקסט
        TextView textView = convertView.findViewById(R.id.textView_item_spinner);
        textView.setText(item.getTitle());

        // הצגת תמונת הפריט בעזרת המרת Base64 ל-Bitmap
        ImageView imageView = convertView.findViewById(R.id.imageView_item_spinner);
        imageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));

        // קביעת גודל התמונה (400x400 פיקסלים)
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 400));

        return convertView;
    }

    // מחזיר את התצוגה של כל פריט בתוך הרשימה הנפתחת של הספינר
    @NonNull
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        // אם אין תצוגה ממוחזרת, ננפח תצוגה חדשה מתבנית item_layout.xml
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
        }

        // מקבל את הפריט המתאים למיקום הנוכחי
        Item item = getItem(position);
        if (item == null) return convertView;

        // הצגת שם הפריט
        TextView textView = convertView.findViewById(R.id.tvItem);
        textView.setText(item.getTitle());

        // הצגת תמונת הפריט
        ImageView imageView = convertView.findViewById(R.id.item_image_view);
        imageView.setImageBitmap(ImageUtil.convertFrom64base(item.getPicBase64()));

        return convertView;
    }

}
