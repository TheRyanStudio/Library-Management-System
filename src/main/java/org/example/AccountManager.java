package org.example;

import java.util.ArrayList;

public class AccountManager {
    ArrayList<Account> accounts;

    public AccountManager(){accounts = new ArrayList<>();}

    public int getAccountSize(){
        return accounts.size();
    }
    Account getAccount(int index){
        return accounts.get(index);
    }

    public Account authenticate(String username, String password) {
        if (username == null || password == null) return null;
        for (Account account : accounts) {
            if (username.equals(account.getUsername()) && password.equals(account.getPassword())){
                return account;
            }
        }
        return null;
    }

    public void addAccount(Account account){accounts.add(account);}

    public Account getAccountByUsername(String username){
        for (Account account: accounts){
            if(username.equals(account.getUsername())){
                return account;
            }
        }
        return null;
    }
}
