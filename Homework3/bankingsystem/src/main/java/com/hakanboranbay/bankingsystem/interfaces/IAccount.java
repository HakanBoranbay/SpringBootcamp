package com.hakanboranbay.bankingsystem.interfaces;

import com.hakanboranbay.bankingsystem.models.Account;

public interface IAccount {

    public Account create(
            String name,
            String surname,
            String email,
            String tcNo,
            String type
    ); 

    public Account findByIdNumber(long idNumber);
    
    public Account update(Account account);

    public boolean transfer(int amount, long senderAccountId, long recievedAccountId);
}
