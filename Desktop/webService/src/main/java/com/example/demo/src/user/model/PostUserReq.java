package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String name;
    private String birthDate;
    private String phoneNum;
    private String carrier;
    private String password;
    private String storeName;
}
