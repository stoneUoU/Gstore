package com.Market.Models;

public class PreSellBean {

    /**
     * id : 9
     * real_price : 2000
     * pic_path : /static_file/upload/9f668b7688da11e880d5021e2a6028ad.png
     * product_name : 北大荒绿野银耳80g/袋 精选东北特产
     * end_time : 2018-09-01 00:00:00
     * presell : true
     */

    private int id;
    private int real_price;
    private String pic_path;
    private String product_name;
    private String end_time;
    private boolean presell;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReal_price() {
        return real_price;
    }

    public void setReal_price(int real_price) {
        this.real_price = real_price;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean isPresell() {
        return presell;
    }

    public void setPresell(boolean presell) {
        this.presell = presell;
    }
}
