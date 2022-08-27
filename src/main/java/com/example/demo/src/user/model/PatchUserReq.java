package com.example.demo.src.user.model;

import lombok.*;

@Getter
@Setter
public class PatchUserReq {
    private String gender;
    public PatchUserReq(){

    }
    public PatchUserReq(String gender){
        this.gender=gender;
    }
}
