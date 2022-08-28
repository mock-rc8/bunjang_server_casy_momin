package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchAccountRes {
    private int accountIdx; // 계좌ID
    private String accountHolder;//예금주
    private String bankName;//은행명
    private String accountNum;//계좌번호
    private String status; // 계좌 상태
}
