package com.ubuntuhealth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 500)
    private String description;

    private String dosage;

    private Integer quantity;

    private Double price;

    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
}