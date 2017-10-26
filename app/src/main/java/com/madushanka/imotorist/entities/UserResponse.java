package com.madushanka.imotorist.entities;


import java.util.List;

/**
 * Created by madushanka on 10/2/17.
 */

public class UserResponse {


    List<User> data;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
