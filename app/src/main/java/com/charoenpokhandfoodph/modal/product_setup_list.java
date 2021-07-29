package com.charoenpokhandfoodph.modal;

public class product_setup_list {
    public String id,name,img,price;
    public boolean mark;
    public boolean isSelected = false;

    public product_setup_list(String id, String name, String img, String price, boolean mark, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.price = price;
        this.mark = mark;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
