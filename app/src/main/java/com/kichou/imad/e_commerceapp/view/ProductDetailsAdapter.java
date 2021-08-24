package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.SeeProductDetails;
import com.kichou.imad.e_commerceapp.utils.ProductRefImages;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.MyViewHolder> {

    private Context ctx;
    private ArrayList<String> imagesUrl = new ArrayList<>();
    private ArrayList<String> pNames = new ArrayList<>();
    private ArrayList<String> pPrice = new ArrayList<>();
    private ArrayList<String> pQte = new ArrayList<>();
    private ArrayList<String> pKey = new ArrayList<>();
    private ArrayList<ProductRefImages> pRef = new ArrayList<>();
    private SeeProductDetails seeProductDetailsInter;


    public ProductDetailsAdapter(Context ctx, ArrayList<String> imagesUrl, ArrayList<String> pNames, ArrayList<String> pPrice, ArrayList<String> pQte, ArrayList<String> pKey, ArrayList<ProductRefImages> pRef, SeeProductDetails seeProductDetails) {
        this.ctx = ctx;
        this.imagesUrl = imagesUrl;
        this.pNames = pNames;
        this.pPrice = pPrice;
        this.pQte = pQte;
        this.pKey = pKey;
        this.pRef = pRef;
        this.seeProductDetailsInter = seeProductDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.product_details_row,parent,false);
        return new ProductDetailsAdapter.MyViewHolder(view);
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
    public int getItemCount()
    {
        return imagesUrl.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView productName,productPrice,productQte,seeProductDetails;

        ImageView productImg;


        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            productImg = itemView.findViewById(R.id.proImgView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQte = itemView.findViewById(R.id.productQte);
            seeProductDetails = itemView.findViewById(R.id.see_detailsBtn);

            seeProductDetails.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    seeProductDetailsInter.seeDetails(pKey.get(getAdapterPosition()));
                }
            });


        }
    }
}
