package com.hakanboranbay.bankingsystem.responses;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CurrencyChangerResponse {

	private boolean success;
	private CurrencyChangerResultResponse result;
}
