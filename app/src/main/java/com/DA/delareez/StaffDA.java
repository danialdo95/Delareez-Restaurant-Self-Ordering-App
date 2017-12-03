package com.DA.delareez;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.model.delareez.Staff;


/**
 * Created by User on 26/9/2017.
 */

public class StaffDA {

    DatabaseReference db;
    Boolean saved=null;

    public StaffDA(DatabaseReference db) {
        this.db = db;
    }

    public Boolean CreateStaff(Staff cust, String UID)
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
