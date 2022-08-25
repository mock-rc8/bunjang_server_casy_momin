package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLogoutRes {
    private int userIdx;
    private String logoutMessage;
    private String status;
}
