package com.kichou.imad.e_commerceapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerChangeProfilePicture;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.AdapterProductPhoto;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerPictureInterface;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class SellerProfileFragment extends Fragment implements SellerPictureInterface{


    private LinearLayout editProfile , productList , disconnectSeller , sellerOrders;

    private TextView sellerName , sellerPhone;

    // seller profile picture
    private CircleImageView profileImage;
    private SellerChangeProfilePicture sellerChangeProfilePicture;
    private static final int imagePickUpCode= 1000;
    private static final int PermissionCode = 1001;
    private Uri profileImgUri;
    //db

    private DatabaseReference sellerRefrence;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AlertDialog.Builder builder ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellerProfileFragment newInstance(String param1, String param2) {
        SellerProfileFragment fragment = new SellerProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_seller_profile, container, false);

        sellerChangeProfilePicture = new SellerChangeProfilePicture(this,getActivity());
        sellerPhone = view.findViewById(R.id.seller_phone);
        sellerPhone.setText(Prevlent.currentSeller);

        sellerName = view.findViewById(R.id.seller_name);
        sellerName.setText(Prevlent.sellerFirstName + " " + Prevlent.sellerLastName);



        builder = new AlertDialog.Builder(getActivity());

        builder.setCancelable(true);
        builder.setTitle("Confirmation");
        builder.setMessage("Voulez-vous vraiment Déconnecter");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Paper.init(getActivity());
                        Paper.book().destroy();
                        changeActivity(WelcomeActivity.class);

                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog dialog = builder.create();


        sellerOrders = view.findViewById(R.id.seller_orders);
        sellerOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(SellerOrdersActivity.class);
            }
        });

        editProfile = view.findViewById(R.id.edit_seller_profile);
        productList = view.findViewById(R.id.seller_products);
        disconnectSeller = view.findViewById(R.id.seller_exit);

        profileImage = view.findViewById(R.id.seller_profile_image);
        // set profile image

        Picasso.get().load(Prevlent.sellerImageProfileUrl).into(profileImage);




        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(EditSellerProfile.class);
            }
        });

        productList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                changeActivity(SellerProductList.class);
            }
        });

        disconnectSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                builder.show();
            }
        });

        // change image profile when click
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pickImageFromGallery();

            }
        });

        return view;
    }

    private void pickImageFromGallery()
    {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(i,imagePickUpCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == imagePickUpCode)
        {
            if(resultCode == Activity.RESULT_OK)
            {
               profileImgUri = data.getData();
               sellerChangeProfilePicture.onSellerProfilePictureChnage(profileImgUri);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PermissionCode :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                {
                    pickImageFromGallery();
                }
                else
                {
                    Toast.makeText(getActivity(),"Pas Droit accés au galerry",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void changeActivity(Class act)
    {
        Intent intent = new Intent(getActivity(),act) ;
        startActivity(intent);
    }


    @Override
    public void onPictureChangeSucces(String message, Uri imgUri)
    {
        profileImage.setImageURI(imgUri);
        uploadImageFromDb();
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureChangeFailed(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    private void uploadImageFromDb()
    {
        sellerRefrence  = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("profile_img");

        // retrevie the data from db
        sellerRefrence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {
                    String imgLink = String.valueOf(task.getResult().child("profile_img").getValue());
                    Prevlent.sellerImageProfileUrl = imgLink;
                   // Picasso.get().load(imgLink).into(profileImage);
                   // Log.d("TAG", "onComplete: " + String.valueOf(task.getResult().child("profile_img").getValue()));

                }
            }
        });



    }




}