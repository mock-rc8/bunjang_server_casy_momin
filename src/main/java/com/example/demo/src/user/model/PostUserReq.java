package com.example.demo.src.user.model;

import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 파라미터가 없는 생성자를 생성, 접근제한자를 PROTECTED로 설정.
public class PostUserReq { // 회원가입 model
    private String name;
    private String residentNumLast; // 주민 뒷자리 1
    private String residentNumFirst; // 주민 앞자리 6
    private String phoneNum;
    private String carrier;
    private String password;
    private String storeName;
}
