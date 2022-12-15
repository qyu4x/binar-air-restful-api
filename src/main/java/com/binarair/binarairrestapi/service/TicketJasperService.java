package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.TicketJasperRequest;
import com.binarair.binarairrestapi.model.response.BookingDetailResponse;
import com.binarair.binarairrestapi.model.response.TicketJasperResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface TicketJasperService {


    byte[] createpdf(String lastName, String bookingReferenceNumber) throws JRException, FileNotFoundException;
}
