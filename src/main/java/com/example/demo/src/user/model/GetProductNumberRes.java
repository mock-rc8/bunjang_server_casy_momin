package com.example.demo.src.user.model;

import lombok.Getter;

@Getter
public class GetProductNumberRes {
    private int productsNumbers;
    public GetProductNumberRes(){

    }
    public GetProductNumberRes(int productsNumbers){
        this.productsNumbers =productsNumbers;
    }
}
