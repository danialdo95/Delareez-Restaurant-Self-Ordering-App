package com.android.delareez;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.DA.delareez.MenuDA;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.model.delareez.Menu;




public class CreateMenu extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;

    private EditText mMenuName;
    private EditText mMenuPrice;
    private Spinner mMenuType;
    private ImageButton mSelectImage;
    private ProgressDialog mProgress;
    private Uri mImageUri;

    private Button mAddMenu;
    private Button mCancelMenu;
    MenuDA Helper;



    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Helper=new MenuDA(mDatabase);

        mMenuName =(EditText) findViewById(R.id.MenuName);
        mMenuPrice = (EditText) findViewById(R.id.Price);
        mMenuType = (Spinner) findViewById(R.id.spinner3);
        mSelectImage =  (ImageButton) findViewById(R.id.imageButtonMenu);
        mAddMenu = (Button) findViewById(R.id.AddMenu);
        mCancelMenu = (Button) findViewById(R.id.CancelMenu);

        mProgress = new ProgressDialog(this);




        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddMenu();

            }


        });

        mCancelMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void AddMenu() {



        String name = mMenuName.getText().toString();
        String p = mMenuPrice.getText().toString();
        String type = mMenuType.getSelectedItem().toString();


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
                !TextUtils.isEmpty(type) && mImageUri !=null){
            mProgress.setMessage("Uploading New Menu...");
            mProgress.show();
            StorageReference filepath = mStorageRef.child("Menu_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Double price = Double.parseDouble(p);
                            //getting a unique id using push().getKey() method
                            //it will create a unique id and we will use it as the Primary Key for our Artist
                            String id = mDatabase.push().getKey();

                            //creating an Menu Object
                            Menu menu = new Menu(id, name, price, type, downloadUrl.toString());

                            //Saving the Menu-

                            //mDatabase.child(id).setValue(menu);
                            Helper.CreateMenu(menu);
                            //setting edittext to blank again


                            mProgress.dismiss();
                            finish();

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), "Failed to upload menu", Toast.LENGTH_SHORT).show();
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



}