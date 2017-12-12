package com.android.delareez;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.DA.delareez.OrderDA;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Customer;
import com.model.delareez.Menu;
import com.model.delareez.Order;

public class UpdateOrderStatus extends AppCompatActivity {

    private TextView OrderID, MenuName, custEmail, Date, Quantity, tablenum, OrderOption;
    private TextView mcustID, mmenuID, payment, staffID, price;
    private Spinner status;
    private Button update;
    private DatabaseReference mFirebaseRef;
    private DatabaseReference mDatabaseMenu;
    private DatabaseReference mDatabaseCust;
    private CardView card;
    private RelativeLayout layout;
    OrderDA Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order_status);


        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        String post_key = getIntent().getExtras().getString("OrderID");

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference();
        mDatabaseCust = FirebaseDatabase.getInstance().getReference();
        layout = (RelativeLayout) findViewById(R.id.layoutO);
        card = (CardView) findViewById(R.id.cardView2);
        OrderID = (TextView) findViewById(R.id.textView26);
        MenuName = (TextView) findViewById(R.id.textView25);
        custEmail = (TextView) findViewById(R.id.textView29);
        Date = (TextView) findViewById(R.id.textView27);
        Quantity = (TextView) findViewById(R.id.textView28);
        tablenum = (TextView) findViewById(R.id.textView31);
        OrderOption = (TextView) findViewById(R.id.textView32);
        status = (Spinner) findViewById(R.id.spinner2);
        update = (Button) findViewById(R.id.button3);

        mcustID = (TextView) findViewById(R.id.textView22);
        mmenuID = (TextView) findViewById(R.id.textView23);
        payment = (TextView) findViewById(R.id.textView33);
        staffID = (TextView) findViewById(R.id.textView34);
        price = (TextView) findViewById(R.id.textView35);

        Helper=new OrderDA(mFirebaseRef);

        mFirebaseRef.child("Order").child(post_key).addValueEventListener(new ValueEventListener() {
         @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
             Order value = dataSnapshot.getValue(Order.class);
             if (dataSnapshot.exists()){
                 String menuid = value.getMenuID();
                 String custID = value.getCustID();

                 OrderID.setText(value.getOrderID());
                 Date.setText(value.getOrderDate());
                 Quantity.setText(value.getNumberOfMenu().toString());
                 tablenum.setText(value.getTablenum());
                 OrderOption.setText(value.getOrderOption());
                 payment.setText(value.getPaymentStatus());
                 staffID.setText(value.getStaffID());
                 mcustID.setText(custID);
                 mmenuID.setText(menuid);
                 price.setText(value.getTotalPaymentPrice().toString());

                 mDatabaseMenu.child("Menu").child(menuid).child("menuName").addListenerForSingleValueEvent(new ValueEventListener() {
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         String menuname = dataSnapshot.getValue(String.class);
                         MenuName.setText(menuname);

                     }

                     public void onCancelled(DatabaseError firebaseError) { }
                 });

                 mDatabaseCust.child("Customer").child(custID).child("custEmail").addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         String cust = dataSnapshot.getValue(String.class);
                         custEmail.setText(cust);

                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }

         }

         @Override
         public void onCancelled(DatabaseError databaseError) {

         }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mstatus = String.valueOf(status.getSelectedItem());
                Order order = new Order();
                order.setCart("placed");
                order.setCustID(mcustID.getText().toString());
                order.setMenuID(mmenuID.getText().toString());
                order.setNumberOfMenu(Integer.parseInt(Quantity.getText().toString()));
                order.setOrderDate(Date.getText().toString());
                order.setOrderID(post_key);
                order.setOrderOption(OrderOption.getText().toString());
                order.setPaymentStatus(payment.getText().toString());
                order.setStaffID(staffID.getText().toString());
                order.setTablenum(tablenum.getText().toString());
                order.setTotalPaymentPrice(Double.parseDouble(price.getText().toString()));
                order.setOrderStatus(mstatus);
                Helper.updateOrder(order, post_key);
                finish();
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home)
            finish();
        return  super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        startActivity(new Intent(UpdateOrderStatus.this, MainActivity.class));


    }
}
