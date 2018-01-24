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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.DA.delareez.OrderDA;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Order;
import com.model.delareez.Staff;


/**
 * Provides UI for the view with Cards.
 */


public class OrderContentFragment extends Fragment {

    private static final String TAG = "OrderContentFragment";
    private RecyclerView mOrderList;
    private DatabaseReference mFirebaseRef;
    private DatabaseReference mDatabaseMenu;
    private DatabaseReference mDatabaseCust;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Order, ViewHolder> firebaseRecyclerAdapter;
    private ProgressBar progressBar;
    OrderDA Helper;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.item_checkout, container,false);


        progressBar = (ProgressBar) myView.findViewById(R.id.LoadingOrder);

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference();
        mDatabaseCust = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query OrderQuery = mFirebaseRef.child("Order").orderByChild("cart").equalTo("placed");
        Helper=new OrderDA(mFirebaseRef);


        mOrderList = (RecyclerView) myView.findViewById(R.id.order_list);
        manager = new LinearLayoutManager(this.getContext());
        mOrderList.setHasFixedSize(true);



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, ViewHolder >(Order.class, R.layout.order_customer_card, ViewHolder.class, OrderQuery){

            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Order model, int position) {
                final String post_key = getRef(position).getKey();
                String menuid = model.getMenuID();
                String custID = model.getCustID();

                mDatabaseMenu.child("Menu").child(menuid).child("menuName").addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String menuname = dataSnapshot.getValue(String.class);
                        viewHolder.menuName.setText(menuname);
                    }

                    public void onCancelled(DatabaseError firebaseError) { }
                });

                mDatabaseCust.child("Customer").child(custID).child("custEmail").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String Cust = dataSnapshot.getValue(String.class);
                        viewHolder.email.setText(Cust);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.mQuantity.setText(Integer.toString(model.getNumberOfMenu()));
                viewHolder.Orderstatus.setText(model.getOrderStatus());
                if (model.getOrderStatus().equals("Not Started")){
                    viewHolder.Orderstatus.setTextColor(Color.RED);
                }
                else if (model.getOrderStatus().equals("Preparing")){
                    viewHolder.Orderstatus.setTextColor(Color.BLUE);
                }
                else if (model.getOrderStatus().equals("Ready")){
                    viewHolder.Orderstatus.setTextColor(Color.GREEN);
                }
                viewHolder.Date.setText(model.getOrderDate());
                viewHolder.orderOption.setText(model.getOrderOption());
                viewHolder.tablenum.setText(model.getTablenum());

                viewHolder.status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFirebaseRef.child("Staff").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Staff staff = dataSnapshot.getValue(Staff.class);
                                if(staff.getStaffType().equals("Manager") || staff.getStaffType().equals("Chef")) {
                                    Intent updateOrder = new Intent(getActivity(),UpdateOrderStatus.class);
                                    updateOrder.putExtra("OrderID", post_key);
                                    startActivity(updateOrder);
                                    Toast.makeText(getContext(), "Change Status", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getContext(), "Only the Manager and Chef can change Order status", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFirebaseRef.child("Staff").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Staff staff = dataSnapshot.getValue(Staff.class);
                                if(staff.getStaffType().equals("Manager") || staff.getStaffType().equals("Chef")){
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                                    // Setting Dialog Title
                                    alertDialog.setTitle("Confirm Delete");

                                    // Setting Dialog Message
                                    alertDialog.setMessage("Are you sure you want delete this Order?");

                                    // Setting Icon to Dialog
                                    alertDialog.setIcon(R.mipmap.ic_warning_black_24dp);

                                    // Setting Positive "Yes" Button
                                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int which) {

                                            Helper.deleteOrder(post_key);

                                            // Write your code here to invoke YES event
                                            Snackbar.make(v, "Order is deleted" , Snackbar.LENGTH_LONG)
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
                                    Toast.makeText(getContext(), "Only the Manager and Chef can delete Order", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });


            }
        };

        mOrderList.setAdapter(firebaseRecyclerAdapter);
        mOrderList.setLayoutManager(manager);



        return myView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView mQuantity;
        public final TextView tablenum;
        public final TextView Date;
        public final TextView Orderstatus;
        public final TextView email;
        public final TextView orderOption;
        public final Button status;
        public final ImageButton delete;




        public ViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.card_title);
            mQuantity = (TextView) itemView.findViewById(R.id.drink_price);
            tablenum = (TextView) itemView.findViewById(R.id.textView9);
            Date = (TextView) itemView.findViewById(R.id.textView8);
            Orderstatus = (TextView) itemView.findViewById(R.id.textView5);
            email = (TextView) itemView.findViewById(R.id.textView4);
            orderOption = (TextView) itemView.findViewById(R.id.textView15);
            status = (Button) itemView.findViewById(R.id.statuschange);
            delete = (ImageButton) itemView.findViewById(R.id.deleteOrder);

        }

    }
}

