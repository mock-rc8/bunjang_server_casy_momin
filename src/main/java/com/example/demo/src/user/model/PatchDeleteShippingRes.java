package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchDeleteShippingRes {
    private int shippingIdx; // 삭제된 배송지ID
    private String status; // 삭제된 배송지 status
}
