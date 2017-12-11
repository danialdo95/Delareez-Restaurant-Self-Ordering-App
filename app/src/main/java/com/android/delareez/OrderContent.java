package com.android.delareez;


import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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


public class OrderContent extends Fragment {

    private static final String TAG = "OrderContent";
    private RecyclerView mOrderList;
    private DatabaseReference mFirebaseRef;
    private DatabaseReference mDatabaseMenu;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseCust;
    private LinearLayoutManager manager;
    private FirebaseRecyclerAdapter<Order, OrderContent.ViewHolder> firebaseRecyclerAdapter;
    OrderDA Helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_order_content, container,false);



        mFirebaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseMenu = FirebaseDatabase.getInstance().getReference();
        mDatabaseCust = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        Query OrderQuery = mFirebaseRef.child("Order").orderByChild("custID").equalTo(id);
        Helper=new OrderDA(mFirebaseRef);


        mOrderList = (RecyclerView) myView.findViewById(R.id.order_list);
        manager = new LinearLayoutManager(this.getContext());
        mOrderList.setHasFixedSize(true);




        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Order, OrderContent.ViewHolder>(Order.class, R.layout.card_order_content, OrderContent.ViewHolder.class, OrderQuery){

            @Override
            protected void populateViewHolder(OrderContent.ViewHolder viewHolder, Order model, int position) {

                final String post_key = getRef(position).getKey();
                String menuid = model.getMenuID();


                if (model.getCart().equals("placed")) {

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
                    viewHolder.status.setText(model.getOrderStatus());
                    if (model.getOrderStatus().equals("Not Started")){
                        viewHolder.status.setTextColor(Color.RED);
                    }
                    else if (model.getOrderStatus().equals("Preparing")){
                        viewHolder.status.setTextColor(Color.BLUE);
                    }
                    else if (model.getOrderStatus().equals("Ready")){
                        viewHolder.status.setTextColor(Color.GREEN);
                    }
                    viewHolder.payment.setText(model.getPaymentStatus());






                } else if (model.getCart().equals("in cart")) {

                    viewHolder.menuName.setVisibility(View.GONE);
                    viewHolder.price.setVisibility(View.GONE);
                    viewHolder.mQuantity.setVisibility(View.GONE);
                    viewHolder.option.setVisibility(View.GONE);
                    viewHolder.layout.setVisibility(View.GONE);
                    viewHolder.cardView.setVisibility(View.GONE);
                    viewHolder.itemView.setVisibility(View.GONE);

                }

            }
        };

        mOrderList.setAdapter(firebaseRecyclerAdapter);
        mOrderList.setLayoutManager(manager);



        return myView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView menuName;
        public final TextView price;
        public final TextView mQuantity;
        public final CardView cardView;
        public final TextView option;
        public final TextView status;
        public final TextView payment;
        public final RelativeLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);

            menuName = (TextView) itemView.findViewById(R.id.textView10);
            price = (TextView) itemView.findViewById(R.id.textView12);
            mQuantity = (TextView) itemView.findViewById(R.id.textView7);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);
            option = (TextView) itemView.findViewById(R.id.textView16);
            status = (TextView) itemView.findViewById(R.id.textView19);
            payment  = (TextView) itemView.findViewById(R.id.textView21);

        }


    }

}
