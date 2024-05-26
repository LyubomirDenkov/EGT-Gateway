package com.egt.gateway.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "request_history")
@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @ManyToOne
    @JoinColumn(name = "external_service_id")
    private ExternalService externalService;

    @Column(name = "client")
    private String client;
}
