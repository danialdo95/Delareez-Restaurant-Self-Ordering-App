package com.android.delareez;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.model.delareez.Menu;
import com.squareup.picasso.Picasso;



public class OrderActivity extends AppCompatActivity {


    private static final String TAG = "OrderActivity";
    private TextView mMenuName;
    private TextView mMenuPrice;
    private FloatingActionButton btn;
    private ElegantNumberButton qtty;
    private TextView mMenuStatus, mMenuType;
    private ImageView mSelectImage;
    private Uri mImageUri;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private DatabaseReference mFirebaseRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        String post_key = getIntent().getExtras().getString("FoodID");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(post_key);

        mMenuName = (TextView) findViewById(R.id.food_name);
        mMenuPrice = (TextView) findViewById(R.id.food_price);
        mMenuStatus= (TextView) findViewById(R.id.status);
        mMenuType = (TextView) findViewById(R.id.Type);
        mSelectImage = (ImageView) findViewById(R.id.img_food);
        qtty = (ElegantNumberButton) findViewById(R.id.number_button);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);



       btn = (FloatingActionButton) findViewById(R.id.btnCart);



        // Read from the database

        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Menu menu = dataSnapshot.getValue(Menu.class);
                collapsingToolbarLayout.setTitle(menu.getMenuName());
                mMenuName.setText(menu.getMenuName());
                mMenuPrice.setText( menu.getMenuPrice().toString() + "0");
                mMenuType.setText(menu.getMenuType());
                mMenuStatus.setText(menu.getMenuStatus());
                mImageUri = Uri.parse(menu.getMenuImage());
                Picasso.with(getApplicationContext()).load(mImageUri).into(mSelectImage);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, menu.getMenuName() + " is added to cart", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








    }

}

