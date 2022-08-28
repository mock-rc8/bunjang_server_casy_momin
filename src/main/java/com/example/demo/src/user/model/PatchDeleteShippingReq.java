package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchDeleteShippingReq {
    private int shippingIdx; // 삭제할 배송지ID
    public PatchDeleteShippingReq(){

    }
    public PatchDeleteShippingReq(int shippingIdx){
        this.shippingIdx=shippingIdx;
    }
}
