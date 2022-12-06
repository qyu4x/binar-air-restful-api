package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceResponse {

    private BigDecimal amount;

    private String currencyCode;

    private String display;

}
