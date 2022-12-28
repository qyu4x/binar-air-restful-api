package com.binarair.binarairrestapi.service.impl;

import com.binarair.binarairrestapi.exception.DataNotFoundException;
import com.binarair.binarairrestapi.repository.BookingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ETicketServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private ETicketServiceImpl eTicketService;

    @Test
    void testCreateTicketDataNotFound() {
        String bookingId = "random-booking-id";

        Mockito.when(bookingRepository.findBookingDetailsById(bookingId))
                .thenReturn(null);

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            eTicketService.createticket(bookingId);
        });
    }

    @Test
    void testCreateTicketSuccess() {
    }
}