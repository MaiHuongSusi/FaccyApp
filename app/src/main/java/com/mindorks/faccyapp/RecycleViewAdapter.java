package com.mindorks.faccyapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private static final String TAG = "RecycleViewAdapter";
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;
    private int selected;

    public interface IOnItemClick {
        void onClick(int pos);
    }

    private IOnItemClick iOnItemClick;

    public void setOnItemClick(IOnItemClick iOnItemClick) {
        this.iOnItemClick = iOnItemClick;
    }

    public RecycleViewAdapter(Context mContext, ArrayList<Integer> mImages, ArrayList<String> mNames) {
        this.mNames = mNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnItemClick(new IOnItemClick() {
            @Override
            public void onClick(int pos) {
                RecycleViewAdapter.this.iOnItemClick.onClick(pos);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).asBitmap().load(mImages.get(position)).into(holder.image);
        holder.name.setText(mNames.get(position).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;

        IOnItemClick iOnItemClick;

        public void setOnItemClick(IOnItemClick iOnItemClick) {
            this.iOnItemClick = iOnItemClick;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }

        @Override
        public void onClick(View view) {
            if (this.iOnItemClick != null) {
                this.iOnItemClick.onClick(getAdapterPosition());
            }
        }
    }
}