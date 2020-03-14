package com.example.bee;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 */
public class QRWalletUnitTest {
    private QRWallet mockWallet() {
        QRWallet mockWallet = new QRWallet("Ruichen");
        return mockWallet;
    }

    @Test
    public void name_isCorrect() {
        assertTrue(mockWallet().getName().equals("Ruichen"));
    }

    @Test
    public void add_isCorrect() {
        QRWallet mockWallet = mockWallet();
        assertEquals(0, mockWallet.getTransactions().size());
        mockWallet.addTransaction("", 2);
        assertEquals(1, mockWallet.getTransactions().size());
    }

    @Test
    public void totalAmount_isCorrect() {
        QRWallet mockWallet = mockWallet();
        assertEquals("0.00", mockWallet.getTotalAmount());
        mockWallet.addTransaction("", 2);
        assertEquals("2.00", mockWallet.getTotalAmount());
        mockWallet.addTransaction("", -1.3);
        assertEquals("0.70", mockWallet.getTotalAmount());
    }
}