package com.android.delareez;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.model.delareez.Menu;
import com.squareup.picasso.Picasso;

public class DrinkContent extends Fragment {


    private static final String TAG = "DrinkContent";
    private RecyclerView mFoodList;
    private DatabaseReference mFirebaseRef;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Menu, DrinkContent.ViewHolder> firebaseRecyclerAdapter;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_drink_content, container,false);

        progressBar = (ProgressBar) myView.findViewById(R.id.LoadingFood);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        Query DrinkQuery = mFirebaseRef.child("Menu").orderByChild("menuType").equalTo("Drink");


        mFoodList = (RecyclerView) myView.findViewById(R.id.food_list2);
        manager = new LinearLayoutManager(this.getContext());
        mFoodList.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);


        //Initializes Recycler View and Layout Manager.
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Menu, DrinkContent.ViewHolder>(Menu.class, R.layout.food_card, DrinkContent.ViewHolder.class, DrinkQuery) {
            @Override
            protected void populateViewHolder(DrinkContent.ViewHolder viewHolder, Menu model, int position) {

                final String post_key = getRef(position).getKey();
                viewHolder.menuName.setText(model.getMenuName());
                viewHolder.menuPrice.setText("RM " +Double.toString(model.getMenuPrice()) + "0");
                viewHolder.menuStatus.setText(model.getMenuStatus());
                if (model.getMenuStatus().equals("Available")){
                    viewHolder.menuStatus.setTextColor(Color.GREEN);
                }
                else if (model.getMenuStatus().equals("Out Of Order")){
                    viewHolder.menuStatus.setTextColor(Color.RED);
                }
                Picasso.with(viewHolder.menuImage.getContext()).load(model.getMenuImage()).into(viewHolder.menuImage);
                viewHolder.OrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent orderFood = new Intent(getActivity(),OrderActivity.class);
                        orderFood.putExtra("FoodID", post_key);

                        startActivity(orderFood);
                    }
                });

                progressBar.setVisibility(View.GONE);
            }
        };

        mFoodList.setAdapter(firebaseRecyclerAdapter);
        mFoodList.setLayoutManager(manager);



        return  myView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView menuPrice;
        public final TextView menuStatus;
        public final ImageView menuImage;
        public final Button OrderButton;





        public ViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.card_title);
            menuPrice = (TextView) itemView.findViewById(R.id.drink_price);
            menuStatus = (TextView) itemView.findViewById(R.id.Status);
            menuImage = (ImageView) itemView.findViewById(R.id.drink_image);
            OrderButton = (Button) itemView.findViewById(R.id.Order);

        }



    }
}
