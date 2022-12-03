package com.binarair.binarairrestapi.repository;

import com.binarair.binarairrestapi.model.entity.History;
import com.binarair.binarairrestapi.model.request.HistoryRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM history hs\n" +
                    "JOIN booking_detail= bd on bk.id = bd.booking_unique_id\n" +
                    "JOIN passenger p on bd.passenger_unique_id = p.id\n" +
                    "WHERE bk.id = :bookingid"
    )
    History findHistoryById(@Param("historyid") String historyId);

    Optional<History> findHistoryById(List<HistoryRequest> data);
}
