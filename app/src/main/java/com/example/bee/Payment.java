package com.example.bee;

/**
 * This is a class used to handle payment for riders and drivers
 * It will be called at driver's activity
 */
public class Payment {
    Payment() {
    }

    /**
     * If payment is successfully made, return true, otherwise return false
     * @param driverWallet
     * @param riderWallet
     * @param amount
     * @return
     */
    public boolean make_Payment(QRWallet driverWallet, QRWallet riderWallet, double amount) {
        if (amount <= 0) return false;
        String driverDescr = String.format("%s owes me $%.2f", riderWallet.getName(), amount);
        String riderDescr = String.format("I owe %s $%.2f", driverWallet.getName(), amount);
        driverWallet.addTransaction(driverDescr, amount);
        riderWallet.addTransaction(riderDescr, -1 * amount);
        return true;
    }
}
