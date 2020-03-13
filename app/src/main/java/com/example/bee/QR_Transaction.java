package com.example.bee;

import androidx.annotation.NonNull;

/**
 * This class represent one transaction via QRcode
 */
public class QR_Transaction {
    private String descr;
    private double amount;

    /**
     * consturctor
     * @param descr
     * @param amount
     */
    QR_Transaction(String descr, double amount) {

        // amount should greater than 0
        this.descr = descr;
        this.amount = amount;
    }

    @NonNull
    @Override
    /**
     * Show the description, "Abram owes me $10.00"
     */
    public String toString() {
        return this.descr;
    }

    /**
     * get the amount of this transaction, receive money is positive, transfer is negative
     * @return amount
     */
    public double getAmount() {
        return this.amount;
    }
}
