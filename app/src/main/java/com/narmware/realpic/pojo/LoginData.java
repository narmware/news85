package com.narmware.realpic.pojo;

/**
 * Created by rohitsavant on 23/05/18.
 */

public class LoginData {
    String user_name,user_email,user_profile_pic;

    public LoginData(String user_name, String user_email, String user_profile_pic) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_profile_pic = user_profile_pic;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public void setUser_profile_pic(String user_profile_pic) {
        this.user_profile_pic = user_profile_pic;
    }
}
