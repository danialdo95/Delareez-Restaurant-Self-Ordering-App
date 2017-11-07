package com.DA.delareez;



import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.model.delareez.Menu;

import java.util.ArrayList;

/**
 * Created by User on 26/9/2017.
 */




public class MenuDA {

    DatabaseReference db;
    Boolean saved=null;
    ArrayList<Menu> menus=new ArrayList<>();

    public MenuDA(DatabaseReference db) {
        this.db = db;
    }




    //WRITE IF NOT NULL
    public Boolean CreateMenu(Menu menu)
    {
        if(menu==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("Menu").push().setValue(menu);
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
 */
    public Boolean deleteMenu(String Key)
    {
        try {
            db.child("Menu").child(Key).removeValue();
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        menus.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Menu menu = ds.getValue(Menu.class);
            menus.add(menu);
        }
    }

    //READ THEN RETURN ARRAYLIST
    public ArrayList<Menu> retrieve() {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return menus;
    }




}








