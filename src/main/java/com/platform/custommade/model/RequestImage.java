package com.platform.custommade.model;

import jakarta.persistence.*;

@Entity
@Table(name = "request_images")
public class RequestImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

    private String imageUrl;

    // Getters & Setters
}
