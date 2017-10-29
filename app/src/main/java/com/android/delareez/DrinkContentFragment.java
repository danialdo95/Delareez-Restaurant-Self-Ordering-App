package com.android.delareez;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;



import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.model.delareez.Menu;
import com.squareup.picasso.Picasso;

/**
 * Provides UI for the view with Cards.
 */




public class DrinkContentFragment extends Fragment {
    private static final String TAG = "DrinkContentFragment";
    private RecyclerView mDrinkList;
    private DatabaseReference mFirebaseRef;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Menu, ViewHolder> firebaseRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.item_drink, container, false);


        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        Query DrinkQuery = mFirebaseRef.child("Menu").orderByChild("menuType").equalTo("Drink");


        mDrinkList = (RecyclerView) myView.findViewById(R.id.drink_list);
        manager = new LinearLayoutManager(this.getContext());
        mDrinkList.setHasFixedSize(true);


        //Initializes Recycler View and Layout Manager.

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Menu, DrinkContentFragment.ViewHolder>(Menu.class, R.layout.drink_row, DrinkContentFragment.ViewHolder.class, DrinkQuery) {
            @Override
            protected void populateViewHolder(DrinkContentFragment.ViewHolder viewHolder, Menu model, int position) {

                viewHolder.menuName.setText(model.getMenuName());
                viewHolder.menuPrice.setText("RM " + Double.toString(model.getMenuPrice()) + "0");
                Picasso.with(viewHolder.menuImage.getContext()).load(model.getMenuImage()).into(viewHolder.menuImage);

            }
        };

        mDrinkList.setAdapter(firebaseRecyclerAdapter);
        mDrinkList.setLayoutManager(manager);

        return myView;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView menuPrice;
        public final ImageView menuImage;



        public ViewHolder(View itemView) {
            super(itemView);

            menuName = (TextView) itemView.findViewById(R.id.drink_name);
            menuPrice = (TextView) itemView.findViewById(R.id.drink_price);
            menuImage = (ImageView) itemView.findViewById(R.id.drink_image);



        }




    }

}
