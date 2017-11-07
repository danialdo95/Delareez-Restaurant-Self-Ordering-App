package com.DA.delareez;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.model.delareez.Customer;
import com.model.delareez.Menu;

import java.util.ArrayList;

/**
 * Created by User on 26/9/2017.
 */

public class CustomerDA {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Menu> menus=new ArrayList<>();

    public CustomerDA(DatabaseReference db) {
        this.db = db;
    }




    //WRITE IF NOT NULL
    public Boolean CreateCustomer(Customer cust, String UID)
    {
        if(cust==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child(UID).setValue(cust);
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
    public Boolean update(int position,String newName)
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
    public Boolean delete(int position)
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
