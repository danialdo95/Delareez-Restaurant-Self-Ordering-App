package com.DA.delareez;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.model.delareez.Order;

/**
 * Created by User on 20/9/2017.
 */

public class OrderDA {
    DatabaseReference db;
    Boolean saved=null;

    public OrderDA(DatabaseReference db) {
        this.db = db;
    }




    //WRITE IF NOT NULL
    public Boolean CreateOrder(Order order, String UID)
    {
        if(order==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("Order").child(UID).setValue(order);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    public Boolean updateOrder(Order order, String key) {
        if (order == null) {
            saved = false;
        } else{
            try {
                db.child("Order").child(key).setValue(order);
                saved = true;

            }catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }


    public Boolean deleteOrder(String Key) {
        try {
            db.child("Order").child(Key).removeValue();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}