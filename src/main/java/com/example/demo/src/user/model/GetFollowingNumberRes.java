package com.example.demo.src.user.model;

import lombok.Getter;

@Getter
public class GetFollowingNumberRes {
    private int followingNumbers;
    public GetFollowingNumberRes(){

    }
    public GetFollowingNumberRes(int followingNumbers){
        this.followingNumbers=followingNumbers;
    }
}
