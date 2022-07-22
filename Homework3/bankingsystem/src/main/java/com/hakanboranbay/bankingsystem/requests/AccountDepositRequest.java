package com.hakanboranbay.bankingsystem.requests;

import lombok.Data;

@Data
public class AccountDepositRequest {
	
	private double amount;
	private long idNumber;
	private String type;
}
