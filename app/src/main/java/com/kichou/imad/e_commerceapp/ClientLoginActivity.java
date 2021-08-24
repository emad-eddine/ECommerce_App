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
import com.kichou.imad.e_commerceapp.controller.clients.ClientLoginControll;
import com.kichou.imad.e_commerceapp.controller.clients.ClientLoginController;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.clientView.ClientLoginView;

import io.paperdb.Paper;

public class ClientLoginActivity extends AppCompatActivity implements ClientLoginView {

    private TextView toClientSignupPage;
    private TextView toSellerLoginPage;

    private ImageView showPassword;

    private EditText clientPhone,clientPassword;

    private Button clientLoginBtn;

    private ClientLoginController clientLoginController;


    private ProgressDialog progressDialog;


    private CheckBox rememberCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_log_in);

        progressDialog = new ProgressDialog(this);

        showPassword = findViewById(R.id.clientShowPassword);

        toClientSignupPage = findViewById(R.id.toClientSingupPage);
        toSellerLoginPage = findViewById(R.id.toSellerLoginPage);
        //

        clientPhone = findViewById(R.id.clientPhone);
        clientPassword = findViewById(R.id.clientPassword);

        clientLoginBtn = findViewById(R.id.clientLogInBtn);

        clientLoginController = new ClientLoginControll(this);

        // check box

        rememberCheckBox = findViewById(R.id.RemembreClientCheckBox);

        // intialise paper dependecy

        Paper.init(this);


        toClientSignupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientLoginActivity.this,ClientSignupActivity.class);
                startActivity(intent);
            }
        });


        toSellerLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientLoginActivity.this,SellerLoginActivity.class);
                startActivity(intent);
            }
        });


        clientLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rememberCheckBox.isChecked())
                {
                    Paper.book().write(Prevlent.userType,"Client");
                    Paper.book().write(Prevlent.userPhoneKey,clientPhone.getText().toString().trim());
                    Paper.book().write(Prevlent.userPasswordKey,clientPassword.getText().toString().trim());
                }


                clientLoginController.onClientLogin(clientPhone.getText().toString().trim(),
                        clientPassword.getText().toString().trim());

                progressDialog.setTitle("Checking Client Account");
                progressDialog.setMessage("Please Wait....!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

            }
        });

        showPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    clientPassword.setTransformationMethod(null);
                    return true;
                }
                else
                {
                    clientPassword.setTransformationMethod(new PasswordTransformationMethod());
                    return true;
                }

            }
        });

    }

    @Override
    public void OnLoginSuccess(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        Intent intent = new Intent(ClientLoginActivity.this,ClientMainActivity.class);
        Prevlent.currentClient = clientPhone.getText().toString().trim();

        getClientInfo();
        getClientProfilePicture();

        finish();
        startActivity(intent);
    }

    @Override
    public void OnLoginError(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
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

        clientRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Object link;
                link = snapshot.child("profile_img").getValue();

                if(link == null)
                {
                    link = "https://firebasestorage.googleapis.com/v0/b/ecommerceapp-8434f.appspot.com/o/user.png?alt=media&token=83f9c610-e2be-49fe-8fd5-d50720b64277";
                    Prevlent.clientImageProfileUrl = link.toString();
                }
                else
                {
                    if(link.equals(""))
                    {
                        link = "https://firebasestorage.googleapis.com/v0/b/ecommerceapp-8434f.appspot.com/o/user.png?alt=media&token=83f9c610-e2be-49fe-8fd5-d50720b64277";
                        Prevlent.clientImageProfileUrl = link.toString();
                    }
                    else
                    {
                        Prevlent.clientImageProfileUrl = link.toString();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}