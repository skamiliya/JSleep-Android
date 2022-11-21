package com.ShabrinaJSleepDN.jsleep_android.model;

public class Account extends Serializable{
    public String name;
    public String password;
    public Renter renter;
    public String email;
    public double balance;

    public Account(int id) {
        super(id);
    }

    @Override
    public String toString(){
        return "Account{" +
                "balance=" + balance +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", renter=" + renter +
                '}';
    }

}
