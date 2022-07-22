package com.hakanboranbay.bankingsystem.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

	public ArrayList<String> transactionLogs(long idNumber) {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("C:\\\\Users\\\\hakan\\\\Desktop\\\\Akbank Java Spring Bootcamp\\\\CodingPractises\\\\bankingsystem\\\\logs.txt"));
			String line = reader.readLine();
			ArrayList<String> list = new ArrayList<String>();
			while (line != null) {
				String[] parts = line.split(" ");
				if (parts[0].equals(idNumber + "")) {
					if (parts[1].equals("deposit")) {
						list.add(idNumber + " nolu hesaba " + parts[3] + " " + findByIdNumber(idNumber).getType() + " yatırılmıştır.");
					}else {
						list.add(idNumber+" nolu hesaptan " + parts[5] + " nolu hesaba " + parts[3] + " " + findByIdNumber(idNumber).getType() + " gönderilmiştir.");
					}
					line = reader.readLine();
				}else {
					line = reader.readLine();
				}
			}  
			reader.close();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	

}
