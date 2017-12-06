package com.android.delareez;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.DA.delareez.OrderDA;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Order;


public class Cart extends AppCompatActivity {

    public static boolean add= true;
    private static final String TAG = "Cart";
    private RecyclerView mOrderList;
    private DatabaseReference mFirebaseRef;
    private DatabaseReference mDatabaseMenu;
    private DatabaseReference mDatabaseCust;
    private LinearLayoutManager manager;
    private TextView total;
    private FirebaseRecyclerAdapter<Order, Cart.ViewHolder> firebaseRecyclerAdapter;
    OrderDA Helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference();
        mDatabaseCust = FirebaseDatabase.getInstance().getReference();
        total = (TextView) findViewById(R.id.total);


        Query OrderQuery = mFirebaseRef.child("Order").orderByChild("cart").equalTo("in cart");
        Helper=new OrderDA(mFirebaseRef);


        mOrderList = (RecyclerView) findViewById(R.id.listCart);
        manager = new LinearLayoutManager(this);
        mOrderList.setHasFixedSize(true);




        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, Cart.ViewHolder>(Order.class, R.layout.order_customer_card, Cart.ViewHolder.class, OrderQuery){
            Double sum = 0.0;

            @Override
            protected void populateViewHolder(Cart.ViewHolder viewHolder, Order model, int position) {
                final String post_key = getRef(position).getKey();
                String menuid = model.getMenuID();
                String custID = model.getCustID();


                    mDatabaseMenu.child("Menu").child(menuid).child("menuName").addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String menuname = dataSnapshot.getValue(String.class);
                            viewHolder.menuName.setText(menuname);
                        }

                        public void onCancelled(DatabaseError firebaseError) {
                        }
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



                    sum = sum + model.getTotalPaymentPrice();
                    total.setText(sum.toString());


                    viewHolder.mQuantity.setText(Integer.toString(model.getNumberOfMenu()));
                    viewHolder.Orderstatus.setText(model.getOrderStatus());
                }




        };


        mOrderList.setAdapter(firebaseRecyclerAdapter);
        mOrderList.setLayoutManager(manager);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView mQuantity;
        public final TextView tablenum;
        public final TextView Date;
        public final TextView Orderstatus;
        public final TextView email;




        public ViewHolder(View itemView) {
            super(itemView);
            menuName = (TextView) itemView.findViewById(R.id.card_title);
            mQuantity = (TextView) itemView.findViewById(R.id.drink_price);
            tablenum = (TextView) itemView.findViewById(R.id.textView9);
            Date = (TextView) itemView.findViewById(R.id.textView8);
            Orderstatus = (TextView) itemView.findViewById(R.id.textView5);
            email = (TextView) itemView.findViewById(R.id.textView4);

        }

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home)
            finish();
        return  super.onOptionsItemSelected(item);
    }
}
