package com.winningticketproject.in.MyAccountTab;

public class Transaction_data {

    public Transaction_data(String id, String transaction_type, String trandation_amount, String transation_date, String transation_orderid) {
        this.id = id;
        this.transaction_type = transaction_type;
        this.trandation_amount = trandation_amount;
        this.transation_date = transation_date;
        this.transation_orderid = transation_orderid;
    }

    String id;
    String transaction_type;
    String trandation_amount;
    String transation_date;
    String transation_orderid;

}
