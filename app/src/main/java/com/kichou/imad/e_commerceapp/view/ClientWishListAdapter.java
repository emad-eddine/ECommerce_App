package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.SeeProductDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientWishListAdapter extends RecyclerView.Adapter<ClientWishListAdapter.MyViewHolder> {

    private Context ctx;
    private ArrayList<String> imgaeUrl = new ArrayList<>();
    private ArrayList<String> pNames = new ArrayList<>();
    private ArrayList<String> pPrice = new ArrayList<>();
    private ArrayList<String> pKey = new ArrayList<>();
    private SeeProductDetails seeProductDetailsInter;

    public ClientWishListAdapter(Context ctx, ArrayList<String> imgaeUrl, ArrayList<String> pNames, ArrayList<String> pPrice, ArrayList<String> pKey,SeeProductDetails seeProductDetailsInter) {
        this.ctx = ctx;
        this.imgaeUrl = imgaeUrl;
        this.pNames = pNames;
        this.pPrice = pPrice;
        this.pKey = pKey;
        this.seeProductDetailsInter = seeProductDetailsInter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater  = LayoutInflater.from(ctx);
        View view = layoutInflater.inflate(R.layout.wish_list_row, parent, false);

        return new ClientWishListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
            holder.productName.setText(pNames.get(position));
            holder.productPrice.setText(pPrice.get(position) + " DA");
            Picasso.get().load(imgaeUrl.get(position)).into(holder.proImgView);
    }

    @Override
    public int getItemCount() {
        return imgaeUrl.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView productName , productPrice;

        private ImageView proImgView;

        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            linearLayout = itemView.findViewById(R.id.row);
            proImgView = itemView.findViewById(R.id.proImgView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seeProductDetailsInter.seeDetails(pKey.get(getAdapterPosition()));
                }
            });

        }
    }
}
