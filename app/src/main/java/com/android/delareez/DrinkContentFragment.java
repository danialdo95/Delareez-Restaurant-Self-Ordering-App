package com.android.delareez;




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
import android.widget.Toast;


import com.DA.delareez.MenuDA;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Menu;
import com.model.delareez.Staff;
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
    private ProgressBar progressBar;
    MenuDA Helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.item_drink, container, false);


        progressBar = (ProgressBar) myView.findViewById(R.id.LoadingDrink);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        Query DrinkQuery = mFirebaseRef.child("Menu").orderByChild("menuType").equalTo("Drink");
        Helper=new MenuDA(mFirebaseRef);

        mDrinkList = (RecyclerView) myView.findViewById(R.id.drink_list);
        manager = new LinearLayoutManager(this.getContext());
        mDrinkList.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);

        //Initializes Recycler View and Layout Manager.


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Menu, DrinkContentFragment.ViewHolder>(Menu.class, R.layout.drink_row, DrinkContentFragment.ViewHolder.class, DrinkQuery) {
            @Override
            protected void populateViewHolder(DrinkContentFragment.ViewHolder viewHolder, Menu model, int position) {

                final String post_key = getRef(position).getKey();
                viewHolder.menuName.setText(model.getMenuName());
                viewHolder.menuPrice.setText("RM " + Double.toString(model.getMenuPrice()) + "0");
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

                        mFirebaseRef.child("Staff").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Staff staff = dataSnapshot.getValue(Staff.class);
                                if (staff.getStaffType().equals("Manager") || staff.getStaffType().equals("Chef")) {

                                    Intent updateFood = new Intent(getActivity(),MenuDetail.class);
                                    updateFood.putExtra("FoodID", post_key);

                                    startActivity(updateFood);
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Only Manager and Chef can edit Menu", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });



                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mFirebaseRef.child("Staff").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Staff staff = dataSnapshot.getValue(Staff.class);
                                if (staff.getStaffType().equals("Manager") ) {
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

                                            Helper.deleteMenu(getRef(position).getKey());

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
                                else{
                                    Toast.makeText(getContext(), "Only the Manager can delete Menu", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                });
                progressBar.setVisibility(View.GONE);
            }
        };

        mDrinkList.setAdapter(firebaseRecyclerAdapter);
        mDrinkList.setLayoutManager(manager);

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

            menuName = (TextView) itemView.findViewById(R.id.drink_name);
            menuPrice = (TextView) itemView.findViewById(R.id.drink_price);
            menuStatus = (TextView) itemView.findViewById(R.id.Status);
            menuImage = (ImageView) itemView.findViewById(R.id.drink_image);
            updateButton = (Button) itemView.findViewById(R.id.order_button);
            deleteButton = (ImageButton) itemView.findViewById(R.id.imageButton2);



        }




    }

}
