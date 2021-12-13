package com.example.finalproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText profileName,profileEmail;
    FirebaseFirestore firestore;
    FirebaseAuth fAuth;
    Button save_button;
    ImageView back;
    public static String TAG = "TAG";
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        Intent data = getIntent();
        String name = data.getStringExtra("Username");
        String email = data.getStringExtra("Email");
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        profileName = findViewById(R.id.EditName);
        profileEmail = findViewById(R.id.EditEmail);
        save_button = findViewById(R.id.save_btn);
        back = findViewById(R.id.editBack);
        profileName.setText(name);
        profileEmail.setText(email);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Log.d(TAG,"onCreate" + name  + " " + " "+ email);



        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty()){

                    Toast.makeText(EditProfile.this, "One or many fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference documentReference = firestore.collection("Users").document(user.getUid());
                        Map<String,Object> edit = new HashMap<>();
                        edit.put("Email",email);
                        edit.put("Username",profileName.getText().toString());
                        documentReference.update(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MyAccount.class));
                                finish();
                            }
                        });

                        Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
