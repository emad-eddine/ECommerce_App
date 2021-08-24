package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kichou.imad.e_commerceapp.EditSellerProfile;
import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.EditProductInterface;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.utils.ProductRefImages;
import com.kichou.imad.e_commerceapp.view.sellerView.SellerAddProductView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductListPhoto extends RecyclerView.Adapter<AdapterProductListPhoto.MyViewHolder> {

    private Context context;

    private ArrayList<String> imagesUrl = new ArrayList<>();
    private ArrayList<String> pNames = new ArrayList<>();
    private ArrayList<String> pPrice = new ArrayList<>();
    private ArrayList<String> pQte = new ArrayList<>();
    private ArrayList<String> pKey = new ArrayList<>();
    private ArrayList<ProductRefImages> pRef = new ArrayList<>();

    private EditProductInterface editProductInterface;



    public AdapterProductListPhoto(EditProductInterface editProductInterface, Context context, ArrayList<String> imagesUrl, ArrayList<String> pNames, ArrayList<String> pPrice, ArrayList<String> pQte, ArrayList<String> pKey,ArrayList<ProductRefImages> pRef) {

        this.editProductInterface = editProductInterface;
        this.context = context;
        this.imagesUrl = imagesUrl;
        this.pNames = pNames;
        this.pPrice = pPrice;
        this.pQte = pQte;
        this.pKey = pKey;
        this.pRef = pRef;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.seller_product_list_row,parent,false);
        return new AdapterProductListPhoto.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.productName.setText(pNames.get(position));
        holder.productPrice.setText(pPrice.get(position) + " DA");
        holder.productQte.setText(pQte.get(position));

        Picasso.get().load(imagesUrl.get(position)).into(holder.productImg);

    }

    @Override
    public int getItemCount() {
        return imagesUrl.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView productName,productPrice,productQte;
        ImageView editBtn ,deleteBtn;
        ImageView productImg;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            productImg = itemView.findViewById(R.id.proImgView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQte = itemView.findViewById(R.id.productQte);
            editBtn = itemView.findViewById(R.id.Edit_productBtn);
            deleteBtn = itemView.findViewById(R.id.Delete_productBtn);



            // remove product
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    // remove product images and details from db and arraylist

                    // remove from arraylist
                    int pos = getAdapterPosition();

                    imagesUrl.remove(getAdapterPosition());
                    pNames.remove(getAdapterPosition());
                    pPrice.remove(getAdapterPosition());
                    pQte.remove(getAdapterPosition());

                    notifyDataSetChanged();


                    // remove from db

                    String key = pKey.get(pos);

                    int index = 0;
                    for( ProductRefImages refP : pRef)
                    {
                        if(refP.getProductKey().equals(key))
                        {
                            for(String url : refP.getProductImagesUrl())
                            {
                                // delete from storage

                                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(url);

                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "onSuccess: deleted file");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "onFailure: did not delete file");
                                    }
                                });
                            }
                        }
                        index = index+1;

                    }

                    // remove from real time db

                    DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().child("sellers").child(Prevlent.currentSeller).child("products").child(key);
                    if(dbNode.removeValue().isSuccessful())
                    {
                        Log.d("TAG", "product removed");

                        pKey.remove(pos);
                        pRef.remove(index);

                    }
                    else
                    {
                        Log.d("TAG", "failed");
                    }

                    // remove from arraylist



                }
            });


            // edit product

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int pos = getAdapterPosition();

                    String productName = pNames.get(pos);
                    String productPrice =  pPrice.get(pos);
                    String productQte = pQte.get(pos);
                    String key = pKey.get(pos);
                    editProductInterface.onProductEdit(key,productName,productPrice,productQte);
                }
            });



        }
    }
}
