package com.android.delareez;

/**
 * Created by User on 10/8/2017.
 */



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.DA.delareez.MenuDA;
import com.model.delareez.Menu;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
            private ProgressBar progressBar;
            MenuDA Helper;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.item_ordered, container,false);


        progressBar = (ProgressBar) myView.findViewById(R.id.LoadingFood);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        Query FoodQuery = mFirebaseRef.child("Menu").orderByChild("menuType").equalTo("Food");
        Helper=new MenuDA(mFirebaseRef);


        mFoodList = (RecyclerView) myView.findViewById(R.id.food_list);
        manager = new LinearLayoutManager(this.getContext());
        mFoodList.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);


        //Initializes Recycler View and Layout Manager.
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Menu, ViewHolder>(Menu.class, R.layout.food_row, ViewHolder.class, FoodQuery) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Menu model, int position) {

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
                viewHolder.updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent updateFood = new Intent(getActivity(),MenuDetail.class);
                        updateFood.putExtra("FoodID", post_key);

                        startActivity(updateFood);
                    }
                });



                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                        // Setting Dialog Title
                        alertDialog.setTitle("Confirm Delete");

                        // Setting Dialog Message
                        alertDialog.setMessage("Are you sure you want delete this?");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_warning_black_24dp);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {

                                Helper.deleteMenu(post_key);

                                // Write your code here to invoke YES event
                                Snackbar.make(view, model.getMenuName() + " is deleted" , Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }
                });
                progressBar.setVisibility(View.GONE);
            }
        };

        mFoodList.setAdapter(firebaseRecyclerAdapter);
        mFoodList.setLayoutManager(manager);

        return myView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView menuPrice;
        public final TextView menuStatus;
        public final ImageView menuImage;
        public final Button updateButton;
        public final ImageButton deleteButton;




        public ViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.card_title);
            menuPrice = (TextView) itemView.findViewById(R.id.drink_price);
            menuStatus = (TextView) itemView.findViewById(R.id.Status);
            menuImage = (ImageView) itemView.findViewById(R.id.drink_image);
            updateButton = (Button) itemView.findViewById(R.id.action_button);
            deleteButton = (ImageButton) itemView.findViewById(R.id.imageButton2);
        }



    }

    
}
