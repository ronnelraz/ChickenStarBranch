package com.charoenpokhandfoodph.modal;

public class order_view_list {

    public String order_id,product_id,img,name,price,tag,total_qty,qty,status,datetime,contact,complete,statusHeader,percent,newprice,promotion_status,promotion_code;


    public order_view_list(String order_id, String product_id, String img, String name, String price, String tag, String total_qty, String qty, String status, String datetime, String contact, String complete, String statusHeader, String percent, String newprice, String promotion_status, String promotion_code) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.img = img;
        this.name = name;
        this.price = price;
        this.tag = tag;
        this.total_qty = total_qty;
        this.qty = qty;
        this.status = status;
        this.datetime = datetime;
        this.contact = contact;
        this.complete = complete;
        this.statusHeader = statusHeader;
        this.percent = percent;
        this.newprice = newprice;
        this.promotion_status = promotion_status;
        this.promotion_code = promotion_code;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getStatusHeader() {
        return statusHeader;
    }

    public void setStatusHeader(String statusHeader) {
        this.statusHeader = statusHeader;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getNewprice() {
        return newprice;
    }

    public void setNewprice(String newprice) {
        this.newprice = newprice;
    }

    public String getPromotion_status() {
        return promotion_status;
    }

    public void setPromotion_status(String promotion_status) {
        this.promotion_status = promotion_status;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }
}
