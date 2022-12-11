package com.ShabrinaJSleepDN.jsleep_android.model;

public class Voucher extends Serializable {
    public Type type;
    public double cut;
    public String name;
    public int code;
    public double minimum;
    private boolean used;

    public enum Type
    {
        REBATE, DISCOUNT
    }

}
