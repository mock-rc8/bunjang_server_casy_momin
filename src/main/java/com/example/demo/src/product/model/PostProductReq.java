package com.example.demo.src.product.model;


import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostProductReq {

    private int userIdx;
    private String productsName;
    private String imageURL;
    private String address;
    private String mainCategory;
    private String subCategory;
    private String hashtagText;
    private int price;
    private String shippingFee;//배송비포함
    private int quantity;
    private String productStatus; //중고인지 판단
    private String exchange;
    private String productExplaination;
    private int pay; //번개페이 사용여부
}
