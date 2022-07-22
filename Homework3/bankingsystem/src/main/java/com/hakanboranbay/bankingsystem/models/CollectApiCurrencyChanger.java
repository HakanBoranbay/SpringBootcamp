package com.hakanboranbay.bankingsystem.models;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hakanboranbay.bankingsystem.interfaces.ITransaction;
import com.hakanboranbay.bankingsystem.responses.CurrencyChangerGoldResponse;
import com.hakanboranbay.bankingsystem.responses.CurrencyChangerResponse;

@Component
public class CollectApiCurrencyChanger implements ITransaction{
		
	public double goldTL() {
		
		RestTemplate template = new RestTemplate();	
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", "apikey 5rkkESHIxcoyNhRDqhGYZG:1DnQuHAsJSlTLddulP4LTy");
		headers.add("content-type", "application/json");
		String url = "https://api.collectapi.com/economy/goldPrice";
		
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<CurrencyChangerGoldResponse> response = template.exchange(url, HttpMethod.GET, requestEntity, CurrencyChangerGoldResponse.class);
				
		double goldTL = response.getBody().getResult()[0].getBuying();
		return goldTL;
	}
	
	

	@Override
	public double transactionCurrencyChange(int amount, String accountTypeOne, String accountTypeTwo) {
			
		RestTemplate template = new RestTemplate();	
		HttpHeaders headers = new HttpHeaders();
				
		headers.add("authorization", "apikey 5rkkESHIxcoyNhRDqhGYZG:1DnQuHAsJSlTLddulP4LTy");
		headers.add("content-type", "application/json");
		String url1 = "https://api.collectapi.com/economy/exchange?int=1&to=TRY&base=USD";
		
		HttpEntity<?> requestEntity1 = new HttpEntity<>(headers);
		ResponseEntity<CurrencyChangerResponse> response1 = template.exchange(url1, HttpMethod.GET, requestEntity1, CurrencyChangerResponse.class);
				
		double dolarTL = response1.getBody().getResult().getData()[0].getCalculated();
		
		
		
		if (accountTypeOne.equalsIgnoreCase("tl") && accountTypeTwo.equalsIgnoreCase("dolar")) {
			return amount / dolarTL;
			
		}
		if (accountTypeOne.equalsIgnoreCase("tl") && accountTypeTwo.equalsIgnoreCase("altın")) {
			return amount / goldTL();
			
		}
		
		if (accountTypeOne.equalsIgnoreCase("dolar") && accountTypeTwo.equalsIgnoreCase("tl")) {

			return amount * dolarTL;
		}
		
		if (accountTypeOne.equalsIgnoreCase("dolar") && accountTypeTwo.equalsIgnoreCase("altın")) {
			return amount / (goldTL() / dolarTL);
			
		}
		
		if (accountTypeOne.equalsIgnoreCase("altın") && accountTypeTwo.equalsIgnoreCase("tl")) {
			return amount * goldTL();
			
		}
		if (accountTypeOne.equalsIgnoreCase("altın") && accountTypeTwo.equalsIgnoreCase("dolar")) {
			return amount * (goldTL() / dolarTL);
		}
		
	return amount;
	}

}
