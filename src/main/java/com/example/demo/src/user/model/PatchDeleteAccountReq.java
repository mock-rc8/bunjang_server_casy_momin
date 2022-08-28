package com.example.demo.src.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchDeleteAccountReq {
    private int accountIdx;
    public PatchDeleteAccountReq(){

    }
    public PatchDeleteAccountReq(int accountIdx){
        this.accountIdx=accountIdx;
    }
}
