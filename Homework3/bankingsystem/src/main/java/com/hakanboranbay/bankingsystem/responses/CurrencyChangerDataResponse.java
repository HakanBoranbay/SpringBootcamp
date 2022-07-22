package com.hakanboranbay.bankingsystem.responses;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CurrencyChangerDataResponse {

	private String code;
	private String name;
	private String rate;
	private String calculatedstr;
	private double calculated;
}
