package com.binarair.binarairrestapi.controller;

import com.binarair.binarairrestapi.model.response.QRCodeResponse;
import com.binarair.binarairrestapi.model.response.WebResponse;
import com.binarair.binarairrestapi.service.QRcodeGeneratorService;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import net.bytebuddy.description.type.TypeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/api/v1")
public class QRcodeController {

    private final QRcodeGeneratorService qRcodeGeneratorService;

    @Autowired
    public QRcodeController(QRcodeGeneratorService qRcodeGeneratorService) {
        this.qRcodeGeneratorService = qRcodeGeneratorService;
    }
    @Operation(summary = "Output a QR linked to Product Owner's whatsapp number (Nico)")
    @GetMapping("/qr")
    public ResponseEntity<WebResponse<QRCodeResponse>> getQRCode(){
        String link = "https://wa.me/+62895411041801";

        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = qRcodeGeneratorService.getQRCodeImage(link,250,250);

        } catch (WriterException | IOException exception) {
            exception.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);

        QRCodeResponse qrCodeResponse = QRCodeResponse.builder()
                .Base64QRCode(qrcode)
                .build();

        WebResponse webResponse = new WebResponse(
                HttpStatus.CREATED.value(),
                HttpStatus.CREATED.getReasonPhrase(),
                qrCodeResponse
        );
        return new ResponseEntity<>(webResponse, HttpStatus.CREATED);
    }
}
