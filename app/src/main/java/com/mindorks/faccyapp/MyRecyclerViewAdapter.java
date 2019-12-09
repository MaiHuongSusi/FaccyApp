package com.mindorks.faccyapp;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.faccyapp.obj.Category;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyRecyclerViewAdapter";
    private ArrayList<Category> listCat = new ArrayList<>();
    private Context context;
    private View.OnClickListener listener;

    public MyRecyclerViewAdapter(ArrayList<Category> listCat, Context context) {
        this.listCat = listCat;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.single_item, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Category proCat = listCat.get( position );
        holder.imgCategory.setImageResource( proCat.getImgCat() );
        holder.imgCategory.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category proCat = listCat.get( position );
//                Log.v( "MyLog","Ahihi" );
                v.setTag( proCat );
                listener.onClick( v );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return listCat.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.listener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;

        public ViewHolder(View itemView) {
            super( itemView );
            imgCategory = itemView.findViewById( R.id.imgCategory );
        }
    }
}
