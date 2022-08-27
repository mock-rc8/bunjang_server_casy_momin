package com.example.demo.src.product.model;


import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class GetProductDetailRes {

    private int productIdx;
    private String productImgURL;
    private int price;
    private int pay;
    private String productName;
    private String address;
    private String created;
    private int views;
    private int heart;
    private int talk;
    private String status; //중고 상품인지
    private int quantity;
    private String shippingFee; //배송비 여부
    private String exchange;//교환 여부
    private String productExplaination;
    private String hashtag; //배열에 담아서 문자열로 바꿔서 줄것

}
