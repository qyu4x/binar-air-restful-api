package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.ScheduleRequest;
import com.binarair.binarairrestapi.model.response.RoundTripTicketResponse;
import com.binarair.binarairrestapi.model.response.ScheduleResponse;
import com.binarair.binarairrestapi.model.response.TicketResponse;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    RoundTripTicketResponse filterFullTwoSearch(String airport, String departure, String passenger, String serviceClass);

    List<TicketResponse> filterFullSearch(String airport, String departure, String passenger, String serviceClass);

    ScheduleResponse save(ScheduleRequest scheduleRequest);

    List<ScheduleResponse> getAll();

    Boolean delete(String scheduleId);

    ScheduleResponse findById(String scheduleId);

}
