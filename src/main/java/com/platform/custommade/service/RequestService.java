package com.platform.custommade.service;

import com.platform.custommade.model.Request;
import com.platform.custommade.model.User;
import com.platform.custommade.repository.RequestRepository;
import com.platform.custommade.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.platform.custommade.model.RequestStatus;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    public RequestService(RequestRepository requestRepository,
                          UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    // ✅ Create request
    public Request createRequest(Long customerId, Request request) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        request.setCustomer(customer);
        request.setStatus(RequestStatus.OPEN);
        request.setCreatedAt(LocalDateTime.now());

        return requestRepository.save(request);
    }

    // ✅ Get all requests of a customer
    public List<Request> getRequestsByCustomer(Long customerId) {

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        return requestRepository.findByCustomer(customer);
    }

    // ✅ Get request by ID
    public Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
    }
}
