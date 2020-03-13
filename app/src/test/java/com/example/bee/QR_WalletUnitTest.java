package com.example.bee;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QR_WalletUnitTest {
    private QR_Wallet mockWallet() {
        QR_Wallet mockWallet = new QR_Wallet("Ruichen");
        return mockWallet;
    }

    @Test
    public void name_isCorrect() {
        assertTrue(mockWallet().getName().equals("Ruichen"));
    }

    @Test
    public void add_isCorrect() {
        QR_Wallet mockWallet = mockWallet();
        assertEquals(0, mockWallet.getTransactions().size());
        mockWallet.addTransaction("", 2);
        assertEquals(1, mockWallet.getTransactions().size());
    }

    @Test
    public void totalAmount_isCorrect() {
        QR_Wallet mockWallet = mockWallet();
        assertEquals("0.00", mockWallet.getTotalAmount());
        mockWallet.addTransaction("", 2);
        assertEquals("2.00", mockWallet.getTotalAmount());
        mockWallet.addTransaction("", -1.3);
        assertEquals("0.70", mockWallet.getTotalAmount());
    }
}