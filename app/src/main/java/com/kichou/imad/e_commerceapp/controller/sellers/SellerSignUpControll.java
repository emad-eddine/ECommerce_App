package com.kichou.imad.e_commerceapp.controller.sellers;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.model.sellerModel.Seller;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerSignupView;

import java.util.HashMap;

public class SellerSignUpControll implements SellerSignupController {

    SellerSignupView sellerSignupView;

    public SellerSignUpControll(SellerSignupView sellerSignupView)
    {
        this.sellerSignupView = sellerSignupView;
    }


    @Override
    public void onSellerSignup(String sellerFirstName, String sellerLastName, String sellerPhone, String password) {
        Seller seller = new Seller(sellerFirstName,sellerLastName,sellerPhone,password);
        int loginCode = seller.isSellerValid();
        if(loginCode == 0)
        {
            sellerSignupView.OnSignupError("Please enter all fields");
        } else  if (loginCode == 1)
        {
            sellerSignupView.OnSignupError("Please enter Password greater the 6 char");
        }
        else  if (loginCode == 1)
        {
            sellerSignupView.OnSignupError("Please enter valide phone number");
        }
        else {
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child("client").child(seller.getSellerPhone()).exists()))
                    {
                        HashMap<String,Object> clientMap = new HashMap<>();

                        clientMap.put("seller_first_name",seller.getSellerFirstName());
                        clientMap.put("seller_last_name",seller.getSellerLastName());
                        clientMap.put("seller_phone",seller.getSellerPhone());
                        clientMap.put("seller_password",seller.getSellerPassword());

                        rootRef.child("sellers").child(seller.getSellerPhone()).updateChildren(clientMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            sellerSignupView.OnSignupSuccess("login Successful");
                                        }
                                        else
                                            sellerSignupView.OnSignupSuccess("error occur");
                                    }
                                });
                    }
                    else
                    {
                        sellerSignupView.OnSignupSuccess("already exsist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}
