package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostShippingReq { // 배송지 추가
    private String receiverName; // 수령인 이름
    private String address; // 수령인 주소
    private String detailAddress; // 수령인 상세주소
    private String receiverPhoneNum; // 수령인 연락처
}
