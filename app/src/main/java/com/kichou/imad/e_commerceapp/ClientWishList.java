package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.sellers.SeeProductDetails;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.CartRecycleAdapter;
import com.kichou.imad.e_commerceapp.view.ClientWishListAdapter;

import java.util.ArrayList;

public class ClientWishList extends AppCompatActivity implements SeeProductDetails {

    private ImageView backBtn;

    private RecyclerView recyclerView;
    private ClientWishListAdapter clientWishListAdapter;

    private ArrayList<String> imgaeUrl = new ArrayList<>();
    private ArrayList<String> pNames = new ArrayList<>();
    private ArrayList<String> pPrice = new ArrayList<>();
    private ArrayList<String> productKey = new ArrayList<>();

    private DatabaseReference databaseReference;
    private SeeProductDetails seeProductDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_wish_list);

        seeProductDetails = this;
        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.wishListRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


//        databaseReference = FirebaseDatabase.getInstance().getReference().child("client_wishList").child(Prevlent.currentClient);
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//
//                for(DataSnapshot val : snapshot.getChildren())
//                {
//                    for(DataSnapshot val2 : val.getChildren())
//                    {
//                      //  Log.d("TAG", "onDataChange: " + val2.getValue());
//                        String pKey = val2.getValue().toString();
//
//                        DatabaseReference pRef = FirebaseDatabase.getInstance().getReference().child("sellers");
//
//                        pRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot)
//                            {
//                                for(DataSnapshot val3 : snapshot.getChildren())
//                                {
//                                    for(DataSnapshot val4 : val3.child("products").getChildren())
//                                    {
//                                        String key = val4.getKey();
//
//                                        if(pKey.equals(key))
//                                        {
//                                            pNames.add(val4.child("pname").getValue().toString());
//                                            pPrice.add(val4.child("price").getValue().toString());
//                                            imgaeUrl.add(val4.child("Images").child("img0").getValue().toString());
//                                            productKey.add(key);
//
//                                        }
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//
//                    }
//                }
//
//
//                clientWishListAdapter = new ClientWishListAdapter(getApplicationContext(),imgaeUrl,pNames,pPrice,productKey);
//                recyclerView.setAdapter(clientWishListAdapter);
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        View view = null;
        startAsynTask(view);

    }

    public void startAsynTask(View v)
    {
        AsyncTaskClientWishList asyncTaskClientWishList = new AsyncTaskClientWishList();
        asyncTaskClientWishList.execute();
    }

    @Override
    public void seeDetails(String pKey)
    {
        Intent intent = new Intent(getApplicationContext(),BuyAProductActivity.class);
        intent.putExtra("pKey",pKey);
        // Toast.makeText(this,pKey,Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private class AsyncTaskClientWishList extends AsyncTask<Void,ArrayList<String>,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            databaseReference = FirebaseDatabase.getInstance().getReference().child("client_wishList").child(Prevlent.currentClient);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {

                    for(DataSnapshot val : snapshot.getChildren())
                    {
                        for(DataSnapshot val2 : val.getChildren())
                        {
                            //  Log.d("TAG", "onDataChange: " + val2.getValue());
                            String pKey = val2.getValue().toString();

                            DatabaseReference pRef = FirebaseDatabase.getInstance().getReference().child("sellers");

                            pRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    for(DataSnapshot val3 : snapshot.getChildren())
                                    {
                                        for(DataSnapshot val4 : val3.child("products").getChildren())
                                        {
                                            String key = val4.getKey();

                                            if(pKey.equals(key))
                                            {
                                                pNames.add(val4.child("pname").getValue().toString());
                                                pPrice.add(val4.child("price").getValue().toString());
                                                imgaeUrl.add(val4.child("Images").child("img0").getValue().toString());
                                                productKey.add(key);

                                               publishProgress(imgaeUrl,pNames,pPrice,productKey);

                                            }

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    }





                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            return null;
        }



        @Override
        protected void onProgressUpdate(ArrayList<String>... values)
        {
            super.onProgressUpdate(values);

            for(String val : values[0])
            {
                Log.d("TAG", "onProgressUpdate: " + val);
            }
           clientWishListAdapter = new ClientWishListAdapter(getApplicationContext(),values[0],values[1],values[2],values[3],seeProductDetails);
            recyclerView.setAdapter(clientWishListAdapter);

        }
    }


}