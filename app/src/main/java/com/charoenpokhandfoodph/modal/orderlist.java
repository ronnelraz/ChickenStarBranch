package com.charoenpokhandfoodph.modal;

public class orderlist {

    public String order_id,transid,product_id,sub,charge,total,accept,name,datetime,client_id,km;

    public orderlist(String order_id, String transid, String product_id, String sub, String charge, String total, String accept, String name, String datetime, String client_id, String km) {
        this.order_id = order_id;
        this.transid = transid;
        this.product_id = product_id;
        this.sub = sub;
        this.charge = charge;
        this.total = total;
        this.accept = accept;
        this.name = name;
        this.datetime = datetime;
        this.client_id = client_id;
        this.km = km;
    }


    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }
}
