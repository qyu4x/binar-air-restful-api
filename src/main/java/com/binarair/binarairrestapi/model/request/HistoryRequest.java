package com.binarair.binarairrestapi.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryRequest {
    private List<HistoryRequest> data;
}
