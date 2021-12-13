package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private TextInputEditText rusername;
    private TextInputEditText remail;
    private TextInputEditText rpassword;
    FirebaseAuth fireAuth;
    String userID;
    FirebaseFirestore fstore;
    Button registerbtn, loginLink;
    boolean valid = true;
    ProgressBar progressBar;
    CheckBox isSeller,isBuyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        rusername = (TextInputEditText) findViewById(R.id.rusername);
        remail = (TextInputEditText) findViewById(R.id.remail);
        rpassword = (TextInputEditText) findViewById(R.id.rpassword);
        loginLink = findViewById(R.id.loginLink);
        getSupportActionBar().hide();
        isSeller = findViewById(R.id.checkBox1);
        isBuyer = findViewById(R.id.checkBox2);
        registerbtn = findViewById(R.id.registerbtn);
        progressBar = findViewById(R.id.progressBar2);
        fireAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
        isSeller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isBuyer.setChecked(false);
                }
            }
        });

        isBuyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isSeller.setChecked(false);
                }
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(isSeller.isChecked() || isBuyer.isChecked())){
                    Toast.makeText(Register.this, "Select the account type", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (valid){
                    String email = remail.getText().toString().trim();
                    String passowrd = rpassword.getText().toString().trim();

                    if(TextUtils.isEmpty(email)){
                        remail.setError("Email is required");
                        return;
                    }

                    if(TextUtils.isEmpty(passowrd)){
                        rpassword.setError("Password is required");
                        return;
                    }

                    if(rpassword.length()< 8){
                        rpassword.setError("Password must be 8 at least 8 characters");
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);

                fireAuth.createUserWithEmailAndPassword(email,passowrd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        FirebaseUser user = fireAuth.getCurrentUser();
                        Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        DocumentReference df = fstore.collection("Users").document(user.getUid());
                        Map<String,Object> userinfo = new HashMap<>();
                        userinfo.put("Username",rusername.getText().toString());
                        userinfo.put("Email",remail.getText().toString());
                        userinfo.put("Password",rpassword.getText().toString());
                        if (isSeller.isChecked()){
                            userinfo.put("isSeller","1");
                            startActivity(new Intent(getApplicationContext(),MyAccount.class));
                            finish();
                        }
                        if (isBuyer.isChecked()){
                            userinfo.put("isBuyer","1");
                            startActivity(new Intent(getApplicationContext(),Buyer.class));
                            finish();
                        }
                        df.set(userinfo);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                }
                        }
                    });
                }
            }
