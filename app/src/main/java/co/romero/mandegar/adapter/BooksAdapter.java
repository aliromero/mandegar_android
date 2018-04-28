package co.romero.mandegar.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import co.romero.mandegar.R;

public class BooksAdapter extends BaseAdapter {

    private final Context mContext;
    private final HashMap<String, String> items;

    // 1
    public BooksAdapter(Context context, HashMap<String, String> items) {
        this.mContext = context;
        this.items = items;
    }

    // 2
    @Override
    public int getCount() {
        return items.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final String name = "test";
        final String pic = "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg";

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_tutorial, null);
        }

        // 3
        final ImageView imageView = convertView.findViewById(R.id.iv_cover);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.tv_name);


        // 4
        nameTextView.setText("تست");

        return convertView;
    }

}