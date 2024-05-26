package com.egt.gateway.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "currency_code")
@Entity
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;
}
