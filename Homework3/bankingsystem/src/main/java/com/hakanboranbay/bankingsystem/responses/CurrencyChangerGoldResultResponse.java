package com.hakanboranbay.bankingsystem.responses;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CurrencyChangerGoldResultResponse {

	private String name; 
	private double buying;
	private String buyingstr;
	private double selling;
	private String sellingstr;
	private String time;
	private String date;
	private String datetime;
	private double rate;
}
