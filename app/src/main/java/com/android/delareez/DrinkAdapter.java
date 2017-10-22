package com.android.delareez;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Listener.delareez.ItemClickListener;
import com.model.delareez.Menu;

import java.util.ArrayList;

/**
 * Created by User on 19/10/2017.
 */

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    Context c;
    ArrayList<Menu> menus;

    public DrinkAdapter(Context c, ArrayList<Menu> menus) {
        this.c = c;
        this.menus = menus;
    }

    @Override
    public DrinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.drink_row,parent,false);
        return new DrinkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DrinkViewHolder holder, int position) {
        final  Menu s=menus.get(position);

        holder.menuName.setText(s.getMenuName());
        holder.menuPrice.setText(Double.toString(s.getMenuPrice()));
        //holder.menuImage.setImageURI(Uri.parse(s.getMenuImage()));


    }

    @Override
    public int getItemCount() {
        return menus.size();
    }


}
