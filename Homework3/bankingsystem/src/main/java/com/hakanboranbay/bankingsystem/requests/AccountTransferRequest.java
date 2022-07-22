package com.hakanboranbay.bankingsystem.requests;

import lombok.Data;

@Data
public class AccountTransferRequest {

	private int amount;
	private long recievedAccountIdNumber;
	
}
