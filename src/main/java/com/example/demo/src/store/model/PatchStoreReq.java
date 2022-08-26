package com.example.demo.src.store.model;


import lombok.*;

import java.sql.Time;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchStoreReq {


    private int userIdx;
    private int storeIdx;
    private String storeName;
    private String imageURL;
    private String storeURL;
    private Time accessTimeStart;
    private Time accessTimeEnd;
    private String storeExplaination;
    private String policy;
    private String caution;


}
