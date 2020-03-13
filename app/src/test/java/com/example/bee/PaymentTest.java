package com.example.bee;

import org.junit.Test;

import static org.junit.Assert.*;

public class PaymentTest {
    private QR_Wallet driverWallet() {
        return new QR_Wallet("driver");
    }

    private QR_Wallet riderWallet() {
        return new QR_Wallet("rider");
    }

    @Test
    public void rejectNegativePayment_isCorrect() {
        Payment payment = new Payment();
        assertFalse(payment.make_Payment(driverWallet(), riderWallet(), -1));
        assertFalse(payment.make_Payment(driverWallet(),riderWallet(), 0));
        assertTrue(payment.make_Payment(driverWallet(), riderWallet(), 1));
    }

    @Test
    public void createTransaction_isCorrect() {
        QR_Wallet driver = driverWallet();
        QR_Wallet rider = riderWallet();
        Payment payment = new Payment();
        payment.make_Payment(driver, rider, 1);
        assertTrue(driver.getTransactions().size() == rider.getTransactions().size());
        assertEquals(1, driver.getTransactions().size());
    }

    @Test
    public void transactionAmount_isCorrect() {
        QR_Wallet driver = driverWallet();
        QR_Wallet rider = riderWallet();
        Payment payment = new Payment();
        payment.make_Payment(driver, rider, 233.33);
        assertEquals("233.33", driver.getTotalAmount());
        assertEquals("-233.33", rider.getTotalAmount());
    }
}
