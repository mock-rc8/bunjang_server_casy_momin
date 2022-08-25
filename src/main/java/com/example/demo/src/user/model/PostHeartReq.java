package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostHeartReq {
    private int productIdx;
    public PostHeartReq(){

    }
    public PostHeartReq(int productIdx){
        this.productIdx = productIdx;
    }
}
