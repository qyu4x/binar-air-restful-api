package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.CheckInRequest;
import com.binarair.binarairrestapi.model.request.CancelCheckInRequest;
import com.binarair.binarairrestapi.model.response.CancelCheckInResponse;
import com.binarair.binarairrestapi.model.response.CheckInResponse;

public interface CheckInService {

    CheckInResponse checkIn(CheckInRequest checkInRequest);

    CancelCheckInResponse cancelCheckIn(CancelCheckInRequest cancelCheckInRequest);

}
