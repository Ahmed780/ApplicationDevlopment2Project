package com.example.finalproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MyAccount extends AppCompatActivity {

    TextView verifymsg, name,email;
    String uid;
    Button verify, changeProfile,map,post;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ImageView profileImage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        verify = findViewById(R.id.verify_btn);
        verifymsg = findViewById(R.id.verifymsg);
        name = findViewById(R.id.accountName);
        email = findViewById(R.id.accountEmail);
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        profileImage = findViewById(R.id.profile_image);
        changeProfile = findViewById(R.id.edit_btn);
        map = findViewById(R.id.map);
        uid = fAuth.getCurrentUser().getUid();
        post = findViewById(R.id.post_btn);
        storageReference = FirebaseStorage.getInstance().getReference().child("profile picture");


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choosePicture();
            }
        });


        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),EditProfile.class);
                intent.putExtra("Username",name.getText().toString());
                intent.putExtra("Email",email.getText().toString());
                startActivity(intent);

            }
        });

        DocumentReference df = fstore.collection("Users").document(uid);
        df.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                name.setText(value.getString("Username"));
                email.setText(value.getString("Email"));
            }
        });


        uid = fAuth.getCurrentUser().getUid();
        FirebaseUser user = fAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            verifymsg.setVisibility(View.VISIBLE);
            verify.setVisibility(View.VISIBLE);

            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MyAccount.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "Email not sent" + e.getMessage());
                        }
                    });

                }
            });

        }
        else{
            post.setVisibility(View.VISIBLE);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(MyAccount.this,PostAd.class);
                        startActivity(intent);
                    }
            });
        }

    }

     private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1000){
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadProfile(imageUri);
        }

    }

    private void uploadProfile(Uri imageUri) {


        StorageReference file = storageReference;
        file.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(MyAccount.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyAccount.this, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.categoryMenu:
                Intent intent2 = new Intent(MyAccount.this, Categories.class);
                this.startActivity(intent2);
                return true;
            case R.id.accountMenu:
                Intent intent3 = new Intent(MyAccount.this, MyAccount.class);
                this.startActivity(intent3);
                return true;
            case R.id.contactMenu:
                Intent intent4 = new Intent(MyAccount.this, ContactUs.class);
                this.startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void logout(View view){
        FirebaseAuth.getInstance();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}

