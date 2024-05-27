package com.egt.gateway.repository.history;

import com.egt.gateway.model.ExternalService;
import com.egt.gateway.model.RequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<RequestHistory, Long> {

    @Query("SELECT cd FROM RequestHistory cd WHERE cd.requestId = :requestId")
    Optional<RequestHistory> findByRequestId(String requestId);

    @Query("SELECT COUNT(cd) FROM RequestHistory cd WHERE cd.externalService.name = :serviceId AND cd.requestTime BETWEEN :startTime AND :endTime")
    Long countByServiceIdAndTimestampBetween(String serviceId, LocalDateTime startTime, LocalDateTime endTime);

}
