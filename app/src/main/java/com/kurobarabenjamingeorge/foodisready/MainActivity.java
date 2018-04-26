package com.kurobarabenjamingeorge.foodisready;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText email, password;
    //Creates a database reference
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private ImageView slidingBg;
    private LinearLayout userSigninLayout;

    Animation downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        slidingBg = findViewById(R.id.slidingBg);
        userSigninLayout = findViewById(R.id.userSigninLayout);
        downToUp = AnimationUtils.loadAnimation(this, R.anim.down_to_up);

        userSigninLayout.setAnimation(downToUp);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        firebaseAuth = FirebaseAuth.getInstance();


    }

    public void signUp(View view){
        final String email_text = email.getText().toString().trim();
        final String password_text = password.getText().toString().trim();

        if(!email_text.isEmpty() && !password_text.isEmpty()){

            if (password_text.length() < 4){

                Toast.makeText(MainActivity.this, "Password must be at least four characters", Toast.LENGTH_LONG).show();
            }else{
                final ProgressDialog userSignUpProgressDialog = new ProgressDialog(this);
                userSignUpProgressDialog.setTitle("Signing up");
                userSignUpProgressDialog.setMessage("Please wait...");
                userSignUpProgressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email_text, password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            userSignUpProgressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user = mDatabase.child(userId);
                            current_user.child("Name").setValue(email_text);
                            current_user.child("Password").setValue(password_text);

                            Intent loginIntent = new Intent(MainActivity.this, SignInActivity.class);
                            startActivity(loginIntent);
                        }
                    }
                });
            }


            
        }else{
            Toast.makeText(this, "Fields cant be empty", Toast.LENGTH_LONG).show();
        }
    }

    public void goToSignInPage(View view){

        startActivity(new Intent(MainActivity.this, SignInActivity.class));

    }
}
