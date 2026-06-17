package com.example.demo.service;

import com.example.demo.model.BarcodeType;
import com.example.demo.model.Profile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CodeImageService {
    private final String verificationBaseUrl;

    public CodeImageService(@Value("${app.verification-base-url}") String verificationBaseUrl) {
        this.verificationBaseUrl = verificationBaseUrl;
    }

    public byte[] qrCode(Profile profile) {
        String data = verificationBaseUrl + "/" + profile.getUuid();
        try {
            return writePng(new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, 220, 220));
        } catch (WriterException ex) {
            throw new IllegalStateException("Could not generate QR code", ex);
        }
    }

    public byte[] barcode(Profile profile) {
        BarcodeType barcodeType = profile.getBarcodeType() == null ? BarcodeType.CODE_128 : profile.getBarcodeType();
        if (barcodeType == BarcodeType.EAN_13) {
            return writePng(new EAN13Writer().encode(toEan13(profile.getRegistrationNumber()), BarcodeFormat.EAN_13, 320, 90));
        }
        return writePng(new Code128Writer().encode(profile.getRegistrationNumber(), BarcodeFormat.CODE_128, 320, 90));
    }

    public String verificationUrl(Profile profile) {
        return verificationBaseUrl + "/" + profile.getUuid();
    }

    private String toEan13(String value) {
        String digits = value == null ? "" : value.replaceAll("\\D", "");
        if (digits.length() >= 12) {
            return digits.substring(0, 12);
        }
        return String.format("%012d", digits.isBlank() ? 0 : Long.parseLong(digits));
    }

    private byte[] writePng(BitMatrix matrix) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not generate code image", ex);
        }
    }
}
