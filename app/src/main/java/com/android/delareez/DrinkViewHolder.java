package com.android.delareez;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Listener.delareez.ItemClickListener;

/**
 * Created by User on 19/10/2017.
 */

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final TextView menuName;
    public final TextView menuPrice;
    public final ImageView menuImage;
    //public final Button UpdateButton;
    ItemClickListener itemClickListener;

    public DrinkViewHolder(View itemView) {
        super(itemView);

        menuName = (TextView) itemView.findViewById(R.id.drink_name);
        menuPrice = (TextView) itemView.findViewById(R.id.drink_price);
        menuImage = (ImageView) itemView.findViewById(R.id.drink_image);
        //UpdateButton = (Button) itemView.findViewById(R.id.action_button);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }


    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
}
