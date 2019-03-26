package com.Market.Models;

public class CarouselBean {

    /**
     * redirect_url :
     * id : 10
     * vendor_id : 4
     * title : 轮播图2
     * pic_path : /static_file/upload/3b248a8c88d511e8a28e021e2a6028ad.png
     * sn : 1
     * is_deleted : false
     * description : 轮播图2
     */

    private String redirect_url;
    private int id;
    private int vendor_id;
    private String title;
    private String pic_path;
    private int sn;
    private boolean is_deleted;
    private String description;

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
