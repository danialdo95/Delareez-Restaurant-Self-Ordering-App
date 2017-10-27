package com.android.delareez;

/**
 * Created by User on 10/8/2017.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.model.delareez.Menu;
import com.squareup.picasso.Picasso;


/**
 * Provides UI for the view with List.
 */
public class FoodContentFragment extends Fragment {

            private static final String TAG = "FoodContentFragment";
            private RecyclerView mFoodList;
            private DatabaseReference mFirebaseRef;
            private LinearLayoutManager manager;
            private FirebaseRecyclerAdapter<Menu, ViewHolder> firebaseRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.item_ordered, container,false);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        Query FoodQuery = mFirebaseRef.child("Menu").orderByChild("menuType").equalTo("Food");

        mFoodList = (RecyclerView) myView.findViewById(R.id.food_list);
        manager = new LinearLayoutManager(this.getContext());
        mFoodList.setHasFixedSize(true);



        //Initializes Recycler View and Layout Manager.

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Menu, ViewHolder>(Menu.class, R.layout.food_row, ViewHolder.class, FoodQuery) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Menu model, int position) {

                viewHolder.menuName.setText(model.getMenuName());
                viewHolder.menuPrice.setText("RM " +Double.toString(model.getMenuPrice()) + "0");
                Picasso.with(viewHolder.menuImage.getContext()).load(model.getMenuImage()).into(viewHolder.menuImage);
                viewHolder.updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent NFC = new Intent(getActivity(),CheckoutOrder.class);
                        startActivity(NFC);
                    }
                });

            }
        };

        mFoodList.setAdapter(firebaseRecyclerAdapter);
        mFoodList.setLayoutManager(manager);

        return myView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView menuPrice;
        public final ImageView menuImage;
        public final Button updateButton;




        public ViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.card_title);
            menuPrice = (TextView) itemView.findViewById(R.id.drink_price);
            menuImage = (ImageView) itemView.findViewById(R.id.drink_image);
            updateButton = (Button) itemView.findViewById(R.id.action_button);

        }



    }

    
}
