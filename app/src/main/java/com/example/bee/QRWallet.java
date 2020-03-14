package com.example.bee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This is a class stores a user's transaction history
 */
public class QRWallet {
    private String name;
    private Collection<QRTransaction> transactions;

    QRWallet(String name) {
        this.name = name;
        this.transactions = new ArrayList<QRTransaction>();
    }

    /**
     * Get total amount inside wallet
     * @return total transaction amount
     */
    public String getTotalAmount() {
        double count = 0;
        Iterator<QRTransaction> iterator = this.transactions.iterator();

        while (iterator.hasNext()) {
            count += iterator.next().getAmount();
        }

        return String.format("%.2f", count);
    }

    public void addTransaction(String descr, double amount){
        this.transactions.add(new QRTransaction(descr, amount));
    }
    /**
     * Get the name of owner
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return a collection contains all transaction
     */
    public Collection<QRTransaction> getTransactions() {
        return this.transactions;
    }
}
