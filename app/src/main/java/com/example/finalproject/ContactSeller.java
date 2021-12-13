package com.example.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class ContactSeller extends AppCompatActivity {

    ImageView imageView,adImage;
    private TextView contactPrice,contactTitle,contactDescription;
    Button email;
    String uid;
    public static String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_seller);
        imageView = findViewById(R.id.sellBack);
        contactTitle = findViewById(R.id.contactTitle2);
        contactPrice = findViewById(R.id.contactPrice2);
        contactDescription = findViewById(R.id.contactDescription2);
        fAuth = FirebaseAuth.getInstance();
        adImage  = findViewById(R.id.adImage);
        uid = fAuth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        email = findViewById(R.id.email_btn);

        Intent data = getIntent();
        String title = data.getStringExtra("title");
        String price = data.getStringExtra("price");
        String description = data.getStringExtra("description");
        String imageUrl = data.getStringExtra("imageUri");

        contactTitle.setText(title);
        contactPrice.setText(price);
        contactDescription.setText(description);
        Picasso.get().load(imageUrl).into(adImage);

        Log.d(TAG,"onCreate" + contactTitle  + " " + " " + contactPrice+ " " + contactDescription + " " + adImage + "");


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactSeller.this, Categories.class);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContactSeller.this,Email.class);
                startActivity(intent);
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
                Intent intent2 = new Intent(ContactSeller.this, Categories.class);
                this.startActivity(intent2);
                return true;
            case R.id.accountMenu:
                Intent intent3 = new Intent(ContactSeller.this, MyAccount.class);
                this.startActivity(intent3);
                return true;
            case R.id.contactMenu:
                Intent intent4 = new Intent(ContactSeller.this, ContactUs.class);
                this.startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
