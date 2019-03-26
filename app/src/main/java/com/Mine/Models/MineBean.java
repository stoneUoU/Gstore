package com.Mine.Models;

import java.util.List;

//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdXRoX2lkIjoiMTU3MTc5MTQ1MDUiLCJsb2dpbl9hdXRoX2lkIjoxMjAsImlkIjo4OSwiYXV0aF90eXBlIjoxLCJ0aW1lc3RhbXAiOjE1MzMyNTY4OTcuODY4NDA4fQ.GBM0BAZlRuqWLiSP-NsE9BeX4CUIWHTbwYmet4dt70E
public class MineBean {

    /**
     * enable : true
     * nick_name : User_934675539
     * gender : 3
     * id : 89
     * profile_photo : /static_file/upload/0404dd32949b11e8ad74021e2acb5ac9.png
     * order_nums : [0,0,0,0,17]
     * birthday : 1970-01-01
     * create_time : 2018-07-04 11:44:55
     * tel : 15717914505
     * customer_tel : 4008627996
     */

    private boolean enable;
    private String nick_name;
    private int gender;
    private int id;
    private String profile_photo;
    private String birthday;
    private String create_time;
    private String tel;
    private String customer_tel;
    private List<Integer> order_nums;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCustomer_tel() {
        return customer_tel;
    }

    public void setCustomer_tel(String customer_tel) {
        this.customer_tel = customer_tel;
    }

    public List<Integer> getOrder_nums() {
        return order_nums;
    }

    public void setOrder_nums(List<Integer> order_nums) {
        this.order_nums = order_nums;
    }
}
