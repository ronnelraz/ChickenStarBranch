package com.charoenpokhandfoodph.modal;

public class completedlist {

    public String id,name,client_id,transacid,status,statusHeader,datetime;

    public completedlist(String id, String name, String client_id, String transacid, String status, String statusHeader, String datetime) {
        this.id = id;
        this.name = name;
        this.client_id = client_id;
        this.transacid = transacid;
        this.status = status;
        this.statusHeader = statusHeader;
        this.datetime = datetime;
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

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTransacid() {
        return transacid;
    }

    public void setTransacid(String transacid) {
        this.transacid = transacid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusHeader() {
        return statusHeader;
    }

    public void setStatusHeader(String statusHeader) {
        this.statusHeader = statusHeader;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
