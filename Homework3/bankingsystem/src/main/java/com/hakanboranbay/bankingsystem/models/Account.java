package com.hakanboranbay.bankingsystem.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
    private String surname;
    private String email;
    private String tcNo;
    private String type;

    private double balance = getBalance();

    private long idNumber;
    private long lastModified;
    
    

}
