package com.example.bee;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * This class is used to generate QRcode for rider to pay
 */
public class QRgenerator {
    private QRCodeWriter qrCodeWriter;
    private String amount;

    /**
     * Constructor
     * @param amount
     *      This is the amount of payment in String type
     */
    public QRgenerator(String amount) {
        this.amount = amount;
        qrCodeWriter = new QRCodeWriter();
    }

    /**
     * writeQRcode on an imageView
     * @param imageView
     *      the 200 * 200 ImageView used to show QRcode
     */
    public void writeQRcode(ImageView imageView) {
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(amount, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
