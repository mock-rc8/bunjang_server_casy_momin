package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//배송지 조회
public class GetShippingRes { // GetShippingRes -> mypage > 배송지설정 -> 유저가 등록해둔 주소관리 리스트가 나옴.
    private int shippingIdx; // 배송지ID
    private String receiverName; // 수령인 이름
    private String address; // 수령인 주소
    private String receiverPhoneNum; // 수령인 연락처
    private String status; // 배송지 상태
}
