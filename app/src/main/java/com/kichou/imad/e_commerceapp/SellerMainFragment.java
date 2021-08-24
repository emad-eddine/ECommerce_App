package com.kichou.imad.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellerMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellerMainFragment extends Fragment implements View.OnClickListener {

    private ImageButton menBtn,womenBtn,devicesBtn,gadgetBtn,gameBtn;

    private TextView homeScreen;
    private ImageView profileBtn;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public SellerMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerMainFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static SellerMainFragment newInstance(String param1, String param2) {
        SellerMainFragment fragment = new SellerMainFragment();
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

        View view = inflater.inflate(R.layout.fragment_seller_main, container, false);
        menBtn = view.findViewById(R.id.menBtn);
        womenBtn = view.findViewById(R.id.femmeBtn);
        devicesBtn = view.findViewById(R.id.devicesBtn);
        gadgetBtn = view.findViewById(R.id.gadgetBtn);
        gameBtn = view.findViewById(R.id.gameBtn);




        menBtn.setOnClickListener(this);
        womenBtn.setOnClickListener(this);
        devicesBtn.setOnClickListener(this);
        gadgetBtn.setOnClickListener(this);
        gameBtn.setOnClickListener(this);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.menBtn)
        {
            moveToAddProduct("Homme");
        }
        else  if(v.getId() == R.id.femmeBtn)
        {
            moveToAddProduct("Femme");
        }
        else  if(v.getId() == R.id.devicesBtn)
        {
            moveToAddProduct("Dispositifs");
        }
        else  if(v.getId() == R.id.gadgetBtn)
        {
            moveToAddProduct("Gadgets");
        }
        else  if(v.getId() == R.id.gameBtn)
        {
            moveToAddProduct("Jeux");
        }
    
    }

    private void changeActivity(Class act)
    {
       // Intent intent = new Intent(this,act) ;
       // startActivity(intent);
    }

    private void moveToAddProduct(String productType)
    {
        Intent intent = new Intent(getActivity(),SellerAddProductActivity.class);
        intent.putExtra("gategory",productType);
        startActivity(intent);

    }
}