package com.example.digtai_codriver;

public class TransactionModel {
    private String senderPhone;
    private String receiverPhone;
    private double amount;

    public TransactionModel(String senderPhone, String receiverPhone, double amount) {
        this.senderPhone = senderPhone;
        this.receiverPhone = receiverPhone;
        this.amount = amount;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public double getAmount() {
        return amount;
    }
}
