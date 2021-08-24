package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.CartRecycleAdapter;
import com.kichou.imad.e_commerceapp.view.ClientWishListAdapter;
import com.kichou.imad.e_commerceapp.view.OrderRecycleViewAdapter;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;

public class SellerOrdersActivity extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView orderRecycleView;
    private OrderRecycleViewAdapter orderRecycleViewAdapter;


    ArrayList<String> client = new ArrayList<>();
    ArrayList<String> addr = new ArrayList<>();
    ArrayList<String> productNames = new ArrayList<>();
    ArrayList<String> productQtes = new ArrayList<>();
    ArrayList<String> productPrices = new ArrayList<>();
    ArrayList<String> orderKey = new ArrayList<>();
    ArrayList<String> productKeys = new ArrayList<>();
    ArrayList<String> clientsKey = new ArrayList<>();
    ArrayList<String> productEtat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);

        orderRecycleViewAdapter = new OrderRecycleViewAdapter();
        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        orderRecycleView = findViewById(R.id.sellerProductListsRecycleView);
        orderRecycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));



        View view = null;
        startAsynTask(view);

       // checkEtat();

    }


    public void startAsynTask(View v)
    {
        AsyncTaskOrdersList1 asyncTaskOrdersList = new AsyncTaskOrdersList1();
        asyncTaskOrdersList.execute();

        AsyncTaskOrdersList2 asyncTaskOrdersList2 = new AsyncTaskOrdersList2();
        asyncTaskOrdersList2.execute();

        AsyncTaskOrdersList3 asyncTaskOrdersList3 = new AsyncTaskOrdersList3();
        asyncTaskOrdersList3.execute();

    }

    private class AsyncTaskOrdersList1 extends AsyncTask<Void,ArrayList<String>,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("orders");


            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {

                    for(DataSnapshot val : snapshot.getChildren())
                    {
                        for(DataSnapshot val2 : val.getChildren())
                        {
                            for(DataSnapshot val3 : val2.child("products").getChildren())
                            {
                                String sellerKey = val3.child("sellersP").child("seller").getValue().toString();
                                if(sellerKey.equals(Prevlent.currentSeller))
                                {
                                    DatabaseReference clientRef= FirebaseDatabase.getInstance().getReference().child("clients").child(val.getKey());

                                    clientRef.addListenerForSingleValueEvent(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            String firstName = snapshot.child("client_first_name").getValue().toString();
                                            String lastName = snapshot.child("client_last_name").getValue().toString();

                                           // Log.d("TAG", "onDataChange: " + firstName +" " + lastName);
                                            client.add("Client : " + firstName + " " + lastName + " /Tel : " + snapshot.getKey());
                                            clientsKey.add(snapshot.getKey());
                                            publishProgress(client,clientsKey);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }
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
            orderRecycleViewAdapter.setClient(values[0]);
            orderRecycleViewAdapter.setClientsKey(values[1]);

        }


    }

    private class AsyncTaskOrdersList2 extends AsyncTask<Void,ArrayList<String>,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("orders");


            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot val : snapshot.getChildren())
                    {

                        for(DataSnapshot val2 : val.getChildren())
                        {
                            // Log.d("TAG", "onDataChange: order " + val2.getKey());



                            for(DataSnapshot val3 : val2.child("products").getChildren())
                            {

                                if(Prevlent.currentSeller.equals(val3.child("sellersP").child("seller").getValue().toString()))
                                {
                                   // Log.d("TAG", "onDataChange: " + val3.getKey());
                                    orderKey.add(val2.getKey());
                                    String addressOfOrder = val2.child("addr1").getValue().toString();
                                    String wilayaOfOrder = val2.child("wilaya").getValue().toString();
                                    addr.add("Address : "+addressOfOrder + " Wilaya : " + wilayaOfOrder);
                                    String pPrice = "Prix : " + val3.child("product_price").getValue().toString() + " DA";
                                    String qte = "Qte : " +val3.child("product_qte").getValue().toString();
                                    productPrices.add(pPrice);
                                    productQtes.add(qte);
                                    productKeys.add(val3.getKey());

                                    //Log.d("TAG", "onDataChange: " + val3.child("etats").getValue().toString());
                                   productEtat.add(val3.child("etats").getValue().toString());

                                    publishProgress(addr,productPrices,productQtes,orderKey,productKeys,productEtat);
                                }


                            }

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

            orderRecycleViewAdapter.setAddr(values[0]);
            orderRecycleViewAdapter.setProductPrices(values[1]);
            orderRecycleViewAdapter.setProductQtes(values[2]);
            orderRecycleViewAdapter.setOrderKey(values[3]);
            orderRecycleViewAdapter.setProductKeys(values[4]);

            productEtat = values[5];

        }


    }

    private class AsyncTaskOrdersList3 extends AsyncTask<Void,ArrayList<String>,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("orders");


            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot val : snapshot.getChildren())
                    {

                        for(DataSnapshot val2 : val.getChildren())
                        {

                            for(DataSnapshot val3 : val2.child("products").getChildren())
                            {
                                // Log.d("TAG", "onDataChange: " + val3.getKey());
                                if(Prevlent.currentSeller.equals(val3.child("sellersP").child("seller").getValue().toString()))
                                {
                                    DatabaseReference prodRef = FirebaseDatabase.getInstance().getReference().child("sellers");
                                    prodRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            for(DataSnapshot val5 : snapshot.getChildren())
                                            {
                                                for(DataSnapshot val6 : val5.child("products").getChildren())
                                                {
                                                    //Log.d("TAG", "onDataChange: " + val6.getKey());

                                                    if(val6.getKey().equals(val3.getKey()))
                                                    {
                                                        productNames.add("Nom Article : " + val6.child("pname").getValue().toString());
                                                        publishProgress(productNames);
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

            orderRecycleViewAdapter.setProductNames(values[0]);


             orderRecycleViewAdapter = new OrderRecycleViewAdapter(getApplicationContext(),client,addr,productNames,productPrices,productQtes,orderKey,clientsKey,productKeys,productEtat);
             orderRecycleView.setAdapter(orderRecycleViewAdapter);




        }


    }





}