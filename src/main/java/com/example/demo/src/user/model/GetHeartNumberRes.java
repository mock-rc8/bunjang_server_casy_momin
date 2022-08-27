package com.example.demo.src.user.model;

import lombok.Getter;

@Getter
public class GetHeartNumberRes {
    private int heartNumbers;
    public GetHeartNumberRes(){

    }
    public GetHeartNumberRes(int heartNumbers){
        this.heartNumbers =heartNumbers;
    }
}
