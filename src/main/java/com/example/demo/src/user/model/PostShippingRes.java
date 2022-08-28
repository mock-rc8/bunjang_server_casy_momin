package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShippingRes { // 배송지 조회와 똑같은 Response
    private int shippingIdx; // 배송지ID
    private String receiverName; // 수령인 이름
    private String receiverPhoneNum; // 수령인 연락처
    private String address; // 수령인 주소
    private String detailAddress; // 수령인 상세주소
    private String status; // 배송지 status
}
