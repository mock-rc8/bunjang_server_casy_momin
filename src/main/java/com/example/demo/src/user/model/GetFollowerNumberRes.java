package com.example.demo.src.user.model;

import lombok.Getter;

@Getter
public class GetFollowerNumberRes {
    private int followerNumbers;
    public GetFollowerNumberRes(){

    }
    public GetFollowerNumberRes(int followerNumbers){
        this.followerNumbers =followerNumbers;
    }
}
