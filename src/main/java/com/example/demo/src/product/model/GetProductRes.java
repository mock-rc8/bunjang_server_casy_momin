package com.example.demo.src.product.model;


import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)


public class GetProductRes {

    private int productIdx;
    private String productURL;
    private int price;
    private String productName;
    private String address;
    private String created;
    private int heartCount;
    private int userHeart;
    private int pay;

}
