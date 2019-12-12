package com.mindorks.faccyapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomListView extends ArrayAdapter<String> {
    private String[] mName;
    private Integer[] mImageId;
    private Activity mContext;


    public CustomListView(Activity mContext, String[] mName, Integer[] mImageId) {
        super(mContext, R.layout.list_topic, mName);
        this.mContext = mContext;
        this.mName = mName;
        this.mImageId = mImageId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = mContext.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.item_list_topic, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.imageView.setImageResource(mImageId[position]);

        return r;
    }

    class ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.imageTopic);
        }
    }
}
