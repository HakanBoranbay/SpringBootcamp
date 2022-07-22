package com.hakanboranbay.bankingsystem.requests;

import lombok.Data;

@Data
public class AccountCreateRequest {

    private String name;
    private String surname;
    private String email;
    private String tcNo;
    private String type;
}
