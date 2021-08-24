package com.kichou.imad.e_commerceapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.EditTotalPrice;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class CartRecycleAdapter extends RecyclerView.Adapter<CartRecycleAdapter.MyViewHolder> {


    private Context ctx;
    private ArrayList<String> productsImages = new ArrayList<>();
    private ArrayList<String> productsName = new ArrayList<>();
    private ArrayList<String> productsPrice = new ArrayList<>();
    private ArrayList<String> productsQte = new ArrayList<>();
    private ArrayList<String> productsKey = new ArrayList<>();
    private EditTotalPrice editTotalPrice ;
    HashMap<Integer,MyViewHolder> holderlist;
    private AlertDialog.Builder dialogue;

    public CartRecycleAdapter(Context ctx, ArrayList<String> productImages, ArrayList<String> productName, ArrayList<String> productPrice, ArrayList<String> productQte , EditTotalPrice editTotalPrice ,ArrayList<String> productsKey) {
        this.ctx = ctx;
        this.productsImages = productImages;
        this.productsName = productName;
        this.productsPrice = productPrice;
        this.productsQte = productQte;
        this.editTotalPrice = editTotalPrice;
        this.productsKey = productsKey;
        holderlist = new HashMap<>();
        dialogue = new AlertDialog.Builder(ctx);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.cart_row, parent, false);

        return new CartRecycleAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.productName.setText(productsName.get(position));
        holder.productPrice.setText(productsPrice.get(position) + " DA");
        Picasso.get().load(productsImages.get(position)).into(holder.proImgView);

        if(!holderlist.containsKey(position))
        {
            Log.d("TAG", "onBindViewHolder: key ");
            holderlist.put(position,holder);
        }

        // calculate total

        float total = 0;

    }

    @Override
    public int getItemCount()
    {
        return productsImages.size();
    }

    public CartRecycleAdapter.MyViewHolder getViewByPosition(int position) {
        return holderlist.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView productName , productPrice , productQte;
        ImageView minusBtn , plusBtn,proImgView,deleteProduct;

        int position;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice  = itemView.findViewById(R.id.productPrice);
            productQte = itemView.findViewById(R.id.productQte);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            plusBtn = itemView.findViewById(R.id.plusBtn);
            proImgView = itemView.findViewById(R.id.proImgView);
            deleteProduct = itemView.findViewById(R.id.deleteBtn);



            plusBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int buyQte = Integer.parseInt(productQte.getText().toString());
                    int avaiQte = Integer.parseInt(productsQte.get(getAdapterPosition()));


                    if(buyQte < avaiQte)
                    {
                        productQte.setText("" + (++buyQte));
                        float productPriceCasted = Float.parseFloat(productsPrice.get(getAdapterPosition()));
                        productPrice.setText(""+(productPriceCasted * buyQte));


                        editTotalPrice.onPriceChange();
                    }

                }
            });


            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    int buyQte = Integer.parseInt(productQte.getText().toString());

                    if(buyQte > 1 )
                    {
                        buyQte = buyQte - 1;
                        productQte.setText("" + buyQte);
                        float productPriceCasted = Float.parseFloat(productsPrice.get(getAdapterPosition()));
                        productPriceCasted = (productPriceCasted * buyQte) ;
                        productPrice.setText(""+productPriceCasted);
                        editTotalPrice.onPriceChange();
                    }
                }
            });



            dialogue.setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            int pos = getAdapterPosition();
                            productsImages.remove(position);
                            productsName.remove(position);
                            productsPrice.remove(position);
                            productsQte.remove(position);
                            holderlist.remove(position);
                            notifyDataSetChanged();

                            // remove from db

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("client_cart").child(Prevlent.currentClient).child(productsKey.get(position));
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot)
                                {
                                    snapshot.getRef().removeValue();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            editTotalPrice.onPriceChange();

                        }
                    });
            dialogue.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            deleteProduct.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialogue.setCancelable(true);
                    dialogue.setTitle("Supprimer produit du panier");
                    dialogue.setMessage("Voulez-vraiment supprimer le produit");
                    AlertDialog dialog = dialogue.create();
                    dialog.show();
                    position = getAdapterPosition();
                }
            });


        }
    }
}
