package com.kichou.imad.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.clients.ClientChoosesGategory;
import com.kichou.imad.e_commerceapp.controller.sellers.SeeProductDetails;
import com.kichou.imad.e_commerceapp.view.AdapterBestProducts;
import com.kichou.imad.e_commerceapp.view.AdapterCategoryPhotos;
import com.kichou.imad.e_commerceapp.view.AdapterNewProducts;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link clientMainScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class clientMainScreen extends Fragment implements ClientChoosesGategory, SeeProductDetails {

    private RecyclerView gategoryRecycleView;
    private AdapterCategoryPhotos adapterCategoryPhotos;

    private ImageView serchBtn;
    private EditText serchText;

    private ProgressBar progressBar;


    private int[] catImages =
            {
                    R.drawable.men_shoes,
                    R.drawable.femme,
                    R.drawable.devices,
                    R.drawable.gadgets,
                    R.drawable.game
            };

    private String[] imgTitles;



    private RecyclerView newAddedProductView;
    private AdapterNewProducts adapterNewProducts;

    private ArrayList<String> newProductImages = new ArrayList<>();

    private ArrayList<String> newProductName =  new ArrayList<>();

    private ArrayList<String> newProductPrice =  new ArrayList<>();

    private ArrayList<String> newProductKey =  new ArrayList<>();

    private SeeProductDetails seeProductDetails;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public clientMainScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment clientMainScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static clientMainScreen newInstance(String param1, String param2) {
        clientMainScreen fragment = new clientMainScreen();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_client_main_screen, container, false);
        progressBar = view.findViewById(R.id.progressBar2);
        seeProductDetails = this;
        // catgeroy recyce view
        gategoryRecycleView = view.findViewById(R.id.categoryRecycleView);
        gategoryRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        imgTitles = getResources().getStringArray(R.array.category_vende);
        adapterCategoryPhotos = new AdapterCategoryPhotos(getActivity(),catImages,imgTitles,this);
        gategoryRecycleView.setAdapter(adapterCategoryPhotos);

        // new added
        newAddedProductView = view.findViewById(R.id.newPoductsRec);
        DatabaseReference pRef= FirebaseDatabase.getInstance().getReference().child("sellers");
        pRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, YYY");
                String saveCurrentDate = currentDate.format(calendar.getTime());


                for(DataSnapshot val : snapshot.getChildren())
                {
                    for (DataSnapshot val2 : val.child("products").getChildren())
                    {
                        String productDate = val2.child("date").getValue().toString();

                        if(saveCurrentDate.equals(productDate))
                        {
                            newProductImages.add(val2.child("Images").child("img0").getValue().toString());
                            newProductName.add(val2.child("pname").getValue().toString());
                            newProductPrice.add(val2.child("price").getValue().toString() + " DA");
                            newProductKey.add(val2.getKey());

                        }
                    }
                }
                newAddedProductView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                adapterNewProducts = new AdapterNewProducts(getActivity(),newProductImages,newProductName,newProductPrice,newProductKey,seeProductDetails);
                newAddedProductView.setAdapter(adapterNewProducts);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        serchBtn = view.findViewById(R.id.imageView2);
        serchText = view.findViewById(R.id.editText);



        serchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String serch = serchText.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("sellers");

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for(DataSnapshot val : snapshot.getChildren())
                        {
                            for(DataSnapshot val2 : val.child("products").getChildren())
                            {
                                if(serch.equals(val2.child("pname").getValue().toString()))
                                {
                                    Intent intent = new Intent(getActivity(),BuyAProductActivity.class);
                                    intent.putExtra("pKey",val2.getKey());
                                    // Toast.makeText(this,pKey,Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    break;
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
        });


        return view;
    }

    @Override
    public void OnchoosingGategroyByClient(String gatName)
    {

        Intent intent  = new Intent(getActivity(),ClientGatDetails.class);
        intent.putExtra("gat",gatName);
        startActivity(intent);
    }



    private void changeActivity(Class act)
    {
        Intent intent  = new Intent(getActivity(),act);
        startActivity(intent);
    }

    @Override
    public void seeDetails(String pKey)
    {
        Intent intent = new Intent(getActivity(),BuyAProductActivity.class);
        intent.putExtra("pKey",pKey);
        // Toast.makeText(this,pKey,Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}