package com.kichou.imad.e_commerceapp.controller.sellers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.model.sellerModel.Seller;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerLoginView;

public class SellerLoginControll implements SellerLoginController {


    SellerLoginView sellerLoginView;

    public SellerLoginControll(SellerLoginView sellerLoginView)
    {
        this.sellerLoginView = sellerLoginView;
    }






    @Override
    public void onSellerLogin(String sellerPhone, String password) {

        Seller seller = new Seller(sellerPhone,password);
        int loginCode = seller.isLoginValid();
        if(loginCode == 0)
        {
            sellerLoginView.OnLoginError("Please enter all fields");
        } else  if (loginCode == 1)
        {
            sellerLoginView.OnLoginError("Please enter Password greater the 6 char");
        }
        else  if (loginCode == 2)
        {
            sellerLoginView.OnLoginError("Please enter valide phone number");
        }
        else {
            final DatabaseReference rootRef;
            rootRef = FirebaseDatabase.getInstance().getReference();

            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("sellers").child(seller.getSellerPhone()).exists()) {
                        String sellerPhoneDb = snapshot.child("sellers").child(seller.getSellerPhone()).child("seller_phone").getValue().toString();
                        String sellerPasswordDb = snapshot.child("sellers").child(seller.getSellerPhone()).child("seller_password").getValue().toString();

                        Seller DBSeller = new Seller(sellerPhoneDb, sellerPasswordDb);


                        if (DBSeller.getSellerPhone().equals(seller.getSellerPhone())) {
                            if (DBSeller.getSellerPassword().equals(seller.getSellerPassword())) {
                                Prevlent.currentSeller = seller.getSellerPhone();
                                sellerLoginView.OnLoginSuccess("Connected...");
                            } else {
                                sellerLoginView.OnLoginError("Password wrong");
                            }

                        }

                    } else {
                        sellerLoginView.OnLoginError("seller Dont exist");
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

        }
}
