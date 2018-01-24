package com.android.delareez;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DA.delareez.OrderDA;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Menu;
import com.model.delareez.Order;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Cart extends AppCompatActivity {

    public static boolean add = true;
    private static final String TAG = Cart.class.getSimpleName();
    private RecyclerView mOrderList;
    private DatabaseReference mFirebaseRef;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseMenu;
    private DatabaseReference mDatabaseCust;
    private LinearLayoutManager manager;
    private TextView total;
    private Button placed;
    private FirebaseRecyclerAdapter<Order, Cart.ViewHolder> firebaseRecyclerAdapter;
    OrderDA Helper;





    private TextView mTvMessage;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference();
        mDatabaseCust = FirebaseDatabase.getInstance().getReference();
        total = (TextView) findViewById(R.id.total);


        Query OrderQuery = mFirebaseRef.child("Order").orderByChild("custID").equalTo(id);

        Helper = new OrderDA(mFirebaseRef);


        mOrderList = (RecyclerView) findViewById(R.id.listCart);
        manager = new LinearLayoutManager(this);
        mOrderList.setHasFixedSize(true);


        initNFC();

        mTvMessage = (TextView) findViewById(R.id.textView18);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, Cart.ViewHolder>(Order.class, R.layout.card_order, Cart.ViewHolder.class, OrderQuery) {


            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Order model, int position) {

                final String post_key = getRef(position).getKey();
                String menuid = model.getMenuID();


                if (model.getCart().equals("in cart")) {

                    mDatabaseMenu.child("Menu").child(menuid).child("menuName").addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String menuname = dataSnapshot.getValue(String.class);
                            viewHolder.menuName.setText(menuname);
                        }

                        public void onCancelled(DatabaseError firebaseError) {
                        }
                    });

                    viewHolder.price.setText(Double.toString(model.getTotalPaymentPrice()));
                    viewHolder.mQuantity.setText(Integer.toString(model.getNumberOfMenu()));
                    viewHolder.option.setText(model.getOrderOption());






                } else if (model.getCart().equals("placed")) {

                    viewHolder.menuName.setVisibility(View.GONE);
                    viewHolder.price.setVisibility(View.GONE);
                    viewHolder.mQuantity.setVisibility(View.GONE);
                    viewHolder.option.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.cardView.setVisibility(View.GONE);
                    viewHolder.itemView.setVisibility(View.GONE);

                }

                viewHolder.checkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if (viewHolder.option.getText().toString().equals("Dine in")){
                           String table = String.valueOf(mTvMessage.getText());
                           if(table.equals("?") || table.equals("TA")) {
                               Toast.makeText(getApplicationContext(),"Please Tag your phone to the NFC tag on the table for dine in and for take away at the take away table", Toast.LENGTH_SHORT).show();
                           }
                           else {
                               Date curDate = new Date();
                               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                               String DateToStr = format.format(curDate);

                               Order order = new Order();
                               order.setOrderID(post_key);
                               order.setOrderDate(DateToStr);
                               order.setOrderOption(model.getOrderOption());
                               order.setOrderStatus(model.getOrderStatus());
                               order.setPaymentStatus(model.getPaymentStatus());
                               order.setTotalPaymentPrice(model.getTotalPaymentPrice());
                               order.setNumberOfMenu(model.getNumberOfMenu());
                               order.setCart("placed");
                               order.setMenuID(model.getMenuID());
                               order.setCustID(model.getCustID());
                               order.setStaffID(model.getStaffID());
                               order.setTablenum(String.valueOf(mTvMessage.getText()));


                               Helper.updateOrder(order, post_key);
                           }
                       }
                       else if (viewHolder.option.getText().toString().equals("Take Away")){
                           String table = String.valueOf(mTvMessage.getText());
                           if(!"TA".equals(table)) {
                               Toast.makeText(getApplicationContext(),"Please Tag your phone to the NFC tag on the table for dine in and for take away at the take away table", Toast.LENGTH_SHORT).show();
                           }
                           else {
                               Date curDate = new Date();
                               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                               String DateToStr = format.format(curDate);

                               Order order = new Order();
                               order.setOrderID(post_key);
                               order.setOrderDate(DateToStr);
                               order.setOrderOption(model.getOrderOption());
                               order.setOrderStatus(model.getOrderStatus());
                               order.setPaymentStatus(model.getPaymentStatus());
                               order.setTotalPaymentPrice(model.getTotalPaymentPrice());
                               order.setNumberOfMenu(model.getNumberOfMenu());
                               order.setCart("placed");
                               order.setMenuID(model.getMenuID());
                               order.setCustID(model.getCustID());
                               order.setStaffID(model.getStaffID());
                               order.setTablenum(String.valueOf(mTvMessage.getText()));


                               Helper.updateOrder(order, post_key);
                           }
                       }



                    }
                });

                viewHolder.cancelorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Helper.deleteOrder(post_key);
                    }


                });


            }
        };

        mOrderList.setAdapter(firebaseRecyclerAdapter);
        mOrderList.setLayoutManager(manager);


        mFirebaseRef.child("Order").orderByChild("custID").equalTo(id).addChildEventListener(new ChildEventListener() {
            Double sum = 0.0;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                if (order.getCart().equals("in cart")){
                    sum = sum + order.getTotalPaymentPrice();
                    total.setText(sum.toString());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                    sum = sum - order.getTotalPaymentPrice();
                    total.setText(sum.toString());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView price;
        public final TextView mQuantity;
        public final CardView cardView;
        public final Button checkout;
        public final ImageButton cancelorder;
        public final TextView option;
        public final RelativeLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);

            menuName = (TextView) itemView.findViewById(R.id.textView10);
            price = (TextView) itemView.findViewById(R.id.textView12);
            mQuantity = (TextView) itemView.findViewById(R.id.textView7);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
            option = (TextView) itemView.findViewById(R.id.textView16);
            checkout = (Button) itemView.findViewById(R.id.button2);
            cancelorder = (ImageButton) itemView.findViewById(R.id.imageButton);

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }










    private void initNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }




    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);



        Log.d(TAG, "onNewIntent: "+intent.getAction());

        if(tag != null) {
            Toast.makeText(this, getString(R.string.message_tag_detected), Toast.LENGTH_SHORT).show();
            Ndef ndef = Ndef.get(tag);
            try {
                ndef.connect();
                NdefMessage ndefMessage = ndef.getNdefMessage();
                String message = new String(ndefMessage.getRecords()[0].getPayload());
                Log.d(TAG, "readFromNFC: "+message);
                mTvMessage.setText(message);
                ndef.close();

            } catch (IOException | FormatException e) {
                e.printStackTrace();

            }


        }


    }


}