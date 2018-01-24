package com.android.delareez;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.DA.delareez.MenuDA;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.model.delareez.Menu;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;


public class MenuDetail extends AppCompatActivity {


    private static final String TAG = "MenuDetail";
    private static final int GALLERY_REQUEST = 1;
    private EditText mMenuName;
    private EditText mMenuPrice;
    private Spinner mMenuType, mMenuStatus;
    private ImageButton mSelectImage;
    private ProgressDialog mProgress;
    private Button mUpdateMenu;
    private Uri mImageUri;
    private DatabaseReference mFirebaseRef;
    private StorageReference mStorageRef;
    MenuDA Helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_detail);
        String post_key = getIntent().getExtras().getString("FoodID");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(post_key);

        Helper=new MenuDA(mFirebaseRef);

        mMenuName =(EditText) findViewById(R.id.MenuName);
        mMenuPrice = (EditText) findViewById(R.id.Price);
        mMenuType = (Spinner) findViewById(R.id.spinner3);
        mMenuStatus = (Spinner) findViewById(R.id.spinner);
        mSelectImage =  (ImageButton) findViewById(R.id.imageButtonMenu);
        mMenuStatus = (Spinner) findViewById(R.id.spinner);
        mUpdateMenu = (Button) findViewById(R.id.UpdateMenu);

        mProgress = new ProgressDialog(this);



        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        // Read from the database

        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Menu menu = dataSnapshot.getValue(Menu.class);
                mMenuName.setText(menu.getMenuName());
                mMenuPrice.setText(menu.getMenuPrice().toString());
                mImageUri = Uri.parse(menu.getMenuImage());
                Picasso.with(getApplicationContext()).load(mImageUri).into(mSelectImage);
                mMenuStatus.setSelection(1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mUpdateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditMenu(post_key);

            }


        });




    }

    private void EditMenu(String Key) {



        String name = mMenuName.getText().toString();
        String p = mMenuPrice.getText().toString();
        String type = mMenuType.getSelectedItem().toString();
        String status = mMenuStatus.getSelectedItem().toString();



        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter the menu name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(p)) {
            Toast.makeText(getApplicationContext(), "Please Enter the menu price!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mImageUri == null) {
            Toast.makeText(getApplicationContext(), "Please Enter the menu image!", Toast.LENGTH_SHORT).show();
            return;
        }






        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(p) &&
                !TextUtils.isEmpty(type) && !TextUtils.isEmpty(status)  && mImageUri !=null){
            mProgress.setMessage("Updating Menu...");
            mProgress.show();

            Double price = Double.parseDouble(p);
            Menu menu = new Menu(Key, name, price, type, mImageUri.toString(), status);

            Helper.updateMenu(menu);
            Toast.makeText(getApplicationContext(), "Menu Details is updated", Toast.LENGTH_SHORT).show();

            StorageReference filepath = mStorageRef.child("Menu_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            menu.setMenuImage(downloadUrl.toString());
                            Toast.makeText(getApplicationContext(), "New Menu Image is Uploaded", Toast.LENGTH_SHORT).show();
                            Helper.updateMenuImage(menu);
                            mProgress.dismiss();
                            finish();

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "No new menu Image is uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });


        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home)
            finish();
        return  super.onOptionsItemSelected(item);
    }
}
