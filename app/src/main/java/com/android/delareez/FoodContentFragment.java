package com.android.delareez;

/**
 * Created by User on 10/8/2017.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




/**
 * Provides UI for the view with List.
 */
public class FoodContentFragment extends Fragment {

            private static final String TAG = "FoodContentFragment";
            private EditText mValueField;
            private Button mAddButton;

                // Write a message to the database

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.item_ordered, container,false);



        return myView;



    }
}
