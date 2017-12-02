package com.DA.delareez;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.model.delareez.Menu;
import com.model.delareez.Order;
import com.model.delareez.Staff;

import java.util.ArrayList;

/**
 * Created by User on 20/9/2017.
 */

public class OrderDA {
    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Menu> menus=new ArrayList<>();

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
/*
    public Boolean UpdateOrder(int position,String newName)
    {
        try {
            menus.remove(position);
            menus.add(position,newName);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public Boolean DeleteOrder(int position)
    {
        try {
            menus.remove(position);
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
*/
}