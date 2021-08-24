package com.kichou.imad.e_commerceapp.controller.clients;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.model.clientModel.Client;
import com.kichou.imad.e_commerceapp.view.clientView.ClientSignupView;

import java.util.HashMap;

public class ClientSignUpControll implements ClientSignupController
{

    ClientSignupView clientSignupView;

    public ClientSignUpControll(ClientSignupView clientSignupView)
    {
        this.clientSignupView = clientSignupView;
    }


    @Override
    public void onClientSignup(String clientFirstName, String clientLastName, String clientPhone, String password) {
        Client client = new Client(clientFirstName,clientLastName,clientPhone,password);
        int loginCode = client.isValid();
        if(loginCode == 0)
        {
            clientSignupView.OnSignupError("Please enter all fields");
        } else  if (loginCode == 1)
        {
            clientSignupView.OnSignupError("Please enter Password greater the 6 char");
        }
        else  if (loginCode == 1)
        {
            clientSignupView.OnSignupError("Please enter valide phone number");
        }
        else {
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child("client").child(client.getClientPhone()).exists()))
                    {
                        HashMap<String,Object> clientMap = new HashMap<>();

                        clientMap.put("client_first_name",client.getClientFirstName());
                        clientMap.put("client_last_name",client.getClientLastName());
                        clientMap.put("client_phone",client.getClientPhone());
                        clientMap.put("client_password",client.getClientPassword());

                        rootRef.child("clients").child(client.getClientPhone()).updateChildren(clientMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            clientSignupView.OnSignupSuccess("login Successful");
                                        }
                                        else
                                            clientSignupView.OnSignupSuccess("error occur");
                                    }
                                });
                    }
                    else
                    {
                        clientSignupView.OnSignupSuccess("already exsist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
