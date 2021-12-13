package com.example.finalproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class Category3 extends AppCompatActivity {

    private String description, price, title, saveCurrentDate, saveCurrentTime;
    private Uri Imageuri;
    private String productRandomKey, DownloadImageUri;
    Button submitbtn;
    EditText Inputtitle, Inputprice, Inputdescription;
    ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    private StorageTask task;
    private ImageView itemPic;
    PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_category3);
        submitbtn = findViewById(R.id.submit_btn3);
        Inputtitle = findViewById(R.id.contactTitle3);
        Inputprice = findViewById(R.id.contactPrice3);
        Inputdescription = findViewById(R.id.contactDescription3);
        progressBar = findViewById(R.id.progressBar3);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Products3");
        storageReference = FirebaseStorage.getInstance().getReference().child("Ad images3");
        imageView = findViewById(R.id.selectBack3);
        itemPic = findViewById(R.id.itemPic3);


         String apiKey = getString(R.string.map_key);
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), apiKey);
            }
            // Create a new Places client instance.
            placesClient = Places.createClient(this);
            AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                    getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment3);
            autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(@NonNull Status status) {

                    Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        itemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task != null && task.isInProgress()) {
                    Toast.makeText(Category3.this, "upload in progress", Toast.LENGTH_SHORT).show();
                }
                ValidateData();
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Imageuri = data.getData();
            itemPic.setImageURI(Imageuri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }

    private void ValidateData() {

        title = Inputtitle.getText().toString();
        price  = Inputprice.getText().toString();
        description = Inputdescription.getText().toString();

        if (TextUtils.isEmpty(title)) {
            Inputtitle.setError("Please enter a title");
            return;
        }

        if (TextUtils.isEmpty(price)) {
            Inputprice.setError("Please enter a price");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            Inputdescription.setError("Please enter a Description");
            return;
        }

        if (Imageuri == null) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }

        else {
            uploadData();
        }
    }

    private void uploadData() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;
    StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(Imageuri));
        final UploadTask uploadTask = fileReference.putFile(Imageuri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {

                            throw task.getException();
                        }
                        DownloadImageUri = fileReference.getDownloadUrl().toString();
                        return fileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            DownloadImageUri = task.getResult().toString();
                            Toast.makeText(Category3.this, "got the ad image Url Successfully...", Toast.LENGTH_SHORT).show();
                            postAd();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Category3.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress((int) progress);
            }
        });
    }
    private void postAd() {
        AdModel adModel = new AdModel(Inputtitle.getText().toString(),Inputprice.getText().toString(), Inputdescription.getText().toString(),DownloadImageUri, productRandomKey);
        String uploadId = databaseReference.push().getKey();
        databaseReference.child(uploadId).setValue(adModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Category3.this, "Ad posted in Clothing!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Category3.this, Categories.class));
                finish();
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
                Intent intent2 = new Intent(Category3.this, Categories.class);
                this.startActivity(intent2);
                return true;
            case R.id.accountMenu:
                Intent intent3 = new Intent(Category3.this, MyAccount.class);
                this.startActivity(intent3);
                return true;
            case R.id.contactMenu:
                Intent intent4 = new Intent(Category3.this, ContactUs.class);
                this.startActivity(intent4);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
