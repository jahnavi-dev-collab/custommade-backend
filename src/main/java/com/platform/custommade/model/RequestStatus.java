package com.platform.custommade.model;

public enum RequestStatus {
    OPEN,        // Customer just created request
    QUOTED,      // Tailor has submitted quote
    ORDERED,     // Customer accepted quote → order created
    CANCELLED,
    COMPLETED
}
