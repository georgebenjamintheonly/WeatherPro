package com.kurobarabenjamingeorge.foodisready;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView foodList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    //Firebase Auth listener checks to see if a user is logged in
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        foodList = (RecyclerView) findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        foodList.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Items");
        firebaseAuth = FirebaseAuth.getInstance();
        //Creating a Firebase auth listener
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Check if the user logged in
                if(firebaseAuth.getCurrentUser() == null){
                    //If te user is not logged in, send him back to the sign up activity
                    Intent logInIntent = new Intent(MenuActivity.this, MainActivity.class);
                    //Disables the user from returning to the previous screen (MenuActivity) when the back button is pressed
                    logInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logInIntent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authListener);

        FirebaseRecyclerAdapter<Food, FoodViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class, R.layout.single_men_item, FoodViewHolder.class, databaseReference
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImageUri());

            }
        };

        foodList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{
        View myView;
        public FoodViewHolder(View itemView) {
            super(itemView);

            myView = itemView;
        }

        public void setName(String name){
            TextView foodNameTextView = myView.findViewById(R.id.foodName);
            foodNameTextView.setText(name);
        }

        public void setDesc(String desc){
            TextView foodDescTextView = myView.findViewById(R.id.foodDescription);
            foodDescTextView.setText(desc);
        }

        public void setPrice(String price){
            TextView foodPriceTextView = myView.findViewById(R.id.foodPrice);
            foodPriceTextView.setText(price);
        }

        public void setImage(Context ctx, String imageUri){
            ImageView foodImageView = myView.findViewById(R.id.foodImage);
            Picasso.with(ctx).load(imageUri).into(foodImageView);
        }
    }
}
