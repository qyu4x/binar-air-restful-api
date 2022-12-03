package com.binarair.binarairrestapi.model.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryResponse {
   private List<BookingDetailResponse>data;

   private List<BookingDetailResponse> departureDate;

   private List<ScheduleResponse> departureTime;

   private List<ScheduleResponse> arrivalTime;

   private List<PassengerBookingResponse> passenger;

   private LocalDateTime updatedAt;
}
