package com.example.demo.src.user.model;

import lombok.Getter;

@Getter
public class GetReviewNumberRes {
    private int reviewNumbers;
    public GetReviewNumberRes(){

    }
    public GetReviewNumberRes(int reviewNumbers){
        this.reviewNumbers=reviewNumbers;
    }
}
