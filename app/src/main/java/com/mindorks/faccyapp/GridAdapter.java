package com.mindorks.faccyapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindorks.faccyapp.obj.Product;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    Context context;
    ArrayList<Product> listPro = new ArrayList<>();
    View view;
    LayoutInflater layoutInflater;
    View.OnClickListener listener;

    public GridAdapter(Context context, ArrayList<Product> listPro) {
        this.context = context;
        this.listPro = listPro;
    }

    @Override
    public int getCount() {
        return listPro.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final Product objPro = listPro.get( position );
        if(convertView == null){
            view = new View( context );
            view = layoutInflater.inflate( R.layout.single_specific_item, parent, false );
            ImageView imageView = (ImageView) view.findViewById( R.id.imgView );
            final TextView textView = (TextView) view.findViewById( R.id.txtNameView );
            imageView.setImageResource( objPro.getImgPro() );
            textView.setText( objPro.getNamePro() );

            imageView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( view.getContext(), ThreeModelActivity.class );
                    intent.putExtra("itemElected", objPro.getNamePro());
                    context.startActivity( intent );
                }
            } );
        } else {
            view = (View) convertView;
        }
        return view;
    }
}
