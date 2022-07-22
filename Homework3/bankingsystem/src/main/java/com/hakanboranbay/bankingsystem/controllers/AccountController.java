package com.hakanboranbay.bankingsystem.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hakanboranbay.bankingsystem.interfaces.IAccount;
import com.hakanboranbay.bankingsystem.models.Account;
import com.hakanboranbay.bankingsystem.models.SerializableAccount;
import com.hakanboranbay.bankingsystem.requests.AccountCreateRequest;
import com.hakanboranbay.bankingsystem.requests.AccountDepositRequest;
import com.hakanboranbay.bankingsystem.requests.AccountTransferRequest;
import com.hakanboranbay.bankingsystem.responses.AccountCreateSuccessResponse;
import com.hakanboranbay.bankingsystem.responses.InvalidAccountTypeResponse;
import com.hakanboranbay.bankingsystem.responses.TransferInsufficientBalanceResponse;
import com.hakanboranbay.bankingsystem.responses.TransferSuccessResponse;

@RestController
public class AccountController {
	

	@Autowired
	private KafkaTemplate<String, String> producer;	
	
	@Autowired
	private IAccount iAccount;	
	
	@Autowired
	private SerializableAccount serializableAccount;

    @PostMapping("/accounts")
    public ResponseEntity<?> create(@RequestBody AccountCreateRequest request) {
        
    	Account newAccount = this.iAccount.create(
        		request.getName(),
                request.getSurname(),
                request.getEmail(),
                request.getTcNo(),
                request.getType()
                );
    	
    	if (newAccount == null) {

            InvalidAccountTypeResponse response = new InvalidAccountTypeResponse();
            response.setMessage("Invalid Account Type: " + request.getType());
            return ResponseEntity.badRequest().body(response);
        } else {
        	AccountCreateSuccessResponse response = new AccountCreateSuccessResponse();
        	response.setIdNumber(newAccount.getIdNumber());
            response.setMessage("Account created.");
            return ResponseEntity.ok().body(response);
        }
        
    	
    }

    @GetMapping("/accounts/{idNumber}")
    public ResponseEntity<?> detail(@PathVariable long idNumber) {
    	Account a = this.iAccount.findByIdNumber(idNumber);
    	return ResponseEntity.ok().lastModified(a.getLastModified()).body(a);
    }
    
    @PatchMapping("/accounts/{idNumber}")
    public ResponseEntity<?> deposit(@PathVariable long idNumber, @RequestBody AccountDepositRequest request) {
    	Account a = this.iAccount.findByIdNumber(idNumber);
    	a.setBalance(a.getBalance() + request.getAmount());
    	this.iAccount.update(a);
    	String message = idNumber + " deposit amount: " + request.getAmount();
    	producer.send("logs", message);
		return ResponseEntity.ok().lastModified(a.getLastModified()).body(a);
		
    }
    
    @PostMapping("/accounts/{idNumber}")
    public ResponseEntity<?> transfer(@PathVariable long idNumber, @RequestBody AccountTransferRequest request){
    	boolean result = this.iAccount.transfer(request.getAmount(), idNumber, request.getRecievedAccountIdNumber());
    	if (result) {
    		TransferSuccessResponse response = new TransferSuccessResponse();
    		response.setMessage("Transfer Succesful.");
    		String message = idNumber + " transfer amount: " + request.getAmount() + " recieved_account: " + request.getRecievedAccountIdNumber(); 
    		producer.send("logs", message);
    		return ResponseEntity.ok().body(response);
    	}
    	TransferInsufficientBalanceResponse response = new TransferInsufficientBalanceResponse();
    	response.setMessage("Insufficient balance.");
    	return ResponseEntity.badRequest().body(response);
    }
    
    @CrossOrigin(origins={"http://localhost"})
	@GetMapping(path="/accounts/logs/{idNumber}")
	public ArrayList<String> transactionLogs(@PathVariable long idNumber) {
    	return this.serializableAccount.transactionLogs(idNumber);
	}
}
