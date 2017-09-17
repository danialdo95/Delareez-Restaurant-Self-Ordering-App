package com.android.delareez;

/**
 * Created by User on 10/8/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * Provides UI for the view with Cards.
 */
public class OrderContentFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.item_checkout, container,false);


        Button button = (Button) myView.findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent NFC = new Intent(getActivity(),CheckoutOrder.class);
                startActivity(NFC);
            }
        });


        return myView;



    }




}

