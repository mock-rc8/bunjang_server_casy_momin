package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAutoLoginRes {
    private int userIdx; // 상점ID
    private String status; // 유저의 상태
}
