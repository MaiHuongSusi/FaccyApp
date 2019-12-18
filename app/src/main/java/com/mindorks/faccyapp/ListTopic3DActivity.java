package com.mindorks.faccyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ListTopic3DActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_topic);

        final ListView list = findViewById(R.id.listView);
        String[] arrayName = {"Ocean", "Animal", "Plant", "Vehicle"};
        Integer[] arrayImg = {R.drawable.ocean, R.drawable.animal, R.drawable.plant, R.drawable.vehicle};

        CustomListView customListView = new CustomListView(this, arrayName, arrayImg);

        list.setAdapter(customListView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = arrayName[position];
                Intent intent = new Intent(ListTopic3DActivity.this, ThreeDActivity.class);
                intent.putExtra("topic", clickedItem);
                startActivity(intent);
            }
        });
    }
}
