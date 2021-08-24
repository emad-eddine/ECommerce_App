package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerLoginControll;
import com.kichou.imad.e_commerceapp.controller.sellers.SellerLoginController;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerLoginView;

import io.paperdb.Paper;

public class SellerLoginActivity extends AppCompatActivity implements SellerLoginView {

    private TextView toSellerSignUpPage, toClientLoginPage;

    private ImageView showPassword;

    private EditText sellerPhone,sellerPassword;

    private Button sellerLoginBtn;


    private SellerLoginController sellerLoginController;

    private ProgressDialog progressDialog;

    private CheckBox sellerCheckBoxRemember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_log_in);

        Paper.init(this);

        progressDialog = new ProgressDialog(this);

        sellerLoginController = new SellerLoginControll(this);

        toSellerSignUpPage = findViewById(R.id.toSellerSinupPage);

        toClientLoginPage = findViewById(R.id.toClientLoginPage);

        showPassword = findViewById(R.id.sellerShowPassword);

        sellerCheckBoxRemember = findViewById(R.id.sellerCheckBoxRemember);

        ///

        sellerPhone = findViewById(R.id.sellerPhone);
        sellerPassword = findViewById(R.id.sellerPassword);

        sellerLoginBtn = findViewById(R.id.sellerLoginBtn);


        toSellerSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerLoginActivity.this,SellerSignupActivity.class);
                startActivity(intent);
            }
        });
        toClientLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerLoginActivity.this,ClientLoginActivity.class);
                startActivity(intent);
            }
        });


        sellerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sellerCheckBoxRemember.isChecked())
                {

                    Paper.book().write(Prevlent.userType,"Seller");
                    Paper.book().write(Prevlent.userPhoneKey,sellerPhone.getText().toString().trim());
                    Paper.book().write(Prevlent.userPasswordKey,sellerPassword.getText().toString().trim());

                }




                sellerLoginController.onSellerLogin(sellerPhone.getText().toString().trim(),
                        sellerPassword.getText().toString().trim());

                progressDialog.setTitle("Checking Seller Account");
                progressDialog.setMessage("Please Wait....!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        });

        // show password

        showPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    sellerPassword.setTransformationMethod(null);
                    return true;
                }
                else
                {
                    sellerPassword.setTransformationMethod(new PasswordTransformationMethod());
                    return true;
                }

            }
        });


    }

    @Override
    public void OnLoginSuccess(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        Intent intent = new Intent(SellerLoginActivity.this,SellerMainActivity.class);
        Prevlent.currentSeller = sellerPhone.getText().toString().trim();
        getSellerInfo();
        getSellerProfileUrl();
        finish();
        startActivity(intent);
    }

    @Override
    public void OnLoginError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
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
        DatabaseReference clientRefrence  = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("profile_img");

        clientRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Object link;
                link = snapshot.child("profile_img").getValue();

                if(link == null)
                {
                    String link1 = "https://firebasestorage.googleapis.com/v0/b/ecommerceapp-8434f.appspot.com/o/user.png?alt=media&token=83f9c610-e2be-49fe-8fd5-d50720b64277";
                    Prevlent.sellerImageProfileUrl = link1;
                }
                else
                {
                    if(link.equals(""))
                    {
                        String link1 = "https://firebasestorage.googleapis.com/v0/b/ecommerceapp-8434f.appspot.com/o/user.png?alt=media&token=83f9c610-e2be-49fe-8fd5-d50720b64277";
                        Prevlent.sellerImageProfileUrl =  link1;
                    }
                    else
                    {
                        Prevlent.sellerImageProfileUrl = link.toString();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}