package com.egt.gateway.repository.externalservices;

import com.egt.gateway.model.ExternalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalServiceRepository extends JpaRepository<ExternalService, Long> {

    @Query("SELECT cd FROM ExternalService cd WHERE cd.name = :externalServiceName")
    Optional<ExternalService> findByServiceName(String externalServiceName);

}
