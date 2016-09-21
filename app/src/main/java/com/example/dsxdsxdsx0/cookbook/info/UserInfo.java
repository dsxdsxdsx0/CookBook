package com.example.dsxdsxdsx0.cookbook.info;

import java.io.Serializable;

/**
 * Created by dsxdsxdsx0 on 2016/9/18.
 *
 * 用户实体类
 */
public class UserInfo implements Serializable {

    private String name;

    private String password;

    private String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
