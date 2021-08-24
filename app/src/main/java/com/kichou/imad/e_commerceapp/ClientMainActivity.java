package com.kichou.imad.e_commerceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kichou.imad.e_commerceapp.view.AdapterBestProducts;
import com.kichou.imad.e_commerceapp.view.AdapterCategoryPhotos;
import com.kichou.imad.e_commerceapp.view.AdapterNewProducts;

import java.util.ArrayList;

import io.paperdb.Paper;

public class ClientMainActivity extends AppCompatActivity {



    private BottomNavigationView bottomNavigationView;
    private NavController navController;


//
//    // categories
//
//    private Button newproductBtn;
//
//    private RecyclerView gategoryRecycleView;
//    private AdapterCategoryPhotos adapterCategoryPhotos;
//
//    private int[] catImages =
//            {
//                R.drawable.men_shoes,
//                R.drawable.femme,
//                R.drawable.devices,
//                R.drawable.gadgets,
//                R.drawable.game
//            };
//
//    private String[] imgTitles;
//
//    // new products
//
//    private RecyclerView newAddedProductView;
//    private AdapterNewProducts adapterNewProducts;
//
//    private int[] newProductImages =
//            {
//                    R.drawable.choussure,
//                    R.drawable.choussure,
//                    R.drawable.choussure,
//                    R.drawable.choussure,
//                    R.drawable.choussure,
//                    R.drawable.choussure,
//                    R.drawable.choussure,
//            };
//
//    private String[] newProductName = {
//            "produit 1","produit 1","produit 1", "produit 1","produit 1","produit 1","produit 1"
//    };
//
//    private String[] newProductPrice = {
//            "1300","1300","1300","1300","1300","1300","1300"
//    };
//
//    // best product Recyle view
//
//    private RecyclerView bestProductView;
//    private AdapterBestProducts adapterBestProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        bottomNavigationView = findViewById(R.id.ClientbottomNavigationView);
        navController = Navigation.findNavController(this, R.id.fragment);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

//
//
//
//
//        // catgeroy recyce view
//        gategoryRecycleView = findViewById(R.id.categoryRecycleView);
////        gategoryRecycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        imgTitles = getResources().getStringArray(R.array.category_vende);
//  //      adapterCategoryPhotos = new AdapterCategoryPhotos(this,catImages,imgTitles);
////        gategoryRecycleView.setAdapter(adapterCategoryPhotos);
//
//        // new products recycle view
//
//        newAddedProductView = findViewById(R.id.newPoductsRec);
//       // newAddedProductView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//        //adapterNewProducts = new AdapterNewProducts(this,newProductImages,newProductName,newProductPrice);
//        //newAddedProductView.setAdapter(adapterNewProducts);
//
//        // best products view
//
//      //  bestProductView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//       // adapterBestProducts = new AdapterBestProducts(this,newProductImages,newProductName,newProductPrice);
//       // bestProductView.setAdapter(adapterBestProducts);
//
//
////        newproductBtn = findViewById(R.id.newproductBtn);
////        newproductBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Paper.init(getBaseContext());
////                Paper.book().destroy();
////                Intent intent = new Intent(ClientMainActivity.this,WelcomeActivity.class);
////                startActivity(intent);
////
////            }
////        });

    }
}