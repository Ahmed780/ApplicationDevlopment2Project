package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PostAd extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    Button btn;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_ad);
        Spinner spinner2 = findViewById(R.id.spinner2);
        imageView = findViewById(R.id.postBack);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter2 = ArrayAdapter.createFromResource(this, R.array.Categories,android.R.layout.simple_spinner_item);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(this);
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
                Intent intent2 = new Intent(PostAd.this, Categories.class);
                this.startActivity(intent2);
                return true;
            case R.id.accountMenu:
                Intent intent3 = new Intent(PostAd.this, MyAccount.class);
                this.startActivity(intent3);
                return true;
            case R.id.contactMenu:
                Intent intent4 = new Intent(PostAd.this, ContactUs.class);
                this.startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();

        if (adapterView.getItemAtPosition(i).equals("Education")) {
            Intent intent = new Intent(PostAd.this, Category.class);
            startActivity(intent);
        }

        if (adapterView.getItemAtPosition(i).equals("Electronics")) {
            Intent intent2 = new Intent(PostAd.this, Category2.class);
            startActivity(intent2);
        }

        if (adapterView.getItemAtPosition(i).equals("Clothing")) {
            Intent intent3 = new Intent(PostAd.this, Category3.class);
            startActivity(intent3);
        }

        if (adapterView.getItemAtPosition(i).equals("Cars and Vehicles")) {
                Intent intent4 = new Intent(PostAd.this, Category4.class);
                startActivity(intent4);
            }

        if (adapterView.getItemAtPosition(i).equals("Toys")) {
            Intent intent5 = new Intent(PostAd.this, Category5.class);
            startActivity(intent5);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    }
