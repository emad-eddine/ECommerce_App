package com.kichou.imad.e_commerceapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kichou.imad.e_commerceapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapters extends RecyclerView.Adapter<ReviewAdapters.MyViewHolder> {


    private Context ctx;
    public static   ArrayList<String> profilePictures = new ArrayList<>();
    public static   ArrayList<String> reviewersName = new ArrayList<>();
    public static   ArrayList<String> reviews = new ArrayList<>();
    public static   ArrayList<String> rates = new ArrayList<>();

    public ReviewAdapters(Context ctx)
    {
        this.ctx = ctx;
    }


    public static ArrayList<String> getProfilePictures() {
        return profilePictures;
    }

    public static void setProfilePictures(ArrayList<String> profilePictures) {
        ReviewAdapters.profilePictures = profilePictures;
    }

    public static ArrayList<String> getReviewersName() {
        return reviewersName;
    }

    public static void setReviewersName(ArrayList<String> reviewersName) {
        ReviewAdapters.reviewersName = reviewersName;
    }

    public static ArrayList<String> getReviews() {
        return reviews;
    }

    public static void setReviews(ArrayList<String> reviews) {
        ReviewAdapters.reviews = reviews;
    }

    public static ArrayList<String> getRates() {
        return rates;
    }

    public static void setRates(ArrayList<String> rates) {
        ReviewAdapters.rates = rates;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View view  = layoutInflater.inflate(R.layout.review_row,parent,false);
        return new ReviewAdapters.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.clientName.setText(reviewersName.get(position));
       // holder.ratingBar.setRating(Float.parseFloat(rates.get(position)));
        if(Float.parseFloat(rates.get(position)) == 1)
        {
            holder.ratingBar.setImageResource(R.drawable.one_star);
        }
        else if(Float.parseFloat(rates.get(position)) == 2)
        {
            holder.ratingBar.setImageResource(R.drawable.two_star);
        }
        else if(Float.parseFloat(rates.get(position)) == 3)
        {
            holder.ratingBar.setImageResource(R.drawable.three_star);
        }
        else if(Float.parseFloat(rates.get(position)) == 4)
        {
            holder.ratingBar.setImageResource(R.drawable.four_star);
        }
        else if(Float.parseFloat(rates.get(position)) == 5)
        {
            holder.ratingBar.setImageResource(R.drawable.five_star);
        }
        holder.review.setText(reviews.get(position));
       Picasso.get().load(profilePictures.get(position)).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return profilePictures.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        private TextView clientName;
        private ImageView ratingBar;
        private TextView review;
        private CircleImageView profilePic;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            clientName = itemView.findViewById(R.id.clientName);
            ratingBar = itemView.findViewById(R.id.clientRatingBar);
            review = itemView.findViewById(R.id.reviewText);
            profilePic = itemView.findViewById(R.id.client_profile_image);




        }
    }
}
