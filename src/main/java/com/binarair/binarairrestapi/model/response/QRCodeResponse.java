package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.util.Base64;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QRCodeResponse {
    String Base64QRCode;

}
