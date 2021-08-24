package com.kichou.imad.e_commerceapp.controller.clients;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.model.clientModel.Client;
import com.kichou.imad.e_commerceapp.view.clientView.ClientLoginView;

public class ClientLoginControll implements ClientLoginController {

    ClientLoginView clientLoginView;

    public ClientLoginControll(ClientLoginView clientLoginView)
    {
        this.clientLoginView = clientLoginView;
    }


    @Override
    public void onClientLogin(String clientPhone, String password)
    {
        Client client = new Client(clientPhone,password);
        int loginCode = client.isLoginValid();
        if(loginCode == 0)
        {
            clientLoginView.OnLoginError("Please enter all fields");
        } else  if (loginCode == 1)
        {
            clientLoginView.OnLoginError("Please enter Password greater the 6 char");
        }
        else  if (loginCode == 2)
        {
            clientLoginView.OnLoginError("Please enter valide phone number");
        }
        else
        {
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.child("clients").child(client.getClientPhone()).exists())
                    {
                        String clientPhoneDb = snapshot.child("clients").child(client.getClientPhone()).child("client_phone").getValue().toString();
                        String clientPasswordDb = snapshot.child("clients").child(client.getClientPhone()).child("client_password").getValue().toString();

                        Client DBClient = new Client(clientPhoneDb,clientPasswordDb);


                        if(DBClient.getClientPhone().equals(client.getClientPhone()))
                        {
                            if(DBClient.getClientPassword().equals(client.getClientPassword()))
                            {
                                clientLoginView.OnLoginSuccess("Connected...");
                            }
                            else
                            {
                                clientLoginView.OnLoginError("Password wrong");
                            }

                        }

                    }
                    else
                    {
                        clientLoginView.OnLoginError("Client Dont exist");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });








        }





    }
}
