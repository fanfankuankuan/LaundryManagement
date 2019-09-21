package com.recycle.laundrymanagement;

public class User {

    private String user_email;
    private String user_phonenum;
    private String user_password;
    private Boolean user_admin;

    public String getUser_email(){
        return user_email;
    }

    public void setUser_email(String user_email){
        this.user_email = user_email;
    }

    public String getUser_phonenum(){
        return user_phonenum;
    }

    public void setUser_phonenum(String user_phonenum){
        this.user_phonenum = user_phonenum;
    }

    public String getUser_password(){
        return user_password;
    }

    public void setUser_password(String user_password){
        this.user_password = user_password;

    }

    public Boolean getUser_admin(){return user_admin; }

    public void setUser_admin(Boolean user_admin){
        this.user_admin = user_admin;
    }
}
