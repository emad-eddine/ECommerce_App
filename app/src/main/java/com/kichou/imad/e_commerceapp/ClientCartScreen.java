package com.kichou.imad.e_commerceapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.sellers.EditTotalPrice;
import com.kichou.imad.e_commerceapp.utils.OrderDetails;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.CartRecycleAdapter;
import com.kichou.imad.e_commerceapp.view.ReviewAdapters;
import com.kichou.imad.e_commerceapp.view.ViewPagerAdapter;

import org.w3c.dom.Text;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientCartScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientCartScreen extends Fragment {


    private RecyclerView recyclerView;
    private CartRecycleAdapter cartRecycleAdapter;
    private TextView priceSomme;
    private Button checkOutBtn;
    private ProgressBar progressBar;


    private DatabaseReference productRef;

    private ArrayList<String> productImages = new ArrayList<>();
    private ArrayList<String> productName = new ArrayList<>();
    private ArrayList<String> productPrice = new ArrayList<>();
    private ArrayList<String> productQte = new ArrayList<>();
    private ArrayList<String> productKey = new ArrayList<>();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientCartScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClientCartScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientCartScreen newInstance(String param1, String param2) {
        ClientCartScreen fragment = new ClientCartScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_client_cart_screen, container, false);


        priceSomme = view.findViewById(R.id.prix_somme);
        checkOutBtn = view.findViewById(R.id.checkOutBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));

        /* get product key */
        View view1 = null;
        startAsynTask(view1);
        /**************************/


        checkOutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),ClientPaiementOne.class);
               // HashMap<String,Object> orderDetails = new HashMap<>();
                OrderDetails orderDetails = new OrderDetails();

                float somme = 0;

                for (int i = 0; i < productImages.size(); i++)
                {
                    CartRecycleAdapter.MyViewHolder holder = ((CartRecycleAdapter) recyclerView.getAdapter()).getViewByPosition(i);
                    View view = holder.itemView;
                    orderDetails.itemKey.add(productKey.get(i));

                    TextView price = view.findViewById(R.id.productPrice);
                    TextView qte = view.findViewById(R.id.productQte);
                    String p = price.getText().toString();
                    p = p.substring(0,p.length()-2);

                    orderDetails.itemPrice.add(p);
                    orderDetails.itemQte.add(qte.getText().toString());


                    somme = somme + Float.parseFloat(p.trim());
                }


                orderDetails.orderAmount = ""+somme;
                intent.putExtra("order",orderDetails);
                startActivity(intent);
            }
        });

        return view;
    }

    public void startAsynTask(View v)
    {
        CartDetailsAsyncTask cartDetailsAsyncTask = new CartDetailsAsyncTask();
        cartDetailsAsyncTask.execute();
    }

    private class CartDetailsAsyncTask extends AsyncTask<Void,ArrayList<String>,Void> implements EditTotalPrice {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // get product name price qteAva

            productRef = FirebaseDatabase.getInstance().getReference().child("client_cart").child(Prevlent.currentClient);

            productRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot pkey : snapshot.getChildren())
                    {
                        String cartPKey = pkey.getKey();

                        DatabaseReference pRef = FirebaseDatabase.getInstance().getReference().child("sellers");
                        pRef.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                for(DataSnapshot valProductKey : snapshot.getChildren())
                                {
                                    for(DataSnapshot valProduct : valProductKey.child("products").getChildren())
                                    {
                                        if(valProduct.getKey().equals(cartPKey))
                                        {
                                            productKey.add(cartPKey);
                                            productName.add(valProduct.child("pname").getValue().toString());
                                            productPrice.add(valProduct.child("price").getValue().toString());
                                            productQte.add(valProduct.child("pQte").getValue().toString());
                                            productImages.add(valProduct.child("Images").child("img0").getValue().toString());
                                            publishProgress(productImages,productName,productPrice,productQte,productKey);
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

            cartRecycleAdapter = new CartRecycleAdapter(getActivity(),values[0],values[1],values[2],values[3],this,values[4]);
            recyclerView.setAdapter(cartRecycleAdapter);

            float total = 0;
            for(String p : values[2])
            {
                total = total + Float.parseFloat(p);
            }
            priceSomme.setText("Somme : " + total + " DA");
        }

        @Override
        public void onPriceChange()
        {
            float prixTotal = 0;
            for (int i = 0; i < productImages.size(); i++)
            {
                CartRecycleAdapter.MyViewHolder holder = ((CartRecycleAdapter) recyclerView.getAdapter()).getViewByPosition(i);
                View view = holder.itemView;
                TextView prix = view.findViewById(R.id.productPrice);

                String p = prix.getText().toString();
                p = p.substring(0,p.length()-2);
                float prixCast = Float.parseFloat(p.trim());

                prixTotal = prixTotal + prixCast;


            }


            priceSomme.setText("Somme : " + prixTotal + " DA");
        }
    }



}