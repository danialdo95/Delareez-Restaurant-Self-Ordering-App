package com.DA.delareez;



import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.model.delareez.Menu;

import java.util.ArrayList;

/**
 * Created by User on 26/9/2017.
 */




public class MenuDA {

    DatabaseReference db;
    Boolean saved = null;
    ArrayList<Menu> menus = new ArrayList<>();

    public MenuDA(DatabaseReference db) {
        this.db = db;
    }


    //WRITE IF NOT NULL
    public Boolean CreateMenu(Menu menu, String id) {
        if (menu == null) {
            saved = false;
        } else {
            try {
                db.child("Menu").child(id).setValue(menu);
                saved = true;

            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        }

        return saved;
    }


    public Boolean updateMenu(Menu menu) {
            if (menu == null) {
                saved = false;
            } else{
                try {
                    db.setValue(menu);
                    saved = true;

                }catch (DatabaseException e) {
                    e.printStackTrace();
                    saved = false;
                }
            }
            return saved;
        }

    public Boolean updateMenuImage(Menu menu)
    {
        if (menu == null) {
            saved = false;
        }
        else{
            try {
                db.setValue(menu);
                saved = true;
            }catch (Exception e)
            {
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

    public Boolean deleteMenu(String Key) {
        try {
            db.child("Menu").child(Key).removeValue();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}








