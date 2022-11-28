package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailRepository extends JpaRepository<BookingDetail, String> {
}
