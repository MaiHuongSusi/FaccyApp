package com.mindorks.faccyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;

import com.mindorks.faccyapp.obj.Category;
import com.mindorks.faccyapp.obj.Product;

import java.util.ArrayList;

public class ThreeModelElectionActivity extends AppCompatActivity {
    private ArrayList<Category> listCat = new ArrayList<>();
    private ArrayList<Product> listPro = new ArrayList<>();
    private GridView gridView;

    //initiate list of products
    Product objAnimal1 = new Product( "Dinosaur", R.drawable.dinosaur, "Animal");
    Product objAnimal2 = new Product( "Cow", R.drawable.cow, "Animal");
    Product objAnimal3 = new Product( "Deer", R.drawable.deer, "Animal");
    Product objAnimal4 = new Product( "Cat", R.drawable.cat, "Animal");
    Product objAnimal5 = new Product( "Bird", R.drawable.bird, "Animal");
    Product objAnimal6 = new Product( "Dog", R.drawable.dog, "Animal");
    Product objAnimal7 = new Product( "Horse", R.drawable.horse, "Animal");
    Product objAnimal8 = new Product( "Rabbit", R.drawable.rabbit, "Animal");
    Product objAnimal9 = new Product( "Shark", R.drawable.shark, "Animal");

    Product objAnimal10 = new Product( "HousePlant", R.drawable.plant, "Plant");
    Product objAnimal11 = new Product( "Rose", R.drawable.rose, "Plant");

    Product objAnimal12 = new Product( "Bench", R.drawable.bench, "Furniture");
    Product objAnimal15 = new Product( "Shoes", R.drawable.sneaker, "Furniture");
    Product objAnimal16 = new Product( "Andy", R.drawable.andy, "Furniture");
    Product objAnimal20 = new Product( "Chair", R.drawable.chair3, "Furniture");

    Product objAnimal17 = new Product( "Orange", R.drawable.orange, "Fruit");
    Product objAnimal18 = new Product( "Apple", R.drawable.apple, "Fruit");
    Product objAnimal19 = new Product( "Pear", R.drawable.pear, "Fruit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_three_model_election );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        gridView = (GridView) findViewById( R.id.gridview );

        initCategory();
    }

    private void attachGridViewElection(){
        GridAdapter gridAdapter = new GridAdapter( ThreeModelElectionActivity.this, listPro );
//        Toast.makeText( this, "Size : " + listPro.size(), Toast.LENGTH_LONG ).show();
        gridView.setAdapter( gridAdapter );
    }

    private void initCategory(){
        Category catAnimal = new Category("Animal", R.drawable.cat_animal);
        Category catPlant = new Category( "Plant", R.drawable.cat_plant);
        Category catFur = new Category( "Furniture", R.drawable.cat_ocean);
        Category catFruit = new Category( "Fruit",  R.drawable.cat_ocean);

        listCat.add(catAnimal);
        listCat.add(catPlant);
        listCat.add( catFur );
        listCat.add( catFruit );

        initRecylerView();
    }

    private void initRecylerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL, false );
        RecyclerView recyclerViewId = findViewById( R.id.recyclerViewId );
        recyclerViewId.setLayoutManager( linearLayoutManager );

//        StaggeredGridLayoutManager gridLayoutManager =
//                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        recyclerViewId.setLayoutManager(gridLayoutManager);

        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter( listCat, this  );
        recyclerViewId.setAdapter( adapter );

        adapter.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category objCat = (Category) v.getTag();
//                Log.v( "Ahihi", objCat.getNameCat() );
                initProduct(objCat.getNameCat());
                attachGridViewElection();
            }
        } );
    }

    private void initProduct(String nameCat){
        switch (nameCat) {
            case "Animal" :
                listPro.clear();
                listPro.add( objAnimal1 );
                listPro.add( objAnimal2 );
                listPro.add( objAnimal3 );
                listPro.add( objAnimal4 );
                listPro.add( objAnimal5 );
                listPro.add( objAnimal6 );
                listPro.add( objAnimal7 );
                listPro.add( objAnimal8 );
                listPro.add( objAnimal9 );
                break;
            case "Plant" :
                listPro.clear();
                listPro.add( objAnimal10 );
                listPro.add( objAnimal11 );
                break;
            case "Furniture":
                listPro.clear();
                listPro.add( objAnimal12 );
                listPro.add( objAnimal15 );
                listPro.add( objAnimal16 );
                listPro.add( objAnimal20 );
                break;
            case "Fruit":
                listPro.clear();
                listPro.add( objAnimal17 );
                listPro.add( objAnimal18 );
                listPro.add( objAnimal19 );
                break;
            default:
                break;
        }
    }
}
