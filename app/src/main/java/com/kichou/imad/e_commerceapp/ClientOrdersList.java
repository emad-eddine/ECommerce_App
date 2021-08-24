package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.ClientOrdersAdapter;
import com.kichou.imad.e_commerceapp.view.OrderRecycleViewAdapter;

import java.util.ArrayList;

public class ClientOrdersList extends AppCompatActivity {

    private ImageView backBtn;

    private RecyclerView recyclerView;
    private ClientOrdersAdapter clientOrdersAdapter = new ClientOrdersAdapter();

    ArrayList<String> productsName = new ArrayList<>();
    ArrayList<String> productsPrice = new ArrayList<>();
    ArrayList<String> productsQte = new ArrayList<>();
    ArrayList<String> productsEtats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_orders_list);


        backBtn = findViewById(R.id.backToSellerMain);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.clientProductListsRecycleView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        View view = null;
        startAsynTask(view);




    }

    public void startAsynTask(View v)
    {


        AsyncTaskOrdersList4 asyncTaskOrdersList2 = new AsyncTaskOrdersList4();
        asyncTaskOrdersList2.execute();

        AsyncTaskOrdersList5 asyncTaskOrdersList3 = new AsyncTaskOrdersList5();
        asyncTaskOrdersList3.execute();

    }


    private class AsyncTaskOrdersList4 extends AsyncTask<Void,ArrayList<String>,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevlent.currentClient);


            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {

                        for(DataSnapshot val2 : snapshot.getChildren())
                        {
                            // Log.d("TAG", "onDataChange: order " + val2.getKey());

                            for(DataSnapshot val3 : val2.child("products").getChildren())
                            {

                                    String pPrice =  val3.child("product_price").getValue().toString() + " DA";
                                    String qte = val3.child("product_qte").getValue().toString();
                                    String etats = val3.child("etats").getValue().toString();
                                    productsPrice.add(pPrice);
                                    productsQte.add(qte);
                                    productsEtats.add(etats);

                                    publishProgress(productsPrice,productsQte,productsEtats);

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

            clientOrdersAdapter.setProductsPrice(values[0]);
            clientOrdersAdapter.setProductsQte(values[1]);
            clientOrdersAdapter.setProductsEtats(values[2]);

        }


    }

    private class AsyncTaskOrdersList5 extends AsyncTask<Void,ArrayList<String>,Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevlent.currentClient);

            ref.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {


                        for(DataSnapshot val2 : snapshot.getChildren())
                        {

                            for(DataSnapshot val3 : val2.child("products").getChildren())
                            {
                                // Log.d("TAG", "onDataChange: " + val3.getKey());

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
                                                        productsName.add(val6.child("pname").getValue().toString());
                                                        publishProgress(productsName);
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

            clientOrdersAdapter.setProductsName(values[0]);


            clientOrdersAdapter = new ClientOrdersAdapter(getApplicationContext(),productsName,productsPrice,productsQte,productsEtats);
            recyclerView.setAdapter(clientOrdersAdapter);


        }


    }
}