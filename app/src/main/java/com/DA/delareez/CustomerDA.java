package com.DA.delareez;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.model.delareez.Customer;


/**
 * Created by User on 26/9/2017.
 */

public class CustomerDA {

    DatabaseReference db;
    Boolean saved=null;

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

}
