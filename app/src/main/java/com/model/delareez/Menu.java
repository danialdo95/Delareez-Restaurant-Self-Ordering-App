package com.model.delareez;

import android.media.Image;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;


/**
 * Created by User on 26/9/2017.
 */

public class Menu {

    private String menuID;
    private String menuName;
    private Boolean menuPrice;
    private Image menuImage;

    public Menu(String menuID, String menuName, Boolean menuPrice, Image menuImage) {
        this.menuID = menuID;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuImage = menuImage;
    }

    public String getMenuID() {
        return menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public Boolean getMenuPrice() {
        return menuPrice;
    }

    public Image getMenuImage() {
        return menuImage;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setMenuPrice(Boolean menuPrice) {
        this.menuPrice = menuPrice;
    }

    public void setMenuImage(Image menuImage) {
        this.menuImage = menuImage;
    }


}
