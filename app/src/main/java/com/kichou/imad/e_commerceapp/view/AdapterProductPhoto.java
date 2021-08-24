package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;
import com.kichou.imad.e_commerceapp.controller.sellers.RemoveSellerAddProductPhoto;

import java.util.ArrayList;

public class AdapterProductPhoto extends RecyclerView.Adapter<AdapterProductPhoto.MyViewHolder> {

    private Context ct;

    private ArrayList<Uri> imgLink = new ArrayList<>();

    private RemoveSellerAddProductPhoto removeSellerAddProductPhoto;

    public AdapterProductPhoto(Context ct , ArrayList<Uri> imgLink, RemoveSellerAddProductPhoto removeSellerAddProductPhoto)
    {
        this.ct = ct;
        this.imgLink= imgLink;
        this.removeSellerAddProductPhoto = removeSellerAddProductPhoto;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(ct);
        View view = layoutInflater.inflate(R.layout.product_photo_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageURI(imgLink.get(position));
    }

    @Override
    public int getItemCount() {
        return imgLink.size();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        Button delBtn;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.proImgView);
            delBtn = itemView.findViewById(R.id.Delete_PhotoBtn);

            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //removeSellerAddProductPhoto.onSellerPhotoProductRemove(pos);
                    imgLink.remove(pos);
                    notifyDataSetChanged();
                }
            });


        }
    }
}

