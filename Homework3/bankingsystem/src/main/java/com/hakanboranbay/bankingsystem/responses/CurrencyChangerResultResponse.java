package com.hakanboranbay.bankingsystem.responses;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CurrencyChangerResultResponse {

	private String base;
	private String lastupdate;
	private CurrencyChangerDataResponse[] data;
}
