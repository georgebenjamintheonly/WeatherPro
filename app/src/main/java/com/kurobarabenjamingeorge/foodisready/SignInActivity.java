package com.kurobarabenjamingeorge.foodisready;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private FirebaseAuth signInAuth;
    private DatabaseReference firebaseDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userEmail = (EditText) findViewById(R.id.userEmail);
        userPassword = (EditText) findViewById(R.id.userPassword);

        signInAuth = FirebaseAuth.getInstance();
        firebaseDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void userSignIn(View view){

        String userEmailText = userEmail.getText().toString().trim();
        String userPasswordText = userPassword.getText().toString().trim();

        if(!userEmailText.isEmpty() && !userPasswordText.isEmpty()){

            final ProgressDialog signInProgressDialog = new ProgressDialog(SignInActivity.this);
            signInProgressDialog.setTitle("Signing in");
            signInProgressDialog.setMessage("Please wait...");
            signInProgressDialog.show();

            signInAuth.signInWithEmailAndPassword(userEmailText, userPasswordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    signInProgressDialog.dismiss();
                    if(task.isSuccessful()){
                        checkIfUserExists();
                    }else{
                        Toast.makeText(SignInActivity.this, "Check your login details.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_LONG).show();
        }

    }

    public void checkIfUserExists(){
        final String user_id = signInAuth.getCurrentUser().getUid();
        firebaseDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){
                    startActivity(new Intent(SignInActivity.this, MenuActivity.class));
                }else{
                    Toast.makeText(SignInActivity.this, "Wrong log in detils.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
