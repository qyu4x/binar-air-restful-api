package com.binarair.binarairrestapi.service;

import com.binarair.binarairrestapi.model.request.TicketJasperRequest;
import com.binarair.binarairrestapi.model.response.ETicketResponse;
import net.sf.jasperreports.engine.JRException;

import java.io.FileNotFoundException;

public interface ETicketService {



    byte[] createticket(String bookingId) throws JRException, FileNotFoundException;
}
