package com.android.delareez;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
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

public class orderArchive extends AppCompatActivity {

    private static final String TAG = "orderArchived";
    private RecyclerView mOrderList;
    private DatabaseReference mFirebaseRef;
    private DatabaseReference mDatabaseMenu;
    private DatabaseReference mDatabaseCust;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Order, orderArchive.ViewHolder> firebaseRecyclerAdapter;

    OrderDA Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference();
        mDatabaseCust = FirebaseDatabase.getInstance().getReference();


        Query OrderQuery = mFirebaseRef.child("OrderArchive");
        Helper=new OrderDA(mFirebaseRef);


        mOrderList = (RecyclerView) findViewById(R.id.order_list);
        manager = new LinearLayoutManager(this);
        mOrderList.setHasFixedSize(true);



        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, orderArchive.ViewHolder>(Order.class, R.layout.archived_card, orderArchive.ViewHolder.class, OrderQuery){

            @Override
            protected void populateViewHolder(orderArchive.ViewHolder viewHolder, Order model, int position) {
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
                viewHolder.Date.setText(model.getOrderDate());
                viewHolder.orderOption.setText(model.getOrderOption());
                viewHolder.tablenum.setText(model.getTablenum());
                viewHolder.price.setText(model.getTotalPaymentPrice().toString());
                viewHolder.status.setText(model.getPaymentStatus());





            }
        };

        mOrderList.setAdapter(firebaseRecyclerAdapter);
        mOrderList.setLayoutManager(manager);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView mQuantity;
        public final TextView tablenum;
        public final TextView Date;
        public final TextView email;
        public final TextView orderOption;
        public final TextView status;
        public final TextView price;




        public ViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.card_title);
            mQuantity = (TextView) itemView.findViewById(R.id.drink_price);
            tablenum = (TextView) itemView.findViewById(R.id.textView9);
            Date = (TextView) itemView.findViewById(R.id.textView8);
            email = (TextView) itemView.findViewById(R.id.textView4);
            orderOption = (TextView) itemView.findViewById(R.id.textView15);
            status = (TextView) itemView.findViewById(R.id.textView39);
            price = (TextView) itemView.findViewById(R.id.textView5);

        }

    }
}
