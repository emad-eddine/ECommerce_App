package com.kichou.imad.e_commerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kichou.imad.e_commerceapp.utils.Prevlent;
import com.kichou.imad.e_commerceapp.utils.ProductRefImages;
import com.kichou.imad.e_commerceapp.view.ProductDetailsAdapter;
import com.kichou.imad.e_commerceapp.view.ReviewAdapters;
import com.kichou.imad.e_commerceapp.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyAProductActivity extends AppCompatActivity {

    String productKey ;

    private ImageView backBtn,wishlist;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Button addBtn;

    private TextView productName , productPrice , productDescreption , nomVendeur , addressVendeur , addReviewBtn;

    private DatabaseReference productRef , productRef2;
    private String sellerKey = "";
    private ArrayList<String> productImages = new ArrayList<>();

    // review recycle view

    private RecyclerView reviewRecycleView;
    private ReviewAdapters reviewAdapters;

    private DatabaseReference ReviewRef;
    private ArrayList<String> profilePictures = new ArrayList<>();
    private  ArrayList<String> reviewersName = new ArrayList<>();
    private  ArrayList<String> reviews = new ArrayList<>();
    private ArrayList<String> rates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_a_product);
        // back btn
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // viewpager
        viewPager = findViewById(R.id.produtcViewPager);

        // get product key

        productKey = getIntent().getStringExtra("pKey");
        //Toast.makeText(this,productKey,Toast.LENGTH_SHORT).show();

        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescreption = findViewById(R.id.articleDes);
        nomVendeur = findViewById(R.id.seller_Name);
        addressVendeur = findViewById(R.id.sellerAddress);

        // initialise data
        productRef  = FirebaseDatabase.getInstance().getReference().child("sellers");
        productRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else
                {

                    for(DataSnapshot val : task.getResult().getChildren())
                    {

                        sellerKey = val.getKey();
                        productRef2  = FirebaseDatabase.getInstance().getReference().child("sellers").child(val.getKey()).child("products");

                        productRef2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task)
                            {
                                if (!task.isSuccessful()) {
                                    Log.e("firebase", "Error getting data", task.getException());
                                }
                                else
                                {
                                    for(DataSnapshot val : task.getResult().getChildren())
                                    {
                                       // Log.d("TAG1", "onComplete: " +val.getKey() );
                                        if(val.getKey().equals(productKey))
                                        {

                                            // get seller details

                                            DatabaseReference sellerRefrence1  = FirebaseDatabase.getInstance().getReference().child("sellers").child(sellerKey).child("");
                                            sellerRefrence1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                    if (!task.isSuccessful()) {
                                                        Log.e("firebase", "Error getting data", task.getException());
                                                    }
                                                    else
                                                    {
                                                        String firstName , lastName;
                                                        firstName = String.valueOf(task.getResult().child("seller_first_name").getValue());
                                                        lastName = String.valueOf(task.getResult().child("seller_last_name").getValue());

                                                        firstName = lastName + " " + firstName;
                                                        nomVendeur.setText(firstName);

                                                    }
                                                }
                                            });


                                          //  Log.d("TAG1", "onComplete: " + );
                                            // add images
                                           // imagesUrl.add(val.child("Images").child("img0").getValue().toString());
                                            //  Log.d("TAG1", "onComplete: " + val.child("Images").child("img0").getValue().toString());


                                            for(DataSnapshot imgurl : val.child("Images").getChildren())
                                            {
                                                //Log.d("TAG1", "onComplete: " + imgurl.getValue().toString());
                                                productImages.add(imgurl.getValue().toString());

                                            }
                                           // productRefImages.setProductImagesUrl(imagesurlFromdb);
                                          //  imagesUrlOfProduct.add(productRefImages);

                                            // add product name
                                            productName.setText(val.child("pname").getValue().toString());
                                          //   Log.d("TAG1", "onComplete: " + val.child("pname").getValue().toString());
                                            // add price
                                            productPrice.setText(val.child("price").getValue().toString() + " DA");
                                            // add description
                                            productDescreption.setText(val.child("description").getValue().toString());
                                            //add adress
                                            addressVendeur.setText(val.child("pAddress").getValue().toString());
                                            viewPagerAdapter = new ViewPagerAdapter(getApplication(),productImages);

                                             viewPager.setAdapter(viewPagerAdapter);
                                        }

                                    }

                                }
                            }

                        });

                    }

                }
            }

        });



        // wish list function
        wishlist = findViewById(R.id.addtowishList);
        wishlist.setImageResource(R.drawable.wish_list_icon);
        wishlist.setTag(R.drawable.wish_list_icon);
        // see if product exist in wish list
        DatabaseReference wishRef = FirebaseDatabase.getInstance().getReference().child("client_wishList").child(Prevlent.currentClient);
        wishRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot val : snapshot.getChildren())
                {
                   // Log.d("TAG", "onDataChange: " + val.getKey());
                    int startIndex =  val.getValue().toString().indexOf("=");
                    String serchProductKey = val.getValue().toString().substring(++startIndex,val.getValue().toString().length()-1);
                   // Log.d("TAG", "onDataChange: " + serchProductKey);
                    if(serchProductKey.equals(productKey))
                    {
                        wishlist.setImageResource(R.drawable.wish_list_add);
                        wishlist.setTag(R.drawable.wish_list_add);
                        break;
                    }
                    else
                        {
                        wishlist.setImageResource(R.drawable.wish_list_icon);
                        wishlist.setTag(R.drawable.wish_list_icon);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer imgId = (Integer) wishlist.getTag();

                DatabaseReference wishListref = FirebaseDatabase.getInstance().getReference().child("client_wishList").child(Prevlent.currentClient);
                final int[] wishListCount = new int[1];
                // get number of product in wish list

                wishListref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        wishListCount[0] = (int) snapshot.getChildrenCount();
                        Log.d("TAG", "onDataChange: " + wishListCount[0]);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });

                if(imgId == R.drawable.wish_list_icon)
                {
                    // add produtc to wishlist and change icon
                    DatabaseReference insertRef = FirebaseDatabase.getInstance().getReference().child("client_wishList").child(Prevlent.currentClient);

                    wishlist.setImageResource(R.drawable.wish_list_add);
                    wishlist.setTag(R.drawable.wish_list_add);

                    // add product key to wish list

                    HashMap<String,String> dataToInsert = new HashMap<>();

                   // Log.d("TAG", "onClick: " + (++wishListCount[0]));
                    dataToInsert.put("productKey",productKey);

                    insertRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            snapshot.child("product"+(++wishListCount[0])).getRef().setValue(dataToInsert);
                            Toast.makeText(getApplicationContext(),"Product has been added to your wish list",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else
                {
                    // delete produtc from wishlist and change icon
                    wishlist.setImageResource(R.drawable.wish_list_icon);
                    wishlist.setTag(R.drawable.wish_list_icon);

                    DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference().child("client_wishList").child(Prevlent.currentClient);

                    deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            for(DataSnapshot val : snapshot.getChildren())
                            {
                                int startIndex =  val.getValue().toString().indexOf("=");
                                String serchProductKey = val.getValue().toString().substring(++startIndex,val.getValue().toString().length()-1);
                                if(serchProductKey.equals(productKey))
                                {
                                    val.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(),"Product has been removed from your wish list",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                                Toast.makeText(getApplicationContext(),"Some error happended try again later",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });


        // add review
        addReviewBtn = findViewById(R.id.pReviewBtn);
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddProductReview.class);
                intent.putExtra("pKey",productKey);
                intent.putExtra("pName",productName.getText().toString());
                startActivity(intent);
            }
        });



        // review recucle view

        reviewRecycleView  = findViewById(R.id.productReviewRecycle);
        reviewRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        View view = null;
        startAsynTask(view);

        // add product to cart
        addBtn = findViewById(R.id.addTocart);
        addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                HashMap<String,String> dataToInsert = new HashMap<>();
                dataToInsert.put("product_key",productKey);
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("client_cart").child(Prevlent.currentClient);

                cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        snapshot.child(productKey).getRef().setValue(dataToInsert).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(getBaseContext(),"product added to cart",Toast.LENGTH_SHORT).show();
                                                                    }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });


    }



    public void startAsynTask(View v)
    {
        ReviewDetailsAsyncTask reviewAsyncTask = new ReviewDetailsAsyncTask();
        ReviewClientDetailsAsyncTask reviewClientDetailsAsyncTask = new ReviewClientDetailsAsyncTask();
        reviewAsyncTask.execute();
        reviewClientDetailsAsyncTask.execute();
    }

    private class ReviewDetailsAsyncTask extends AsyncTask<Void,ArrayList<String>,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            // get the data

            ReviewRef = FirebaseDatabase.getInstance().getReference().child("poducts_reviews").child(productKey);

            ReviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {


                    for(DataSnapshot val : snapshot.getChildren())
                    {

                        String clientKey = val.getKey();

                        DatabaseReference reviewR = FirebaseDatabase.getInstance().getReference().child("poducts_reviews").child(productKey).child(clientKey);

                        reviewR.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {

                                String rate = snapshot.child("rate").getValue().toString();
                                String review = snapshot.child("review").getValue().toString();

                                Log.d("TAG", "onDataChange: rate " + rate + " review " + review );

                                rates.add(rate);
                                reviews.add(review);
                                publishProgress(reviews,rates);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }







//                reviewRecycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    // reviewAdapters = new ReviewAdapters(getApplicationContext(),profilePictures,reviewersName,reviews,rates);
                    //reviewRecycleView.setAdapter(reviewAdapters);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<String>... values)
        {
            super.onProgressUpdate(values);


            Log.d("TAG", "onDataChange: rate  " + values[0].get(0) + " " + values[0].get(0));

            rates = values[1];
            reviews = values[0];

            ReviewAdapters.setRates(values[1]);
            ReviewAdapters.setReviews(values[0]);


        }


    }


    private class ReviewClientDetailsAsyncTask extends AsyncTask<Void,ArrayList<String>,Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            // get the data

            ReviewRef = FirebaseDatabase.getInstance().getReference().child("poducts_reviews").child(productKey);

            ReviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {


                    for(DataSnapshot val : snapshot.getChildren())
                    {

                        String clientKey = val.getKey();


                        // get client details
                        DatabaseReference clientRef = FirebaseDatabase.getInstance().getReference().child("clients").child(val.getKey());
                        clientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                // Log.d("TAG", "onDataChange: " + snapshot.child("client_first_name").getValue());
                                String clientFirstName , clientLastName;

                                // get name
                                clientFirstName  = snapshot.child("client_first_name").getValue().toString();
                                clientLastName  = snapshot.child("client_last_name").getValue().toString();

                                clientFirstName = clientFirstName + " " + clientLastName;

                                Object link =  snapshot.child("profile_img").child("profile_img").getValue();
                                String profileimgLink;
                                if(link == null)
                                {
                                    profileimgLink = "https://firebasestorage.googleapis.com/v0/b/ecommerceapp-8434f.appspot.com/o/user.png?alt=media&token=83f9c610-e2be-49fe-8fd5-d50720b64277";
                                }
                                else
                                {
                                    profileimgLink = snapshot.child("profile_img").child("profile_img").getValue().toString();
                                }


                                //   Log.d("TAG", "onDataChange: name " + clientFirstName + " profileimgLink " + profileimgLink );

                                profilePictures.add(profileimgLink);
                                reviewersName.add(clientFirstName);
                                publishProgress(profilePictures,reviewersName);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });





                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<String>... values)
        {
            super.onProgressUpdate(values);


            profilePictures = values[0];
            ReviewAdapters.setProfilePictures(values[0]);
            reviewersName = values[1];
            ReviewAdapters.setReviewersName(values[1]);
            reviewAdapters = new ReviewAdapters(getApplicationContext());
            reviewRecycleView.setAdapter(reviewAdapters);


        }


    }

}