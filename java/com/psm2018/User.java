package com.psm2018;

/**
 * Created by Rohizam on 27/10/2017.
 */

public class User {
    private String Name;
    private String Password;
    private String Phonenum;
    private String Email;
    private String Heartbeat;
    private String Emer;
    private String Img;
    private String Date;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }





    public User() {
    }


    public String getEmer() {
        return Emer;
    }

    public void setEmer(String emer) {
        Emer = emer;
    }

    public User(String phonenum, String password, String email, String emer) {

        Phonenum = phonenum;
        Password = password;
        Email = email;
        Emer = emer;


    }

    public void setall(String phonenum, String password, String email, String emer,String img) {
        Phonenum = phonenum;
        Password = password;
        Email = email;
        Emer = emer;
        Img = img;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public void braaa(String img){
        Img = img;
    }

    public String getHeartbeat() {
        return Heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        Heartbeat = heartbeat;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


    public String getPhonenum() {
        return Phonenum;
    }

    public void setPhonenum(String phonenum) {
        Phonenum = phonenum;
    }

    public String getName() {
        return Name;
    }


    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }



}
