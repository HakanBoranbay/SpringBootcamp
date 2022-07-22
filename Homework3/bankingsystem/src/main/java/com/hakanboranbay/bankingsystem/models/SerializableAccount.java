package com.hakanboranbay.bankingsystem.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hakanboranbay.bankingsystem.interfaces.IAccount;
import com.hakanboranbay.bankingsystem.interfaces.ITransaction;

@Component
public class SerializableAccount implements IAccount{
	
	@Autowired
	private ITransaction currency;

	@Override
	public Account create(String name, String surname, String email, String tcNo, String type) {
		
		if(type.equalsIgnoreCase("tl") || type.equalsIgnoreCase("dolar") || type.equalsIgnoreCase("altın")) {
			
			long idNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

		    Account account = new Account();
		    
		    account.setIdNumber(idNumber);
		    account.setBalance(0);
		    account.setEmail(email);
		    account.setName(name);
		    account.setSurname(surname);
		    account.setTcNo(tcNo);
		    account.setType(type);
		    account.setLastModified(System.currentTimeMillis());
		    
		    try {
				ObjectOutputStream os = new ObjectOutputStream(
						new FileOutputStream(
								new File(
										"C:\\Users\\hakan\\Desktop\\Akbank Java Spring Bootcamp\\CodingPractises\\bankingsystem\\Accounts\\" + account.getIdNumber()))
						);
				os.writeObject(account);
				os.close();
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    return account;
			
		} else {
			return null;
		}
		
	    
	}

	@Override
	public Account findByIdNumber(long idNumber) {
		ObjectInputStream is;
		try {
			is = new ObjectInputStream(
					new FileInputStream(
							new File(
									"C:\\Users\\hakan\\Desktop\\Akbank Java Spring Bootcamp\\CodingPractises\\bankingsystem\\Accounts\\" + idNumber))
					);
			Account a = (Account) is.readObject();
			is.close();
			return a;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Account update(Account account) {
		File f = new File("C:\\Users\\hakan\\Desktop\\Akbank Java Spring Bootcamp\\CodingPractises\\bankingsystem\\Accounts\\" + account.getIdNumber());
		f.delete();
		
		account.setLastModified(System.currentTimeMillis());

	    try {
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(
							new File(
									"C:\\Users\\hakan\\Desktop\\Akbank Java Spring Bootcamp\\CodingPractises\\bankingsystem\\Accounts\\" + account.getIdNumber()))
					);
			os.writeObject(account);
			os.close();
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return account;
	}

	@Override
	public boolean transfer(int amount, long senderAccountId, long recievedAccountId) {
		Account senderAccount = this.findByIdNumber(senderAccountId);
		Account recievedAccount = this.findByIdNumber(recievedAccountId);
		
		if (senderAccount.getBalance() < amount) {
			return false;
		}
		double transferredAmount = amount;
		if (!senderAccount.getType().equals(recievedAccount.getType())) {
			transferredAmount = this.currency.transactionCurrencyChange(amount, senderAccount.getType(), recievedAccount.getType());
		}
		recievedAccount.setBalance(recievedAccount.getBalance() + transferredAmount);
		senderAccount.setBalance(senderAccount.getBalance() - amount);
		this.update(senderAccount);
		this.update(recievedAccount);
		return true;
	}

}
