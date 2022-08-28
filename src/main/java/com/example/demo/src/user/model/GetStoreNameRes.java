package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetStoreNameRes {
    private String storeName;
    public GetStoreNameRes(){

    }
    public GetStoreNameRes(String storeName){
        this.storeName=storeName;
    }
}
