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

    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        for (Account account : accounts) {
            if (username.equals(account.getUsername()) && password.equals(account.getPassword())){
                return true;
            }
        }
        return false;
    }

    public void addAccount(Account account){accounts.add(account);}

}
