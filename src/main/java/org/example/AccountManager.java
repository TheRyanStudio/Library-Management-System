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

    public void addAccount(Account account){accounts.add(account);}

}
