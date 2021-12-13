package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    TextInputEditText lEmail;
    TextInputEditText lPassword;
    FirebaseAuth fireAuth;
    FirebaseFirestore fstore;
    Button loginbtn,registerlink, forgotLink;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginbtn = findViewById(R.id.loginbtn);

        lEmail = (TextInputEditText) findViewById(R.id.username);
        lPassword = (TextInputEditText) findViewById(R.id.password);
        registerlink = findViewById(R.id.registerLink);
        progressBar = findViewById(R.id.progressBar);
        forgotLink = findViewById(R.id.forgotPassword);
        getSupportActionBar().hide();
        fireAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = lEmail.getText().toString().trim();
                String passowrd = lPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    lEmail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(passowrd)){
                    lPassword.setError("Password is required");
                    return;
                }

                if(lPassword.length()< 8){
                    lPassword.setError("Password must be 8 at least 8 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                fireAuth.signInWithEmailAndPassword(email,passowrd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        checkUser(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);

            }
        });


        forgotLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText resetEmail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email to receive reset link");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = resetEmail.getText().toString();
                        fireAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                        Toast.makeText(MainActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                passwordResetDialog.create().show();

            }
        });

    }

    private void checkUser(String uid) {
        DocumentReference df = fstore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                if(documentSnapshot.getString("isSeller") !=null){
                    startActivity(new Intent(getApplicationContext(), MyAccount.class));
                    finish();
                }
                if(documentSnapshot.getString("isBuyer") !=null) {
                    startActivity(new Intent(getApplicationContext(),Buyer.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}