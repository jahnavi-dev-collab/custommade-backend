package com.platform.custommade.model;

import jakarta.persistence.*;

@Entity
@Table(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id")
    private Request request;

    @Column(columnDefinition = "jsonb")
    private String measurementData; // store JSON as string

    // Getters & Setters
}
