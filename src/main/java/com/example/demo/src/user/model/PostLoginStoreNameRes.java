package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLoginStoreNameRes {
    private String storeName;
    public PostLoginStoreNameRes(){

    }
    public PostLoginStoreNameRes(String storeName){
        this.storeName=storeName;
    }
}
