package com.hakanboranbay.bankingsystem.responses;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CurrencyChangerGoldResponse {
	
	private boolean success;
	private CurrencyChangerGoldResultResponse[] result;

}
