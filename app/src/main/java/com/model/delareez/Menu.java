package com.model.delareez;


/**
 * Created by User on 26/9/2017.
 */

public class Menu {

    private String menuID;
    private String menuName;
    private Double menuPrice;
    private String menuType;
    private String menuImage;
    private String menuStatus;

    public Menu (){ }


    public Menu(String menuID, String menuName, Double menuPrice, String menuType, String menuImage, String menuStatus) {
        this.menuID = menuID;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuType = menuType;
        this.menuImage = menuImage;
        this.menuStatus = menuStatus;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Double getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(Double menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(String menuStatus) {
        this.menuStatus = menuStatus;
    }

}
