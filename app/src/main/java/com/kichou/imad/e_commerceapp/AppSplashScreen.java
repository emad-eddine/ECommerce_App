package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kichou.imad.e_commerceapp.controller.clients.ClientLoginControll;
import com.kichou.imad.e_commerceapp.controller.clients.ClientLoginController;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerLoginControll;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerLoginController;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.clientView.ClientLoginView;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerLoginView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class AppSplashScreen extends AppCompatActivity implements ClientLoginView, SellerLoginView {

    // animation
    private ImageView splashImgView;
    private Animation animSlide;


    // welcome Activity
    private ClientLoginController clientLoginController;
    private SellerLoginController sellerLoginController;

    String userType = "";
    String userPhoneKey = "";
    String userPasswordKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_splash_screen);

//        // animation
//        splashImgView = findViewById(R.id.splashImgView);
//        animSlide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_screen_animation);
//        animSlide.setRepeatCount(Animation.INFINITE);
//        splashImgView.startAnimation(animSlide);
//        animSlide.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                splashImgView.startAnimation(animSlide);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });


        // main Activity

        Paper.init(this);

        // controllers

        clientLoginController = new ClientLoginControll(this);
        sellerLoginController = new SellerLoginControll(this);

        userType = Paper.book().read(Prevlent.userType);
        userPhoneKey = Paper.book().read(Prevlent.userPhoneKey);
        userPasswordKey = Paper.book().read(Prevlent.userPasswordKey);




        if(userPhoneKey != null && userPasswordKey!= null && userType!= null)
        {
            if(!TextUtils.isEmpty(userPhoneKey) && !TextUtils.isEmpty(userPasswordKey) && !TextUtils.isEmpty(userType))
            {
                if(userType.equals("Client"))
                {
                    clientLoginController.onClientLogin(userPhoneKey,userPasswordKey);
                    Prevlent.currentClient = userPhoneKey;
                }
                else if(userType.equals("Seller"))
                {
                    sellerLoginController.onSellerLogin(userPhoneKey,userPasswordKey);
                    Prevlent.currentSeller = userPhoneKey;
                }


            }

        }
        else
        {
            Intent intent = new Intent(AppSplashScreen.this,WelcomeActivity.class);
            finish();
            startActivity(intent);
        }





    }

    @Override
    public void OnLoginSuccess(String message) {
        if(userType.equals("Client"))
        {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AppSplashScreen.this,ClientMainActivity.class);
            getClientInfo();
            getClientProfilePicture();

            finish();
            startActivity(intent);
        }
        else if(userType.equals("Seller"))
        {
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AppSplashScreen.this,SellerMainActivity.class);
            getSellerInfo();
            getSellerProfileUrl();

            finish();
            startActivity(intent);
        }
    }

    @Override
    public void OnLoginError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AppSplashScreen.this,WelcomeActivity.class);
        finish();
        startActivity(intent);
    }



    private void getSellerInfo()
    {
        DatabaseReference sellerRefrence;
        sellerRefrence  = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("");

        // retrevie the data from db
        sellerRefrence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {
                    String firstName , lastName;
                    firstName = String.valueOf(task.getResult().child("seller_first_name").getValue());
                    lastName = String.valueOf(task.getResult().child("seller_last_name").getValue());
                    Prevlent.sellerFirstName = firstName;
                    Prevlent.sellerLastName = lastName;
                }
            }
        });

    }

    public void getSellerProfileUrl()
    {
       DatabaseReference sellerRefrence  = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("profile_img");

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

                }
            }
        });
    }


    private void getClientInfo()
    {
        DatabaseReference clientRefrence  = FirebaseDatabase.getInstance().getReference().child("clients").child(Prevlent.currentClient).child("");

        // retrevie the data from db
        clientRefrence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {
                    String firstName , lastName;
                    firstName = String.valueOf(task.getResult().child("client_first_name").getValue());
                    lastName = String.valueOf(task.getResult().child("client_last_name").getValue());

                   Prevlent.clientFirstName = firstName;
                   Prevlent.clientLastName = lastName;

                }
            }
        });
    }


    private void getClientProfilePicture()
    {
        DatabaseReference clientRefrence  = FirebaseDatabase.getInstance().getReference().child("clients").child(Prevlent.currentClient).child("profile_img");

        // retrevie the data from db
        clientRefrence.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {
                    String imgLink = String.valueOf(task.getResult().child("profile_img").getValue());

                    Prevlent.clientImageProfileUrl = imgLink;

                }
            }
        });
    }
}