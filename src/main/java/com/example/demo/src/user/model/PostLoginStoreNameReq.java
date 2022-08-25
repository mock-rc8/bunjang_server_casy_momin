package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLoginStoreNameReq {
    private String storeName;
    public PostLoginStoreNameReq(){

    }
    public PostLoginStoreNameReq(String storeName){
        this.storeName=storeName;
    }
}
