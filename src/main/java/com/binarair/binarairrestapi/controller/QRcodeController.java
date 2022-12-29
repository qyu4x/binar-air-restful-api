package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.service.QRcodeGeneratorService;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Base64;

public class QRcodeController {

    private final QRcodeGeneratorService qRcodeGeneratorService;

    @Autowired
    public QRcodeController(QRcodeGeneratorService qRcodeGeneratorService) {
        this.qRcodeGeneratorService = qRcodeGeneratorService;
    }

    @GetMapping("/qr")
    public String getQRCode(Model model){
        String link = "https://wa.me/0895411041801";

        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = qRcodeGeneratorService.getQRCodeImage(link,250,250);

        } catch (WriterException | IOException exception) {
            exception.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);


        return qrcode;
    }
}
