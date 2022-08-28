package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAccountReq {
    private String accountHolder;//예금주
    private String bankName;//은행명
    private String accountNum;//계좌번호
}
